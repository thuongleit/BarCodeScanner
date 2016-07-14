package com.whooo.babr.view.binding;


import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.whooo.babr.di.ApplicationComponent;
import com.squareup.picasso.Picasso;

public class ImageViewDataBinding {

    @BindingAdapter("url")
    public static void setImageResource(ApplicationComponent component, ImageView view, @Nullable String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            component.picasso()
                    .load(imageUrl)
                    .priority(Picasso.Priority.LOW)
                    .into(view);
        }
    }
}
