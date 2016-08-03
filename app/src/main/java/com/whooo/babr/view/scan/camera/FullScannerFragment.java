package com.whooo.babr.view.scan.camera;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.whooo.babr.R;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseFragment;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.scan.result.ResultActivity;
import com.whooo.babr.vo.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import timber.log.Timber;

public class FullScannerFragment extends BaseFragment implements ZBarScannerView.ResultHandler, CameraContract.View {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private static final int REQUEST_RESULT_ACTIVITY = 1;
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    private ZBarScannerView mScannerView;
    private AlertDialog mProgressDialog;

    @Inject
    CameraContract.Presenter mCameraPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return mCameraPresenter;
    }

    public FullScannerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        initInjector();
        mScannerView = new ZBarScannerView(getActivity());
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }
        setupFormats();
        return mScannerView;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.menu_action_flash_on);
            menuItem.setIcon(R.drawable.ic_flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.menu_action_flash_off);
            menuItem.setIcon(R.drawable.ic_flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


        if (mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.menu_action_flash_on);
                } else {
                    item.setTitle(R.string.menu_action_flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if (mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    private void startCamera() {
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            Timber.e(e, "Exception in scan code");
        }
        mCameraPresenter.searchProducts(rawResult.getContents());
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<>();
            for (int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_RESULT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = activity().getIntent();
                    intent.putParcelableArrayListExtra(EXTRA_DATA, data.getParcelableArrayListExtra(ResultActivity.EXTRA_DATA));
                    activity().setResult(Activity.RESULT_OK, intent);
                    activity().finish();
                }else {
                    startCamera();
                }
                break;
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(getContext());
            mProgressDialog.setOnCancelListener(dialog -> {
                mCameraPresenter.unsubscribe();
                startCamera();
            });
        }

        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onSearchSuccess(List<Product> products) {
        Intent intent = new Intent(getContext(), ResultActivity.class);
        intent.putParcelableArrayListExtra(ResultActivity.EXTRA_DATA, (ArrayList<? extends Parcelable>) products);
        startActivityForResult(intent, REQUEST_RESULT_ACTIVITY);
    }

    @Override
    public void onEmptyResponse() {
        DialogFactory.createGenericErrorDialog(getContext(), "No products found. Try another code.").show();
    }

    @Override
    public void stopCamera() {
        mScannerView.stopCamera();
    }

    @Override
    public void showNetworkError() {
        buildFailedDialog(getString(R.string.error_connection_lost_message)).show();
    }

    @Override
    public void showInAppError() {
        buildFailedDialog(getString(R.string.dialog_error_general_message)).show();
    }

    private void initInjector() {
        DaggerCameraComponent
                .builder()
                .applicationComponent(activity().getApp().getAppComponent())
                .cameraModule(new CameraModule(this))
                .build()
                .inject(this);
    }

    @NonNull
    private AlertDialog.Builder buildFailedDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.dialog_error_title)).setMessage(message);
        builder.setPositiveButton(getString(R.string.dialog_action_scan_again), (dialog, which) -> {
            dialog.dismiss();
            startCamera();
        });
        builder.setNegativeButton(getString(R.string.dialog_action_cancel), (dialog, which) -> {
            dialog.dismiss();
            activity().setResult(Activity.RESULT_CANCELED);
            activity().finish();
        });
        builder.setOnCancelListener(dialog -> {
            dialog.dismiss();
            activity().reloadActivity();
        });
        return builder;
    }
}
