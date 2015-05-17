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
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class RoundCornerProgressBar extends BaseRoundCornerProgressBar {

    @SuppressLint("NewApi")
    public RoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected int initProgressBarLayout() {
        return R.layout.round_corner_layout;
    }

    @Override
    protected void setup(TypedArray typedArray, DisplayMetrics metrics) { }

    @Override
    public void setBackgroundLayoutSize(LinearLayout layoutBackground) {
        int height, width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            width = layoutBackground.getMeasuredWidth();
            height = layoutBackground.getMeasuredHeight();
        } else {
            width = layoutBackground.getWidth();
            height = layoutBackground.getHeight();
        }
        if(height - (getPadding() * 2) == 0) {
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
    protected void onLayoutMeasured() { }

    @Override
    protected float setLayoutProgressWidth(float ratio) {
        return (ratio > 0) ? (int) ((getBackgroundWidth() - (getPadding() * 2)) / ratio) : 0;
    }

    @Override
    protected float setSecondaryLayoutProgressWidth(float ratio) {
        return (ratio > 0) ? (int) ((getBackgroundWidth() - (getPadding() * 2)) / ratio) : 0;
    }
}
