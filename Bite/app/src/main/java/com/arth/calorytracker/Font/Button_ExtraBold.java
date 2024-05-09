package com.arth.calorytracker.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by android on 3/19/2018.
 */

public class Button_ExtraBold extends Button {
    public Button_ExtraBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public Button_ExtraBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public Button_ExtraBold(Context context) {
        super(context);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Montserrat-ExtraBold.otf");
            setTypeface(tf);
        }
    }
}
