package com.thuongleit.babr.util.dialog;

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
 * Created by ThongLe on 4/1/2016.
 */
public class DialogQrcodeHistory extends AlertDialog {



    @Bind(R.id.iv_qrGenerate)
    ImageView ivQRGenerate;

    public DialogQrcodeHistory(Context context,String listId) {
        super(context);
        init(context,listId);

    }

    private void init(Context context,String listId) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.dialog_qrcode_history, null);
        setView(inflater);
        ButterKnife.bind(this,inflater);
        Bitmap myBitmap = QRCode.from(listId).bitmap();
        ivQRGenerate.setImageBitmap(myBitmap);
    }

}
