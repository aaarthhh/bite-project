package com.arth.calorytracker.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * Created by android on 3/19/2018.
 */

public class CheckedTextView_Medium extends CheckedTextView {
    public CheckedTextView_Medium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CheckedTextView_Medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckedTextView_Medium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Montserrat-Medium.otf");
            setTypeface(tf);
        }
    }
}
