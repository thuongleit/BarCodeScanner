package com.whooo.babr.view.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.whooo.babr.R;
import com.whooo.babr.databinding.FragmentCartBinding;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.util.dialog.QrCodeDialogFactory;
import com.whooo.babr.view.base.BaseFragment;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.cart.detail.DetailActivity;
import com.whooo.babr.vo.Cart;

import javax.inject.Inject;


public class CartFragment extends BaseFragment implements CartContract.View {

    private static final String ARG_IS_NEED_PENDING = "ARG_IS_NEED_PENDING";

    private AlertDialog mProgressDialog;

    private boolean mIsPending;

    @Inject
    CartContract.Presenter mPresenter;

    public static Fragment createInstance(boolean needPending) {
        Fragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_NEED_PENDING, needPending);
        fragment.setArguments(args);

        return fragment;
    }

    public CartFragment() {
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null && getArguments() != null){
            mIsPending = getArguments().getBoolean(ARG_IS_NEED_PENDING, false);
        }else if(savedInstanceState != null){
            mIsPending = savedInstanceState.getBoolean(ARG_IS_NEED_PENDING, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCartBinding binding = FragmentCartBinding.inflate(inflater, container, false);

        if (mIsPending) {
            setHasOptionsMenu(true);
        }

        initInjector();
        initViews(binding);
        binding.setPresenter((CartPresenter) mPresenter);
        binding.setViewModel(mPresenter.getViewModel());

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_NEED_PENDING, mIsPending);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNetworkError() {
        DialogFactory
                .createSimpleOkErrorDialog(getContext(), R.string.dialog_error_title, R.string.dialog_internet_disconnnect_error)
                .show();
    }

    @Override
    public void showInAppError() {
        DialogFactory
                .createSimpleOkErrorDialog(getContext(), R.string.dialog_error_title, R.string.dialog_error_general_message)
                .show();
    }

    private void initInjector() {
        DaggerCartComponent
                .builder()
                .applicationComponent(activity().getApp().getAppComponent())
                .cartModule(new CartModule(this, mIsPending))
                .build()
                .inject(this);

    }

    private void initViews(FragmentCartBinding binding) {
        RecyclerView mRecyclerView = binding.recyclerView;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void startDetailActivity(Cart cart) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CART_ID, cart.objectId);
        intent.putExtra(DetailActivity.EXTRA_CART_NAME, cart.name);
        intent.putExtra(DetailActivity.EXTRA_IS_PENDING, mIsPending);
        startActivity(intent);
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
        }

        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showConfirmDialogInDelete(@NonNull Cart cart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to delete " + cart.name + "? \nIt's permanently deleted.");
        builder.setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> mPresenter.deleteCart(cart));
        builder.setNegativeButton(R.string.dialog_action_cancel, null);
        builder.create().show();
    }

    @Override
    public void showConfirmDialogInCheckout(@NonNull Cart cart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to checkout " + cart.name + "?");
        builder.setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> mPresenter.checkout(cart));
        builder.setNegativeButton(R.string.dialog_action_cancel, null);
        builder.create().show();
    }

    @Override
    public void generateQRCode(@NonNull Cart cart) {
        QrCodeDialogFactory.create(getContext(), cart.objectId).show();
    }
}
