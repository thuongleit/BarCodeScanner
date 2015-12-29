package com.whooo.barscanner.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.whooo.barscanner.R;
import com.whooo.barscanner.model.BarCode;
import com.whooo.barscanner.net.GetBarCodeAsyncTask;
import com.whooo.barscanner.utils.AppUtils;
import com.whooo.barscanner.utils.Log;
import com.whooo.barscanner.widget.CameraPreview;
import com.whooo.barscanner.widget.CustomProgressDialog;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.parceler.Parcels;

import butterknife.Bind;

public class CameraFragment extends BaseFragment implements Camera.PreviewCallback {

    @Bind(R.id.cameraPreview)
    FrameLayout mCameraPreview;


    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageScanner mScanner;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;

    private CustomProgressDialog mProgressDialog;
    private boolean mFlash = false;
    private String qrCode;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qrscan_camera;
    }

    @Override
    protected void initializeVariables() {
        mAutoFocusHandler = new Handler();
    }

    @Override
    protected void setupViews() {
        // Create and configure the ImageScanner;
        setupScanner();
        setHasOptionsMenu(true);
        //Create and Configure Camera
        setupCamera();
        if (!AppUtils.isCameraAvailable(getActivity())) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
            return;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        mCamera = getCameraInstance();
        startScan();
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
            Log.e("Cannot open camera. " + e.getMessage());
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
                        Log.d("Scan Code Result " + sym.getComponents());

                        String scanResult = sym.getData().trim();

                        //Use Below function to make a server call and complete request.
                        executeQRCode(scanResult);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Failed in getting qr code from server" + e.getMessage());
            showErrorDialog("Cannot check this QR Code from server. Please try again", true);
        }
    }

    void executeQRCode(String qrcode) {
        showProgress();
        this.qrCode = qrcode;
        new GetBarCodeAsyncTask(new GetBarCodeAsyncTask.OnUpdateUICallback() {
            @Override
            public void onUpdateUI(BarCode barCode) {
                hideProgress();
                if (barCode != null) {
                    FragmentActivity activity = CameraFragment.this.getActivity();
                    Intent intent = activity.getIntent();
                    intent.putExtra("data", Parcels.wrap(barCode));
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                } else {
                    buildFailedDialog(String.format("Number %s was incorrect or invalid, either the length or the the check digit may have been incorrect.", qrCode)).show();
                }
            }
        }).execute("http://www.upcitemdb.com/upc/" + qrcode);
    }

    private void showProgress() {
        mProgressDialog = new CustomProgressDialog(getActivity());
        mProgressDialog.show("Please wait...");
    }

    private void hideProgress() {
        mProgressDialog.dismiss("");
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
        builder.setTitle("Error").setMessage(message);
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
                reloadActivity();
            }
        });
        return builder;
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


}
