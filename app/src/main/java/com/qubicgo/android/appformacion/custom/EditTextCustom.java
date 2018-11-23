package com.qubicgo.android.appformacion.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.Gravity;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.utilities.Fonts;


public class EditTextCustom extends AppCompatEditText {
    public EditTextCustom(Context context) {
        super(context);
        init(context);
    }

    public EditTextCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Fonts.BENTON_BBVA_BOOK);
        setTypeface(typeface);
        setGravity(Gravity.CENTER);
        setTextColor(context.getResources().getColor(R.color.gray));
    }
}
