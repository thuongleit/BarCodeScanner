package com.whooo.barscanner.widget;

import android.app.Dialog;
import android.content.Context;

import com.whooo.barscanner.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class CustomProgressDialog extends Dialog {
    CircleProgressBar progress1;

    Context mContext;
    CustomProgressDialog dialog;

    public CustomProgressDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomProgressDialog show(CharSequence message) {
        dialog = new CustomProgressDialog(mContext, R.style.ProgressDialog);
        dialog.setContentView(R.layout.view_material_progress);

        progress1 = (CircleProgressBar) dialog.findViewById(R.id.progress1);
        dialog.setCancelable(true);
        if (dialog != null) {
            dialog.show();
        }
        return dialog;
    }

    public CustomProgressDialog dismiss(CharSequence message) {
        if (dialog != null) {
            dialog.dismiss();
        }

        return dialog;
    }
}