package com.akexorcist.roundcornerprogressbar.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.customview.view.AbsSavedState;

import com.akexorcist.roundcornerprogressbar.R;

@SuppressWarnings("unused")
public abstract class AnimatedRoundCornerProgressBar extends BaseRoundCornerProgressBar {
    public static final long DEFAULT_DURATION = 500L;

    private boolean isProgressAnimating = false;
    private boolean isSecondaryProgressAnimating = false;
    private float lastProgress;
    private float lastSecondaryProgress;

    private float animationSpeedScale;
    private boolean isAnimationEnabled;

    private ValueAnimator progressAnimator;
    private ValueAnimator secondaryProgressAnimator;

    public AnimatedRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimatedRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setupStyleable(@NonNull Context context, @NonNull AttributeSet attrs) {
        super.setupStyleable(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRoundCornerProgressBar);
        isAnimationEnabled = typedArray.getBoolean(R.styleable.AnimatedRoundCornerProgressBar_rcAnimationEnable, false);
        animationSpeedScale = typedArray.getFloat(R.styleable.AnimatedRoundCornerProgressBar_rcAnimationSpeedScale, 1f);
        typedArray.recycle();
        lastProgress = super.getProgress();
        lastSecondaryProgress = super.getSecondaryProgress();
    }

    @Override
    public float getProgress() {
        if (!isAnimationEnabled && !isProgressAnimating) {
            return super.getProgress();
        } else {
            return lastProgress;
        }
    }

    @Override
    public void setProgress(int progress) {
        setProgress((float) progress);
    }

