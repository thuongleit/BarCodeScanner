package com.whooo.babr.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.whooo.babr.R;

public class GoogleProgressBar extends ProgressBar {

    public GoogleProgressBar(Context context) {
        this(context, null);
    }

    public GoogleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public GoogleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Due to some new ADT features, initialing with values from resource file may meet preview problems.
        // If View.isInEditMode() returns true, skip drawing.
        if (isInEditMode()) {
            return;
        }

        Drawable drawable = buildDrawable(context, R.array.google_colors);
        if (drawable != null) {
            setIndeterminateDrawable(drawable);
        }
    }

    private Drawable buildDrawable(Context context, int colorsId) {
        return new ChromeFloatingCirclesDrawable.Builder(context)
                .colors(getResources().getIntArray(colorsId))
                .build();
    }
}
