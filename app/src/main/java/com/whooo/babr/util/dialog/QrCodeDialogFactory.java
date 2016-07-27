package com.whooo.babr.util.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.whooo.babr.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class QrCodeDialogFactory extends AlertDialog {

    public static AlertDialog create(Context context, @NonNull String listId) {
        return new QrCodeDialogFactory(context, listId);
    }

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    private QrCodeDialogFactory(Context context, String listId) {
        super(context);
        init(context, listId);
    }

    private void init(Context context, String listId) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_qrcode_history, null);
        setView(view);
        ImageView ivQrCode = (ImageView) view.findViewById(R.id.iv_qrcode);
        View laybar = view.findViewById(R.id.laybar);
        Bitmap myBitmap = QRCode.from(listId).bitmap();

        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.scan);
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        }
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.highlight);

        mSubscriptions.add(Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                    if (laybar != null) {
                        laybar.startAnimation(animation);
                    }
                }));

        mSubscriptions.add(Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    if (animation != null) {
                        if (laybar != null) {
                            laybar.setVisibility(View.GONE);
                        }
                    }

                    if (laybar != null) {
                        laybar.clearAnimation();
                    }

                    if (ivQrCode != null) {
                        ivQrCode.setImageBitmap(myBitmap);
                    }
                }));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubscriptions != null) {
            mSubscriptions.clear();
            mSubscriptions = null;
        }
    }
}
