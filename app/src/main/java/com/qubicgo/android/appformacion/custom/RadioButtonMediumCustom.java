package com.qubicgo.android.appformacion.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.utilities.Fonts;


public class RadioButtonMediumCustom extends AppCompatRadioButton {
    public RadioButtonMediumCustom(Context context) {
        super(context);
        init(context);
    }

    public RadioButtonMediumCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadioButtonMediumCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Fonts.BENTON_BBVA_MEDIUM);
        setTypeface(typeface);
        setButtonDrawable(getResources().getDrawable(R.drawable.custom_radio_button));
        Drawable img = getContext().getResources().getDrawable(R.drawable.custom_radio_button);
        img.setBounds(0, 0, 0, 0);
        setCompoundDrawables(img, null, null, null);
        setGravity(Gravity.TOP);
    }
}
