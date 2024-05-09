package com.arth.calorytracker.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by android on 3/19/2018.
 */

public class RadioTextView_Medium extends RadioButton {
    public RadioTextView_Medium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RadioTextView_Medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioTextView_Medium(Context context) {
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
