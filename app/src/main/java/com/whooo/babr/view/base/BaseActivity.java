package com.whooo.babr.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.whooo.babr.BarApplication;
import com.whooo.babr.R;
import com.whooo.babr.util.dialog.DialogFactory;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public BarApplication getApp() {
        return (BarApplication) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }

    protected abstract BasePresenter getPresenter();

    protected void reloadActivity() {
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    protected void showNetworkErrorDialog() {
        DialogFactory
                .createSimpleOkErrorDialog(this, R.string.error_connection_lost_title, R.string.error_connection_lost_message)
                .show();
    }

    protected void showInAppErrorDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_generic_error_title)
                .setMessage(R.string.error_generic_error_message)
                .setNeutralButton(getString(R.string.dialog_action_send_feedback), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton(R.string.dialog_action_ok, null);

        alertDialog.create().show();
    }
}
