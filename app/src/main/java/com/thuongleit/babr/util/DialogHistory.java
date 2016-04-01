package com.thuongleit.babr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thuongleit.babr.R;
import com.thuongleit.babr.view.qrgenerate.QRCode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ThongLe on 3/31/2016.
 */
public class DialogHistory  extends AlertDialog {
    private Context context;
    private DialogSaveHistoryListener listener;

    @Bind(R.id.edNameListHistory)
    EditText edName;

    public DialogHistory(Context context, DialogSaveHistoryListener listener,String listId) {
        super(context);
        this.context = context;
        this.listener = listener;
        init(context,listId);

    }

    private void init(Context context,String listId) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.dialog_add_history, null);
        setView(inflater);
        ButterKnife.bind(this,inflater);

    }

    @OnClick(R.id.btnCancel)
    void onCancel(View v) {
        listener.onCancel();
        dismiss();
    }

    @OnClick(R.id.btnSave)
    void onChoose(View v) {
        if (!TextUtils.isEmpty(edName.getText().toString().trim())) {
            listener.onSave(edName.getText().toString().trim());
            dismiss();
        }else {
            Toast.makeText(context, "Please Input Name Of List", Toast.LENGTH_SHORT).show();
        }
    }



    public interface DialogSaveHistoryListener {
        void onCancel();

        void onSave(String name);

    }
}
