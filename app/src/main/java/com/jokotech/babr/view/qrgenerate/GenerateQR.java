package com.jokotech.babr.view.qrgenerate;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jokotech.babr.R;
import com.jokotech.babr.view.main.MainActivity;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ThongLe on 3/5/2016.
 */
public class GenerateQR extends AppCompatActivity implements Animation.AnimationListener {

    @Bind(R.id.iv_qrGenerate)
    ImageView ivQRGenerate;
    @Bind(R.id.laybar)
    ImageView ivLaybar;
    @Bind(R.id.tv_generating)
    TextView tvGenerating;
    private MediaPlayer mediaPlayer;
    private Observable observable;
    private Animation animation;
    private ViewGroup viewGroup;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gererate_qr);

        ButterKnife.bind(this);

        userId=getIntent().getStringExtra(MainActivity.USER_ID_EXTRA);

        mediaPlayer = MediaPlayer.create(this, R.raw.scan);
        animation = AnimationUtils.loadAnimation(this, R.anim.highlight);
        animation.setAnimationListener(this);
        viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> {
                    mediaPlayer.start();
                    ivLaybar.startAnimation(animation);
                });

        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    if (animation != null) {
                        ivLaybar.setVisibility(View.GONE);
                    }

                    ivLaybar.clearAnimation();

                    tvGenerating.setVisibility(View.GONE);
                    Bitmap myBitmap = QRCode.from(userId).bitmap();
                    ivQRGenerate.setImageBitmap(myBitmap);
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
