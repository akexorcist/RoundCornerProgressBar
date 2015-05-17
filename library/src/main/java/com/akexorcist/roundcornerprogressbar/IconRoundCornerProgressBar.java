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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class IconRoundCornerProgressBar extends BaseRoundCornerProgressBar {
    protected final static int DEFAULT_HEADER_COLOR = Color.parseColor("#ff9f9f9f");
    protected final static int DEFAULT_ICON_SIZE = 40;
    protected final static int DEFAULT_ICON_PADDING = 5;

    protected ImageView imageIcon;
    protected LinearLayout layoutHeader;

    protected boolean isIconSetBeforeDraw;
    protected boolean isHeaderColorSetBeforeDraw;

    protected int headerWidth;
    protected int iconSize;
    protected int iconPadding;
    protected int headerColor;

    @SuppressLint("NewApi")
    public IconRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            iconSize = DEFAULT_ICON_SIZE;
            iconPadding = DEFAULT_ICON_PADDING;
            headerColor = DEFAULT_HEADER_COLOR;

            isIconSetBeforeDraw = false;
            isHeaderColorSetBeforeDraw = false;
        }
    }

    @Override
    protected void setup(TypedArray typedArray, DisplayMetrics metrics) {
        imageIcon = (ImageView) findViewById(R.id.round_corner_progress_icon);
        imageIcon.setScaleType(ScaleType.CENTER_CROP);
        if (!isIconSetBeforeDraw) {
            int iconResource = typedArray.getResourceId(R.styleable.RoundCornerProgress_rcIconSrc, R.drawable.round_corner_progress_icon);
            imageIcon.setImageResource(iconResource);
        }
        iconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconSize, metrics);
        iconSize = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcIconSize, DEFAULT_ICON_SIZE);
        imageIcon.setLayoutParams(new LayoutParams(iconSize, iconSize));

        layoutHeader = (LinearLayout) findViewById(R.id.round_corner_progress_header);

        iconPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconPadding, metrics);
        iconPadding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcIconPadding, DEFAULT_ICON_PADDING);
        layoutHeader.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
        if (!isHeaderColorSetBeforeDraw) {
            setHeaderColor(typedArray.getColor(R.styleable.RoundCornerProgress_rcHeaderColor, DEFAULT_HEADER_COLOR));
        }
    }

    @Override
    protected void onLayoutMeasured() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            headerWidth = layoutHeader.getMeasuredWidth();
        } else {
            headerWidth = layoutHeader.getWidth();
        }
    }

    @Override
    public void setBackgroundLayoutSize(LinearLayout layoutBackground) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setBackgroundWidth(layoutBackground.getMeasuredWidth());
        } else {
            setBackgroundWidth(layoutBackground.getWidth());
        }
    }

    @Override
    protected int initProgressBarLayout() {
        return R.layout.round_corner_with_icon_layout;
    }

    @Override
    protected void setGradientRadius(GradientDrawable gradient) {
        int radius = getRadius() - (getPadding() / 2);
        gradient.setCornerRadii(new float[]{0, 0, radius, radius, radius, radius, 0, 0});
    }

    @Override
    protected float setLayoutProgressWidth(float ratio) {
        return (ratio > 0) ? (getBackgroundWidth() - (headerWidth + (getPadding() * 2))) / ratio : 0;
    }

    @Override
    protected float setSecondaryLayoutProgressWidth(float ratio) {
        return (ratio > 0) ? (getBackgroundWidth() - (headerWidth + (getPadding() * 2))) / ratio : 0;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setHeaderColor(int color) {
        headerColor = color;
        int radius = getRadius() - (getPadding() / 2);
        GradientDrawable gradient = new GradientDrawable();
        gradient.setShape(GradientDrawable.RECTANGLE);
        gradient.setColor(headerColor);
        gradient.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutHeader.setBackground(gradient);
        } else {
            layoutHeader.setBackgroundDrawable(gradient);
        }

        if (!isProgressBarCreated()) {
            isHeaderColorSetBeforeDraw = true;
        }
    }

    public int getHeaderColor() {
        return headerColor;
    }

    public void setIconImageResource(int resource) {
        imageIcon.setImageResource(resource);
    }

    public void setIconImageBitmap(Bitmap bitmap) {
        imageIcon.setImageBitmap(bitmap);
    }

    public void setIconImageDrawable(Drawable drawable) {
        imageIcon.setImageDrawable(drawable);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.headerWidth = this.headerWidth;
        ss.iconSize = this.iconSize;
        ss.iconPadding = this.iconPadding;
        ss.headerColor = this.headerColor;

        ss.isIconSetBeforeDraw = this.isIconSetBeforeDraw;
        ss.isHeaderColorSetBeforeDraw = this.isHeaderColorSetBeforeDraw;
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

        this.headerWidth = ss.headerWidth;
        this.iconSize = ss.iconSize;
        this.iconPadding = ss.iconPadding;
        this.headerColor = ss.headerColor;

        setHeaderColor(headerColor);

        this.isIconSetBeforeDraw = ss.isIconSetBeforeDraw;
        this.isHeaderColorSetBeforeDraw = ss.isHeaderColorSetBeforeDraw;
    }

    private static class SavedState extends BaseSavedState {
        int headerWidth;
        int iconSize;
        int iconPadding;
        int headerColor;

        boolean isIconSetBeforeDraw;
        boolean isHeaderColorSetBeforeDraw;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.headerWidth = in.readInt();
            this.iconSize = in.readInt();
            this.iconPadding = in.readInt();
            this.headerColor = in.readInt();

            this.isIconSetBeforeDraw = in.readByte() != 0;
            this.isHeaderColorSetBeforeDraw = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.headerWidth);
            out.writeInt(this.iconSize);
            out.writeInt(this.iconPadding);
            out.writeInt(this.headerColor);

            out.writeByte((byte) (this.isIconSetBeforeDraw ? 1 : 0));
            out.writeByte((byte) (this.isHeaderColorSetBeforeDraw ? 1 : 0));
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
