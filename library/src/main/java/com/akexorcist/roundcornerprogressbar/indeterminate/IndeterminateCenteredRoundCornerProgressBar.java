package com.akexorcist.roundcornerprogressbar.indeterminate;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.akexorcist.roundcornerprogressbar.CenteredRoundCornerProgressBar;

@SuppressWarnings("unused")
public class IndeterminateCenteredRoundCornerProgressBar extends CenteredRoundCornerProgressBar {
    public IndeterminateCenteredRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndeterminateCenteredRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndeterminateCenteredRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView() {
        super.initView();
        disableAnimation();
        setMax(100);
        setProgress(0);
        enableAnimation();
        setProgress(100);
    }

    @Override
    protected void onProgressChangeAnimationEnd(LinearLayout layout) {
        if (isShown()) {
            disableAnimation();
            setProgress(0);
            enableAnimation();
            setProgress(100);
        }
    }
}
