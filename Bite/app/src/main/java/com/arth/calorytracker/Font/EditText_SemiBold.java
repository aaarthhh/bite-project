package com.arth.calorytracker.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by android on 3/19/2018.
 */

public class EditText_SemiBold extends EditText {
    public EditText_SemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditText_SemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_SemiBold(Context context) {
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
