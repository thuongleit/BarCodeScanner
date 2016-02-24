package com.thuongleit.babr.view.widget;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.thuongleit.babr.util.CameraUtils;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "CameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private PreviewCallback mPreviewCallback;
    private AutoFocusCallback mAutoFocusCallback;

    public CameraPreview(Context context, PreviewCallback previewCallback, AutoFocusCallback autoFocusCb) {
        super(context);

        mPreviewCallback = previewCallback;
        mAutoFocusCallback = autoFocusCb;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            requestLayout();
        }
    }


    public void hideSurfaceView() {
        this.setVisibility(View.INVISIBLE);
    }

    public void showSurfaceView() {
        this.setVisibility(View.VISIBLE);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.cancelAutoFocus();
            mCamera.stopPreview();
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        //stop camera
        try {
            mCamera.stopPreview();
        } catch (Exception e) {

        }

        startCamera();
    }

    public void startCamera() {
        try {
            if (mCamera != null) {
                // FIXME: 9/12/15 Should be fixed for screen
                // Hard code camera surface rotation 90 degs to match Activity view in portrait
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setPreviewCallback(mPreviewCallback);
                mCamera.startPreview();
                mCamera.autoFocus(mAutoFocusCallback);
            }
        } catch (IOException e) {

        }
    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.cancelAutoFocus();
        }
    }

    public void setFlash(boolean flag) {
        if(this.mCamera != null && CameraUtils.isFlashSupported(this.mCamera)) {
            Camera.Parameters parameters = this.mCamera.getParameters();
            if(flag) {
                if(parameters.getFlashMode().equals("torch")) {
                    return;
                }

                parameters.setFlashMode("torch");
            } else {
                if(parameters.getFlashMode().equals("off")) {
                    return;
                }

                parameters.setFlashMode("off");
            }

            this.mCamera.setParameters(parameters);
        }
    }
}
