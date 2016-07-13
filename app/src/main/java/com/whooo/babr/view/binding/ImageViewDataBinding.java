package com.whooo.babr.view.binding;


import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.whooo.babr.di.components.ApplicationComponent;
import com.squareup.picasso.Picasso;

public class ImageViewDataBinding {

    @BindingAdapter("url")
    public static void setImageResource(ApplicationComponent component, ImageView view, String imageUrl) {
        component.picasso()
                .load(imageUrl)
                .priority(Picasso.Priority.LOW)
                .into(view);
    }
}
