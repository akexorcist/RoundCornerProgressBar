package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * Thank you to @redsanso for add this feature. See @see <a href="https://github.com/akexorcist/Android-RoundCornerProgressBar/issues/42">Centered Progress Bar</a>
 **/
@Keep
public class CenteredRoundCornerProgressBar extends RoundCornerProgressBar {
    public CenteredRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenteredRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        super.drawProgress(layoutProgress, max, progress, totalWidth, radius, padding, colorProgress, isReverse);
        MarginLayoutParams params = (MarginLayoutParams) layoutProgress.getLayoutParams();

        float ratio = max / progress;
        float progressWidth = (totalWidth - (padding * 2)) / ratio;
        float deltaWidth = totalWidth - progressWidth;

        params.setMargins((int) (deltaWidth / 2), params.topMargin, (int) (deltaWidth / 2), params.bottomMargin);
        layoutProgress.setLayoutParams(params);
    }
}
