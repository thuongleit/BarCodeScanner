package com.whooo.barscanner.view.scan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.whooo.barscanner.R;
import com.whooo.barscanner.di.ActivityScope;
import com.whooo.barscanner.util.AppUtils;
import com.whooo.barscanner.util.DialogFactory;
import com.whooo.barscanner.view.base.BaseFragment;
import com.whooo.barscanner.view.widget.CameraPreview;
import com.whooo.barscanner.vo.Product;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CameraFragment extends BaseFragment implements Camera.PreviewCallback, ScanQrCodeView {

    public static final String EXTRA_DATA =
            "com.whooo.barscanner.view.scan.CameraFragment.EXTRA_DATA";

    @Bind(R.id.cameraPreview)
    FrameLayout mCameraPreview;

    @Inject
    ScanQrCodePresenter mScanQrCodePresenter;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrscan_camera, container, false);
        ButterKnife.bind(this, view);
        getComponent().inject(this);

        mAutoFocusHandler = new Handler();

        // Create and configure the ImageScanner;
        setupScanner();
        setHasOptionsMenu(true);
        //Create and Configure Camera
        setupCamera();
        if (!AppUtils.isCameraAvailable(getActivity())) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
        }
        mScanQrCodePresenter.attachView(this);

        return view;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_camera, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_flash) {
            if (mFlash) {
                Toast.makeText(getActivity(), "Flash [OFF]", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_flash_off_white_24dp);
                mFlash = false;
            } else {
                Toast.makeText(getActivity(), "Flash [ON]", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_flash_on_white_24dp);
                mFlash = true;
            }
            mPreview.setFlash(mFlash);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExecuteFinished(Product product) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_DATA, product);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogFactory.createProgressDialog(mContext, "Executing...");
        }

        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showNetworkError() {
        DialogFactory.createGenericErrorDialog(mContext, "You has been disconnected!").show();
    }

    @Override
    public void showGeneralError(String message) {
        DialogFactory.createGenericErrorDialog(mContext, message).show();
    }

    private void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);
    }

    private void setupCamera() {
        mPreview = new CameraPreview(getActivity(), this, autoFocusCB);
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
        Toast.makeText(getActivity(), "Camera unavailable", Toast.LENGTH_LONG).show();
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

                        //Use Below function to make a server call and complete request.
                        mScanQrCodePresenter.executeQrCode(scanResult);
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
                    activity().reloadActivity();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.dialog_error_title)).setMessage(message);
        builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startScan();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                activity().reloadActivity();
            }
        });
        return builder;
    }
}
