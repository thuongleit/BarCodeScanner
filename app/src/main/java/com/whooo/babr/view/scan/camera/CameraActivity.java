package com.whooo.babr.view.scan.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.whooo.babr.R;
import com.whooo.babr.databinding.ActivityCameraBinding;
import com.whooo.babr.util.AppUtils;
import com.whooo.babr.util.dialog.DialogFactory;
import com.whooo.babr.view.base.BaseActivity;
import com.whooo.babr.view.base.BasePresenter;
import com.whooo.babr.view.scan.result.ResultActivity;
import com.whooo.babr.view.widget.CameraPreview;
import com.whooo.babr.vo.Product;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class CameraActivity extends BaseActivity implements CameraContract.View, Camera.PreviewCallback{
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private static final int REQUEST_RESULT_ACTIVITY = 1;

    //views
    private FrameLayout mCameraPreview;
    private Toolbar mToolbar;

    @Inject
    CameraContract.Presenter mCameraPresenter;

    private Context mContext;
    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageScanner mScanner;

    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;
    private boolean mFlash = false;
    private AlertDialog mProgressDialog;

    private Runnable mDoAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing) {
                mCamera.autoFocus(mAutoFocusCB);
            }
        }
    };

    // Mimic continuous auto-focusing
    private Camera.AutoFocusCallback mAutoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(mDoAutoFocus, 1000);
        }
    };

    @Override
    protected BasePresenter getPresenter() {
        return mCameraPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCameraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        mContext = this;

        initializeInjector();
        setupViews(binding);
        mAutoFocusHandler = new Handler();
        // Create and configure the ImageScanner;
        setupScanner();
        //Create and Configure Camera
        setupCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = getCameraInstance();
        startScan();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            // According to Jason Kuang on http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
            // there might be surface recreation problems when the device goes to sleep. So lets just hide it and
            // recreate on resume
            stopCamera();
            mPreview.hideSurfaceView();
            mCamera.release();
            mPreview.setCamera(null);
            mCamera = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_flash) {
            if (mFlash) {
                Toast.makeText(mContext, R.string.menu_action_flash_off, Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_flash_off);
                mFlash = false;
            } else {
                Toast.makeText(mContext, R.string.menu_action_flash_on, Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_flash_on);
                mFlash = true;
            }
            mPreview.setFlash(mFlash);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_RESULT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = getIntent();
                    intent.putParcelableArrayListExtra(EXTRA_DATA, data.getParcelableArrayListExtra(ResultActivity.EXTRA_DATA));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    startScan();
                }
                break;
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext);
            mProgressDialog.setCancelable(false);
        }

        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void playRingtone() {
        AppUtils.playSound(mContext);
    }

    @Override
    public void onSearchSuccess(List<Product> products) {
        Intent intent = new Intent(mContext, ResultActivity.class);
        intent.putParcelableArrayListExtra(ResultActivity.EXTRA_DATA, (ArrayList<? extends Parcelable>) products);
        startActivityForResult(intent, REQUEST_RESULT_ACTIVITY);
    }

    @Override
    public void onEmptyResponse() {
        // TODO: 7/9/16 add action
        DialogFactory.createGenericErrorDialog(mContext, "No products found. Try another barcode.").show();
    }

    @Override
    public void showNetworkError() {
        buildFailedDialog(getString(R.string.error_connection_lost_message)).show();
    }

    @Override
    public void showInAppError() {
        buildFailedDialog(getString(R.string.dialog_error_general_message)).show();
    }

    private void setupViews(ActivityCameraBinding binding) {
        mToolbar = binding.toolbar;
        mCameraPreview = binding.preview;
        //mParentPreview = binding.;

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeInjector() {
        DaggerCameraComponent
                .builder()
                .applicationComponent(getApp().getAppComponent())
                .cameraModule(new CameraModule(this))
                .build()
                .inject(this);
    }

    private void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);
    }

    private void setupCamera() {
        mPreview = new CameraPreview(mContext, this, mAutoFocusCB);
        mCameraPreview.addView(mPreview);
    }

    private void startScan() {
        if (mCamera == null) {
            // Cancel request if mCamera is null.
            cancelRequest();
            return;
        }
        mPreview.setCamera(mCamera);
        mPreview.showSurfaceView();
        mPreview.setFlash(mFlash);
        mPreview.startCamera();
        mPreviewing = true;
    }

    private void stopCamera() {
        mPreview.stopCamera();
        mPreviewing = false;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();
        } catch (Exception e) {
            Timber.e(e, "Cannot open camera. ");
        }
        return c;
    }

    private void cancelRequest() {
        Toast.makeText(mContext, "Camera unavailable", Toast.LENGTH_LONG).show();
        mCameraPreview.setVisibility(View.GONE);
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);
            int result = mScanner.scanImage(barcode);


            if (result != 0) {
                stopCamera();
                SymbolSet syms = mScanner.getResults();
                for (Symbol sym : syms) {
                    String symData = sym.getData();
                    if (!TextUtils.isEmpty(symData)) {
                        Timber.d("Scan Code Result %s", sym.getComponents());
                        String code = sym.getData().trim();

                        //Use Below function to make a server call and complete request.
                        mCameraPresenter.searchProducts(code);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "Failed in getting qr code from server");
            buildFailedDialog("Problem in scanning with camera. Please try again.").show();
        }
    }

    @NonNull
    private AlertDialog.Builder buildFailedDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.dialog_error_title)).setMessage(message);
        builder.setPositiveButton(getString(R.string.dialog_action_scan_again), (dialog, which) -> {
            dialog.dismiss();
            startScan();
        });
        builder.setNegativeButton(getString(R.string.dialog_action_cancel), (dialog, which) -> {
            dialog.dismiss();
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        builder.setOnCancelListener(dialog -> {
            dialog.dismiss();
            reloadActivity();
        });
        return builder;
    }
}
