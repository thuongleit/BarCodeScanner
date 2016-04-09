package com.jokotech.babr.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class DancingScriptTextView extends TextView {
    public DancingScriptTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "fonts/DancingScript-Bold.ttf"));
    }
}