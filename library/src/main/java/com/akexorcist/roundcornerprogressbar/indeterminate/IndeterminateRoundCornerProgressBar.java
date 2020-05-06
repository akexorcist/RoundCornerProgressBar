package com.akexorcist.roundcornerprogressbar.indeterminate;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

@SuppressWarnings("unused")
public class IndeterminateRoundCornerProgressBar extends RoundCornerProgressBar {
    public IndeterminateRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndeterminateRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndeterminateRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView() {
        super.initView();
        setMax(100);
    }

    @Override
    protected void onProgressChangeAnimationUpdate(LinearLayout layout, float current, float to) {
        super.onProgressChangeAnimationUpdate(layout, current, to);
        if (!isShown()) {
            super.stopProgressAnimationImmediately();
        }
    }

    @Override
    protected void onProgressChangeAnimationEnd(final LinearLayout layout) {
        if (isShown()) {
            startIndeterminateAnimation();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            startIndeterminateAnimation();
        }
    }

    private void startIndeterminateAnimation() {
        disableAnimation();
        setProgress(0);
        enableAnimation();
        setProgress(100);
    }

    private void stopIndeterminateAnimation() {
        super.stopProgressAnimationImmediately();
    }
}
