package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Thank you to @redsanso for add this feature. See @see <a href="https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/42">Centered Progress Bar</a>
 **/
@SuppressWarnings("unused")
@Keep
public class CenteredRoundCornerProgressBar extends RoundCornerProgressBar {

    public CenteredRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenteredRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public CenteredRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        super.drawProgress(layoutProgress, progressDrawable, max, progress, totalWidth, radius, padding, isReverse);
        MarginLayoutParams params = (MarginLayoutParams) layoutProgress.getLayoutParams();
        float ratio = max / progress;
        float progressWidth = (totalWidth - (padding * 2)) / ratio;
        float deltaWidth = totalWidth - progressWidth;
        params.setMargins((int) (deltaWidth / 2), params.topMargin, (int) (deltaWidth / 2), params.bottomMargin);
        layoutProgress.setLayoutParams(params);
    }
}
