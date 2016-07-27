package com.whooo.babr.view.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whooo.babr.R;
import com.whooo.babr.databinding.FragmentProductBinding;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.util.dialog.QrCodeDialogFactory;
import com.whooo.babr.view.base.BaseFragment;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.widget.DividerItemDecoration;
import com.whooo.babr.vo.Product;

import javax.inject.Inject;

public class ShopFragment extends BaseFragment implements ShopContract.View {

    @Inject
    ShopContract.Presenter mPresenter;

    private Context mContext;
    private View mEmptyView;
    private AlertDialog mProgressDialog;
    private FrameLayout mLayoutContent;

    public static Fragment createInstance() {
        return new ShopFragment();
    }

    public ShopFragment() {
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentProductBinding binding = FragmentProductBinding.inflate(inflater, container, false);

        mContext = getContext();
        setHasOptionsMenu(true);

        initInjector();
        initViews(binding);

        binding.setPresenter(mPresenter);
        binding.setViewmodel(mPresenter.getViewModel());

        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_checkout:
                if (mPresenter.getViewModel().data.isEmpty()) {
                    DialogFactory.createGenericErrorDialog(mContext, "You don't have any items!").show();
                } else {
                    performCheckout();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckoutSuccess(String keyOfCart) {
        showToast("Checkout success!");

        AlertDialog alertDialog = QrCodeDialogFactory.create(mContext, keyOfCart);
        alertDialog.show();
    }

    @Override
    public void onRemoveProductsSuccess() {
        showToast("Item removed!");
    }

    @Override
    public void requestFailed(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    @Override
    public void onEmptyResponse() {
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.view_empty_product, null);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutContent.addView(mEmptyView, layoutParams);
    }

    @Override
    public void removeEmptyViewIfNeeded() {
        if (mEmptyView != null && !mPresenter.getViewModel().empty.get()) {
            mLayoutContent.removeView(mEmptyView);
        }
    }

    @Override
    public void addPendingRemove(int position, Product clone) {
        final Snackbar snackbar = Snackbar.make(mLayoutContent, "Item deleted", Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(mContext, R.color.white))
                .setAction("Undo", view -> {
                    mPresenter.undoRemovedProduct(position, clone);
                });
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            snackbar.dismiss();
            mPresenter.removeProducts(clone);
        }, 2000);
    }

    @Override
    public void showStandaloneProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext);
        }
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showNetworkError() {
        DialogFactory
                .createSimpleOkErrorDialog(mContext, R.string.dialog_error_title, R.string.dialog_internet_disconnnect_error)
                .show();
    }

    @Override
    public void showInAppError() {
        DialogFactory
                .createSimpleOkErrorDialog(mContext, R.string.dialog_error_title, R.string.dialog_error_general_message)
                .show();
    }

    private void initInjector() {
        DaggerShopComponent
                .builder()
                .applicationComponent(activity().getApp().getAppComponent())
                .shopModule(new ShopModule(this))
                .build()
                .inject(this);
    }

    private void initViews(FragmentProductBinding binding) {
        mLayoutContent = binding.layoutContent;
        RecyclerView mRecyclerView = binding.recyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void performCheckout() {
        final String[] cartName = {AppUtils.generateTimeStamp()};

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_checkout, null);
        TextInputEditText inputCartName = (TextInputEditText) dialogView.findViewById(R.id.input_cart_name);
        inputCartName.setText(cartName[0]);
        AlertDialog dialogCheckout = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setPositiveButton(R.string.dialog_action_ok, ((dialog, which) -> {
                    cartName[0] = inputCartName.getText().toString();
                    if (TextUtils.isEmpty(cartName[0])) {
                        DialogFactory.createGenericErrorDialog(mContext, "You have to input cart name.").show();
                    } else {
                        inputCartName.setError(null);
                        dialog.dismiss();
                        mPresenter.checkout(cartName[0]);
                    }
                })).setNegativeButton(R.string.dialog_action_cancel, ((dialog, which1) -> {
                    dialog.dismiss();
                }))
                .setNeutralButton("Save for later", (dialog, which) -> {
                    cartName[0] = inputCartName.getText().toString();
                    if (TextUtils.isEmpty(cartName[0])) {
                        DialogFactory.createGenericErrorDialog(mContext, "You have to input cart name.").show();
                    } else {
                        inputCartName.setError(null);
                        dialog.dismiss();
                        mPresenter.addToPending(cartName[0]);
                    }
                }).create();
        dialogCheckout.show();
    }
}
