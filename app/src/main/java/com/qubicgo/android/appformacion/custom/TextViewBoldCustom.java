package com.qubicgo.android.appformacion.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.qubicgo.android.appformacion.utilities.Fonts;


public class TextViewBoldCustom extends AppCompatTextView {
    public TextViewBoldCustom(Context context) {
        super(context);
        init(context);
    }

    public TextViewBoldCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewBoldCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Fonts.BENTON_BBVA_BOLD);
        setTypeface(typeface);
    }

}
