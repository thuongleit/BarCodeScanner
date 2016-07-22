package com.whooo.babr.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.whooo.babr.R;

public final class DialogFactory {

    public static Dialog createSimpleNoTitleDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {
        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static AlertDialog createProgressDialog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_progress, null));
        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        return dialog;
    }
}