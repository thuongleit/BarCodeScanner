package com.jokotech.babr.view.scan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jokotech.babr.R;
import com.jokotech.babr.config.Constant;
import com.jokotech.babr.di.ActivityScope;
import com.jokotech.babr.util.AppUtils;
import com.jokotech.babr.util.RevealBackgroundView;
import com.jokotech.babr.util.dialog.DialogFactory;
import com.jokotech.babr.view.base.ToolbarActivity;
import com.jokotech.babr.view.widget.CameraPreview;
import com.jokotech.babr.view.widget.ViewFinderView;
import com.jokotech.babr.vo.Product;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;

public class CameraActivity extends ToolbarActivity implements ScanView, Camera.PreviewCallback{

    public static final String EXTRA_SERVICE = "CameraActivity.EXTRA_SERVICE";
    public static final String EXTRA_DATA = "CameraActivity.EXTRA_DATA";
    public static final String EXTRA_LOAD_USER_ID = "load_user_id";
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";
    private static final int REQUEST_RESULT_ACTIVITY = 1;

    @Bind(R.id.cameraPreview)
    FrameLayout mCameraPreview;
    @Bind(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
    @Bind(R.id.viewFinder)
    ViewFinderView viewFinderView;

    @Inject
    ProductLookupPresenter mProductLookupPresenter;
    @Inject
    @ActivityScope
    Context mContext;

    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageScanner mScanner;


    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;
    private boolean mFlash = false;
    private ProgressDialog mProgressDialog;
    private String mService;
    private int order = 0;
    private String mCode;
    private boolean isOnlyResult = false;
    private int state=0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);

        mSupportActionBar.setDisplayHomeAsUpEnabled(true);

        mService = getIntent().getStringExtra(EXTRA_SERVICE);
        mAutoFocusHandler = new Handler();
//        if (state==0) {
//            setupRevealBackground(savedInstanceState);
//            state=1;
//        }else {
//            mCameraPreview.setVisibility(View.VISIBLE);
//            viewFinderView.setVisibility(View.VISIBLE);
//        }
        // Create and configure the ImageScanner;
        setupScanner();
        //Create and Configure Camera
        setupCamera();
        if (!AppUtils.isCameraAvailable(mContext)) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
        }
        mProductLookupPresenter.attachView(this);
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
    public void onDestroy() {
        super.onDestroy();
        mProductLookupPresenter.detachView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_flash) {
            if (mFlash) {
                Toast.makeText(mContext, "Flash [OFF]", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_flash_off);
                mFlash = false;
            } else {
                Toast.makeText(mContext, "Flash [ON]", Toast.LENGTH_SHORT).show();
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
                    intent.putParcelableArrayListExtra(EXTRA_DATA, data.getParcelableArrayListExtra(SearchResultActivity.EXTRA_DATA));
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
            mProgressDialog = DialogFactory.createProgressDialog(mContext, "Searching...");
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
    public void onEmptyProductReturn() {
        //Toast.makeText(mContext, "No Item Found", Toast.LENGTH_SHORT).show();
        //  swistchToNextScan("No items found on all services");
    }

    @Override
    public void onRequestSuccess(Parcelable parcelable) {


        Intent intent = new Intent(mContext, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.EXTRA_DATA, parcelable);
        startActivityForResult(intent, REQUEST_RESULT_ACTIVITY);


    }


    @Override
    public void onRequestSuccessList(List<Product> parcelables) {

        Intent intent = new Intent(mContext, SearchResultActivity.class);
        intent.putParcelableArrayListExtra(SearchResultActivity.EXTRA_DATA, (ArrayList<? extends Parcelable>) parcelables);
        intent.putExtra(EXTRA_LOAD_USER_ID, true);
        startActivityForResult(intent, REQUEST_RESULT_ACTIVITY);

    }

    @Override
    public void showNetworkError() {
        buildFailedDialog("You has been disconnected!").show();
    }

    @Override
    public void showGeneralError(String message) {
        //  swistchToNextScan(message);
    }



//    private void setupRevealBackground(Bundle savedInstanceState) {
//        vRevealBackground.setOnStateChangeListener(this);
//        final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
//        if (savedInstanceState == null && startingLocation.length > 0) {
//
//            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
//                    vRevealBackground.startFromLocation(startingLocation);
//                    return true;
//                }
//            });
//        } else {
//            vRevealBackground.setToFinishedFrame();
//            // userPhotosAdapter.setLockedAnimations(true);
//        }
//    }

    private void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);
    }

    private void setupCamera() {
        mPreview = new CameraPreview(mContext, this, autoFocusCB);
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
    public static Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();
        } catch (Exception e) {
            Timber.e(e, "Cannot open camera. ");
        }
        return c;
    }

    public void cancelRequest() {
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
                        Timber.i("Scan Code Result %s", sym.getComponents());

                        String scanResult = sym.getData().trim();

                        mCode = scanResult;
                        //Use Below function to make a server call and complete request.
                        mProductLookupPresenter.execute(scanResult, mService);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Timber.e(e, "Failed in getting qr code from server");
            DialogFactory.createTryAgainDialog(mContext, "Cannot query this QR Code from server. Please try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    reloadActivity();
                }
            });
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    @NonNull
    private AlertDialog.Builder buildFailedDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.dialog_error_title)).setMessage(message);
        builder.setPositiveButton("Scan Again", (dialog, which) -> {
            dialog.dismiss();
            startScan();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(dialog -> {
            dialog.dismiss();
            reloadActivity();
        });
        return builder;
    }

//    @Override
//    public void onStateChange(int state) {
//        if (RevealBackgroundView.STATE_FINISHED == state) {
//            mCameraPreview.setVisibility(View.VISIBLE);
//            viewFinderView.setVisibility(View.VISIBLE);
//        } else {
//            mCameraPreview.setVisibility(View.INVISIBLE);
//            viewFinderView.setVisibility(View.INVISIBLE);
//
//        }
//    }

    public interface TaskCompleteListener {
        void onTaskComplete(boolean isFinished);
    }
}
