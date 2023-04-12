package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar2;

@Keep
public class RoundCornerProgressBar extends AnimatedRoundCornerProgressBar2 {

    public RoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int initLayout() {
        return R.layout.layout_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(@NonNull Context context, @Nullable AttributeSet attrs) {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void drawProgress(@NonNull LinearLayout layoutProgress,
                                @NonNull GradientDrawable progressDrawable,
                                float max,
                                float progress,
                                float totalWidth,
                                int radius,
                                int padding,
                                boolean isReverse) {
        int newRadius = radius - (padding / 2);
        progressDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        layoutProgress.setBackground(progressDrawable);
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
