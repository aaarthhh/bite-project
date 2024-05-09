package com.arth.calorytracker.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by android on 3/19/2018.
 */

public class Button_SemiBold extends Button {
    public Button_SemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Button_SemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Button_SemiBold(Context context) {
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
