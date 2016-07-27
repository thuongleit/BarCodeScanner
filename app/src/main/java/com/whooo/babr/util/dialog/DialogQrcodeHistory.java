package com.whooo.babr.util.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.whooo.babr.R;
import com.whooo.babr.view.qrgenerate.QRCode;

/**
 * Created by ThongLe on 4/1/2016.
 */
public class DialogQrcodeHistory extends AlertDialog {


    ImageView ivQRGenerate;

    public DialogQrcodeHistory(Context context, String listId) {
        super(context);
        init(context, listId);

    }

    private void init(Context context, String listId) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.dialog_qrcode_history, null);
        setView(inflater);
        Bitmap myBitmap = QRCode.from(listId).bitmap();
        ivQRGenerate.setImageBitmap(myBitmap);
    }
}
