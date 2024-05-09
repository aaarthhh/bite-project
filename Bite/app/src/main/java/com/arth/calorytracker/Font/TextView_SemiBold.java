package com.arth.calorytracker.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by android on 3/19/2018.
 */

public class TextView_SemiBold extends TextView {
    public TextView_SemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextView_SemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextView_SemiBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Montserrat-SemiBold.otf");
            setTypeface(tf);
        }
    }
}
