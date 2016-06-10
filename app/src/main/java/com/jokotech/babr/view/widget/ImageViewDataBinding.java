package com.jokotech.babr.view.widget;


import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.jokotech.babr.di.components.ApplicationComponent;
import com.squareup.picasso.Picasso;

public class ImageViewDataBinding {

    @BindingAdapter("android:src")
    public static void setImageResource(ApplicationComponent component, ImageView view, int drawable) {
        component.picasso()
                .load(drawable)
                .priority(Picasso.Priority.LOW)
                .into(view);
    }
}
