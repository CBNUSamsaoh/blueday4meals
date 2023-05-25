package com.example.blueday4meals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CustomRadioOutlineView extends RadioButton {
    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor;

    public CustomRadioOutlineView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public CustomRadioOutlineView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomRadioOutlineView(Context context){
        super(context);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextOutLineView);
        stroke = a.getBoolean(R.styleable.CustomTextOutLineView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.CustomTextOutLineView_textStrokeWidth, 0.0f);
        strokeColor = a.getColor(R.styleable.CustomTextOutLineView_textStrokeColor, 0xffffff);
    }

    @Override
    protected  void onDraw(Canvas canvas){
        if(stroke){
            ColorStateList states = getTextColors();
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);
            getPaint().setStyle(Style.FILL);
            setTextColor(states);
        }
        super.onDraw(canvas);
    }
}
