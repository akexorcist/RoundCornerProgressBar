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

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;


/**
 * Created by Akexorcist on 9/14/15 AD.
 */
@Keep
public class RoundCornerProgressBar extends BaseRoundCornerProgressBar {

    public RoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int initLayout() {
        return R.layout.layout_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(@NonNull Context context, AttributeSet attrs) {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void drawProgress(@NonNull LinearLayout layoutProgress,
                                float max,
                                float progress,
                                float totalWidth,
                                int radius,
                                int padding,
                                int colorProgress,
                                boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }
        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.MarginLayoutParams progressParams = (ViewGroup.MarginLayoutParams) layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        if (padding + (progressWidth / 2) < radius) {
            int margin = Math.max(radius - padding, 0) - (progressWidth / 2);
            progressParams.topMargin = margin;
            progressParams.bottomMargin = margin;
        } else {
            progressParams.topMargin = 0;
            progressParams.bottomMargin = 0;
        }
        layoutProgress.setLayoutParams(progressParams);
    }

    @Override
    protected void onViewDraw() {

    }
}
