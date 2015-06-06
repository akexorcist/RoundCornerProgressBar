/*

Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package com.akexorcist.roundcornerprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;

import java.text.NumberFormat;

public class TextRoundCornerProgressBar extends BaseRoundCornerProgressBar {
    protected final static int DEFAULT_PROGRESS_BAR_HEIGHT = 30;
    protected final static int DEFAULT_TEXT_PADDING = 10;
    protected final static int DEFAULT_TEXT_SIZE = 18;
    protected final static int DEFAULT_TEXT_WIDTH = 100;
    protected final static int DEFAULT_TEXT_COLOR = Color.parseColor("#ff333333");

    protected TextView textViewValue;

    protected String text;
    protected String textUnit;

    protected boolean autoTextChange;

    protected int textSize;
    protected int textPadding;
    protected int textWidth;
    protected int textColor;

    @SuppressLint("NewApi")
    public TextRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int initProgressBarLayout() {
        return R.layout.round_corner_with_text_layout;
    }

    @Override
    protected void setup(TypedArray typedArray, DisplayMetrics metrics) {
        autoTextChange = typedArray.getBoolean(R.styleable.RoundCornerProgress_rcAutoTextChange, false);
        textSize = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcTextProgressSize, DEFAULT_TEXT_SIZE);
        textPadding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcTextProgressPadding, DEFAULT_TEXT_PADDING);
        text = typedArray.getString(R.styleable.RoundCornerProgress_rcTextProgress);
        text = (text == null) ? "" : text;
        textUnit = typedArray.getString(R.styleable.RoundCornerProgress_rcTextProgressUnit);
        textUnit = (textUnit == null) ? "" : textUnit;
        textWidth = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcTextProgressWidth, DEFAULT_TEXT_WIDTH);
        textViewValue = (TextView) findViewById(R.id.round_corner_progress_text);
        textViewValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textViewValue.setTextColor(typedArray.getColor(R.styleable.RoundCornerProgress_rcTextProgressColor, DEFAULT_TEXT_COLOR));
        textViewValue.setText(text);
        textViewValue.setPadding(textPadding, 0, textPadding, 0);
    }

    @Override
    public void setBackgroundLayoutSize(LinearLayout layoutBackground) {
        int height, width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            width = getMeasuredWidth() - textWidth;
            height = getMeasuredHeight();
        } else {
            width = getWidth() - textWidth;
            height = getHeight();
        }
        if(height == 0) {
            height = (int) dp2px(DEFAULT_PROGRESS_BAR_HEIGHT);
        }
        setBackgroundWidth(width);
        setBackgroundHeight(height);
    }

    @Override
    protected void setGradientRadius(GradientDrawable gradient) {
        int radius = getRadius() - (getPadding() / 2);
        gradient.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
    }

    @Override
    protected void onLayoutMeasured() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setBackgroundWidth(getMeasuredWidth() - textWidth);
        } else {
            setBackgroundWidth(getWidth() - textWidth);
        }
    }

    @Override
    protected float setLayoutProgressWidth(float ratio) {
        if (isAutoTextChange()) {
            String strProgress = NumberFormat.getInstance().format((progress % 1 == 0) ? (int) progress : progress);
            textViewValue.setText(strProgress + " " + textUnit);
        }
        return (ratio > 0) ? (getBackgroundWidth() - (getPadding() * 2)) / ratio : 0;
    }

    @Override
    protected float setSecondaryLayoutProgressWidth(float ratio) {
        return (ratio > 0) ? (getBackgroundWidth() - (getPadding() * 2)) / ratio : 0;
    }

    public void setTextUnit(String unit) {
        textUnit = unit;
        setProgress();
    }

    public String getTextUnit() {
        return textUnit;
    }

    public void setTextProgress(CharSequence text) {
        textViewValue.setText(text);
    }

    public CharSequence getTextProgress() {
        return textViewValue.getText();
    }

    public void setTextColor(int color) {
        textColor = color;
        textViewValue.setTextColor(color);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setAutoTextChange(boolean isAuto) {
        autoTextChange = isAuto;
    }

    public boolean isAutoTextChange() {
        return autoTextChange;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.autoTextChange = this.autoTextChange;

        ss.textSize = this.textSize;
        ss.textPadding = this.textPadding;
        ss.textWidth = this.textWidth;
        ss.textColor = this.textColor;

        ss.text = this.text;
        ss.textUnit = this.textUnit;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.autoTextChange = ss.autoTextChange;

        this.textSize = ss.textSize;
        this.textPadding = ss.textPadding;
        this.textWidth = ss.textWidth;
        this.textColor = ss.textColor;
        this.text = ss.text;
        this.textUnit = ss.textUnit;
        setTextProgress(text + textUnit);
        setTextColor(textColor);

    }

    private static class SavedState extends BaseSavedState {
        String text;
        String textUnit;

        int textSize;
        int textPadding;
        int textWidth;
        int textColor;

        boolean autoTextChange;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.textSize = in.readInt();
            this.textPadding = in.readInt();
            this.textWidth = in.readInt();
            this.textColor = in.readInt();

            this.autoTextChange = in.readByte() != 0;

            this.text = in.readString();
            this.textUnit = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.textSize);
            out.writeInt(this.textPadding);
            out.writeInt(this.textWidth);
            out.writeInt(this.textColor);

            out.writeByte((byte) (this.autoTextChange ? 1 : 0));

            out.writeString(this.text);
            out.writeString(this.textUnit);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