    @Override
    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        } else {
            progress = Math.min(progress, max);
        }
        clearProgressAnimation();
        this.lastProgress = progress;
        if (isAnimationEnabled) {
            startProgressAnimation(super.getProgress(), progress);
        } else {
            super.setProgress(progress);
        }
    }

    @Override
    public float getSecondaryProgress() {
        if (!isAnimationEnabled && !isSecondaryProgressAnimating) {
            return super.getSecondaryProgress();
        } else {
            return lastSecondaryProgress;
        }
    }

    @Override
    public void setSecondaryProgress(int progress) {
        setSecondaryProgress((float) progress);
    }

    @Override
    public void setSecondaryProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        } else {
            progress = Math.min(progress, max);
        }
        clearSecondaryProgressAnimation();
        this.lastSecondaryProgress = progress;
        if (isAnimationEnabled) {
            startSecondaryProgressAnimation(super.getSecondaryProgress(), progress);
        } else {
            super.setSecondaryProgress(progress);
        }
    }

    @FloatRange(from = 0.2f, to = 5f)
    public float getAnimationSpeedScale() {
        return animationSpeedScale;
    }

    public void enableAnimation() {
        this.isAnimationEnabled = true;
    }

    public void disableAnimation() {
        this.isAnimationEnabled = false;
    }

    public void setAnimationSpeedScale(@FloatRange(from = 0.2f, to = 5f) float scale) {
        scale = Math.min(scale, 5);
        scale = Math.max(scale, 0.2f);
        this.animationSpeedScale = scale;
    }

    public boolean isProgressAnimating() {
        return isProgressAnimating;
    }

    public boolean isSecondaryProgressAnimating() {
        return isSecondaryProgressAnimating;
    }

    protected void onProgressChangeAnimationUpdate(LinearLayout layout, float current, float to) {
    }

    protected void onProgressChangeAnimationEnd(LinearLayout layout) {
    }

    protected void stopProgressAnimationImmediately() {
        clearProgressAnimation();
        clearSecondaryProgressAnimation();
        if (isAnimationEnabled && isProgressAnimating) {
            disableAnimation();
            if (isProgressAnimating) {
                super.setProgress(lastProgress);
            }
            if (isSecondaryProgressAnimating) {
                super.setSecondaryProgress(lastProgress);
            }
            enableAnimation();
        }
    }

    private long getAnimationDuration(float from, float to, float max, float scale) {
        float diff = Math.abs(from - to);
        return (long) (diff * DEFAULT_DURATION / max * scale);
    }

    private void startProgressAnimation(float from, float to) {
        isProgressAnimating = true;
        if (progressAnimator != null) {
            progressAnimator.removeUpdateListener(progressAnimationUpdateListener);
            progressAnimator.removeListener(progressAnimationAdapterListener);
            progressAnimator.cancel();
            progressAnimator = null;
        }
        progressAnimator = ValueAnimator.ofFloat(from, to);
        progressAnimator.setDuration(getAnimationDuration(from, to, max, animationSpeedScale));
        progressAnimator.addUpdateListener(progressAnimationUpdateListener);
        progressAnimator.addListener(progressAnimationAdapterListener);
        progressAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener progressAnimationUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            onUpdateProgressByAnimation((float) animation.getAnimatedValue());
        }
    };

    private AnimatorListenerAdapter progressAnimationAdapterListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            isProgressAnimating = false;
            onProgressAnimationEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isProgressAnimating = false;
        }
    };

    private void onUpdateProgressByAnimation(float progress) {
        super.setProgress(progress);
        onProgressChangeAnimationUpdate(layoutProgress, progress, lastProgress);
    }

    private void onProgressAnimationEnd() {
        onProgressChangeAnimationEnd(layoutProgress);
    }

    private void restoreProgressAnimationState() {
        if (isProgressAnimating) {
            startProgressAnimation(super.getProgress(), lastProgress);
        }
    }

    private void clearProgressAnimation() {
        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
        }
    }

    private void startSecondaryProgressAnimation(float from, float to) {
        isSecondaryProgressAnimating = true;
        if (secondaryProgressAnimator != null) {
            secondaryProgressAnimator.removeUpdateListener(secondaryProgressAnimationUpdateListener);
            secondaryProgressAnimator.removeListener(secondaryProgressAnimationAdapterListener);
            secondaryProgressAnimator.cancel();
            secondaryProgressAnimator = null;
        }
        secondaryProgressAnimator = ValueAnimator.ofFloat(from, to);
        secondaryProgressAnimator.setDuration(getAnimationDuration(from, to, max, animationSpeedScale));
        secondaryProgressAnimator.addUpdateListener(secondaryProgressAnimationUpdateListener);
        secondaryProgressAnimator.addListener(secondaryProgressAnimationAdapterListener);
        secondaryProgressAnimator.start();
    }

    private ValueAnimator.AnimatorUpdateListener secondaryProgressAnimationUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            onUpdateSecondaryProgressByAnimation((float) animation.getAnimatedValue());
        }
    };

    private AnimatorListenerAdapter secondaryProgressAnimationAdapterListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            isSecondaryProgressAnimating = false;
            onSecondaryProgressAnimationEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isSecondaryProgressAnimating = false;
        }
    };

    private void onUpdateSecondaryProgressByAnimation(float progress) {
        super.setSecondaryProgress(progress);
        onProgressChangeAnimationUpdate(layoutSecondaryProgress, progress, lastProgress);
    }

    private void onSecondaryProgressAnimationEnd() {
        onProgressChangeAnimationEnd(layoutSecondaryProgress);
    }

    private void restoreSecondaryProgressAnimationState() {
        if (isSecondaryProgressAnimating) {
            startSecondaryProgressAnimation(super.getSecondaryProgress(), lastSecondaryProgress);
        }
    }

    private void clearSecondaryProgressAnimation() {
        if (secondaryProgressAnimator != null && secondaryProgressAnimator.isRunning()) {
            secondaryProgressAnimator.cancel();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.isProgressAnimating = this.isProgressAnimating;
        ss.isSecondaryProgressAnimating = this.isSecondaryProgressAnimating;
        ss.lastProgress = this.lastProgress;
        ss.lastSecondaryProgress = this.lastSecondaryProgress;
        ss.animationSpeedScale = this.animationSpeedScale;
        ss.isAnimationEnabled = this.isAnimationEnabled;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.isProgressAnimating = ss.isProgressAnimating;
        this.isSecondaryProgressAnimating = ss.isSecondaryProgressAnimating;
        this.lastProgress = ss.lastProgress;
        this.lastSecondaryProgress = ss.lastSecondaryProgress;
        this.animationSpeedScale = ss.animationSpeedScale;
        this.isAnimationEnabled = ss.isAnimationEnabled;
        restoreProgressAnimationState();
        restoreSecondaryProgressAnimationState();
    }

    protected static class SavedState extends AbsSavedState {
        boolean isProgressAnimating;
        boolean isSecondaryProgressAnimating;
        float lastProgress;
        float lastSecondaryProgress;
        float animationSpeedScale;
        boolean isAnimationEnabled;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            this(in, null);
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.isProgressAnimating = in.readByte() != 0;
            this.isSecondaryProgressAnimating = in.readByte() != 0;
            this.lastProgress = in.readFloat();
            this.lastSecondaryProgress = in.readFloat();
            this.animationSpeedScale = in.readFloat();
            this.isAnimationEnabled = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (this.isProgressAnimating ? 1 : 0));
            out.writeByte((byte) (this.isSecondaryProgressAnimating ? 1 : 0));
            out.writeFloat(this.lastProgress);
            out.writeFloat(this.lastSecondaryProgress);
            out.writeFloat(this.animationSpeedScale);
            out.writeByte((byte) (this.isAnimationEnabled ? 1 : 0));
        }

        public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
