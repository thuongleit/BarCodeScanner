package com.jokotech.babr.util.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jokotech.babr.R;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by ThongLe on 3/23/2016.
 */
public final class DialogSaveImage extends AlertDialog {

    private Context context;
    private DialogSaveImageListener listener;
    private boolean isDontShow = false;

    public DialogSaveImage(Context context, DialogSaveImageListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        init(context);
    }

    private void init(Context context) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.dialog_save_image, null);
        setView(inflater);
        ButterKnife.bind(this, inflater);
    }

    @OnClick(R.id.btnCancel)
    void onCancel(View v) {
        listener.onCancel();
        dismiss();
    }

    @OnCheckedChanged(R.id.cbChoose)
    void onChoose(boolean isChecked) {
            isDontShow = isChecked;
    }

    @OnClick(R.id.btnDontShow)
    void onDontShow(View v) {
        listener.onDontShow(isDontShow);
        dismiss();
    }

    public interface DialogSaveImageListener {
        void onCancel();


        void onDontShow(boolean isDontShow);
    }
}
