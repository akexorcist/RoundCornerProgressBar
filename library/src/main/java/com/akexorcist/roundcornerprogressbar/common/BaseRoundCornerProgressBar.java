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

package com.akexorcist.roundcornerprogressbar.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import com.akexorcist.roundcornerprogressbar.R;

/**
 * Created by Akexorcist on 9/14/15 AD.
 */
@SuppressWarnings("unused")
@Keep
public abstract class BaseRoundCornerProgressBar extends LinearLayout {
    protected final static int DEFAULT_MAX_PROGRESS = 100;
    protected final static int DEFAULT_PROGRESS = 0;
    protected final static int DEFAULT_SECONDARY_PROGRESS = 0;
    protected final static int DEFAULT_PROGRESS_RADIUS = 30;
    protected final static int DEFAULT_BACKGROUND_PADDING = 0;

    private LinearLayout layoutBackground;
    private LinearLayout layoutProgress;
    private LinearLayout layoutSecondaryProgress;

    private GradientDrawable progressDrawable;
    private GradientDrawable secondaryProgressDrawable;

    private int radius;
    private int padding;
    private int totalWidth;

    private float max;
    private float progress;
    private float secondaryProgress;

    private int colorBackground;
    private int colorProgress;
    private int[] colorProgressArray;
    private int colorSecondaryProgress;

    private boolean isReverse;

    private OnProgressChangedListener progressChangedListener;

    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    // Setup a progress bar layout by sub class
    protected abstract int initLayout();

    // Setup an attribute parameter on sub class
    protected abstract void initStyleable(@NonNull Context context, AttributeSet attrs);

    // Initial any view on sub class
    protected abstract void initView();

    // Draw a progress by sub class
    protected abstract void drawProgress(@NonNull LinearLayout layoutProgress,
                                         @NonNull GradientDrawable progressDrawable,
                                         float max,
                                         float progress,
                                         float totalWidth,
                                         int radius,
                                         int padding,
                                         boolean isReverse);

    // Draw all view on sub class
    protected abstract void onViewDraw();

    public void setup(Context context, AttributeSet attrs) {
        setupStyleable(context, attrs);

        removeAllViews();
        // Setup layout for sub class
        LayoutInflater.from(context).inflate(initLayout(), this);
        // Initial default view
        layoutBackground = findViewById(R.id.layout_background);
        layoutProgress = findViewById(R.id.layout_progress);
        layoutSecondaryProgress = findViewById(R.id.layout_secondary_progress);

        initView();
    }

    // Retrieve initial parameter from view attribute
    public void setupStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRoundCornerProgressBar);

        radius = (int) typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS));
        padding = (int) typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));

        isReverse = typedArray.getBoolean(R.styleable.BaseRoundCornerProgressBar_rcReverse, false);

        max = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcProgress, DEFAULT_PROGRESS);
        secondaryProgress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS);

        int colorBackgroundDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorBackground = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcBackgroundColor, colorBackgroundDefault);
        colorProgress = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcProgressColor, -1);
        int colorProgressId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcProgressColors, 0);
        if (colorProgressId != 0) {
            colorProgressArray = getResources().getIntArray(colorProgressId);
        } else {
            colorProgressArray = null;
        }
        int colorSecondaryProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default);
        colorSecondaryProgress = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColor, colorSecondaryProgressDefault);
        typedArray.recycle();

        updateProgressDrawable();
        updateSecondaryProgressDrawable();

        initStyleable(context, attrs);
    }

    // Progress bar always refresh when view size has changed
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        totalWidth = newWidth;
        drawBackgroundProgress();
        drawPadding();
        // Can't instantly change the size of child views (primary & secondary progress)
        // when `onSizeChanged(...)` called. Using `post` method then
        // call these methods inside the Runnable will solved this.
        // And I can't reuse the `drawAll()` method because this problem.
        post(new Runnable() {
            @Override
            public void run() {
                drawPrimaryProgress();
                drawSecondaryProgress();
            }
        });
        onViewDraw();
    }

    // Redraw all view
    protected void drawAll() {
        drawBackgroundProgress();
        drawPadding();
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
        onViewDraw();
    }

    // Draw progress background
    private void drawBackgroundProgress() {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorBackground);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutBackground.setBackground(backgroundDrawable);
        } else {
            layoutBackground.setBackgroundDrawable(backgroundDrawable);
        }
    }

    // Create an color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(@ColorInt int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    // Create an empty color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(int[] colors) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setOrientation(!isReverse() ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.RIGHT_LEFT);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }

    private void updateProgressDrawable() {
        if (colorProgress != -1) {
            progressDrawable = createGradientDrawable(colorProgress);
        } else if (colorProgressArray != null && colorProgressArray.length > 0) {
            progressDrawable = createGradientDrawable(colorProgressArray);
        } else {
            int colorProgressDefault = getResources().getColor(R.color.round_corner_progress_bar_progress_default);
            progressDrawable = createGradientDrawable(colorProgressDefault);
        }
    }

    private void updateSecondaryProgressDrawable() {
        secondaryProgressDrawable = createGradientDrawable(colorSecondaryProgress);
    }

    private void drawPrimaryProgress() {
        int possibleRadius = Math.min(radius, layoutBackground.getMeasuredHeight() / 2);
        drawProgress(layoutProgress, progressDrawable, max, progress, totalWidth, possibleRadius, padding, isReverse);
    }

    private void drawSecondaryProgress() {
        int possibleRadius = Math.min(radius, layoutBackground.getMeasuredHeight() / 2);
        drawProgress(layoutSecondaryProgress, secondaryProgressDrawable, max, secondaryProgress, totalWidth, possibleRadius, padding, isReverse);
    }

    private void drawProgressReverse() {
        setupReverse(layoutProgress);
        setupReverse(layoutSecondaryProgress);
    }

    // Change progress position by depending on isReverse flag
    private void setupReverse(@NonNull LinearLayout layoutProgress) {
        RelativeLayout.LayoutParams progressParams = (RelativeLayout.LayoutParams) layoutProgress.getLayoutParams();
        removeLayoutParamsRule(progressParams);
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        layoutProgress.setLayoutParams(progressParams);
    }

    private void drawPadding() {
        layoutBackground.setPadding(padding, padding, padding, padding);
    }

    // Remove all of relative align rule
    private void removeLayoutParamsRule(@NonNull RelativeLayout.LayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        }
    }

    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        drawProgressReverse();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(@Px int radius) {
        if (radius >= 0) {
            this.radius = radius;
        }
        drawBackgroundProgress();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(@Px int padding) {
        if (padding >= 0) {
            this.padding = padding;
        }
        drawPadding();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max >= 0) {
            this.max = max;
        }
        if (this.progress > max) {
            this.progress = max;
        }
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getLayoutWidth() {
        return totalWidth;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress < 0) {
            this.progress = 0;
        } else {
            this.progress = Math.min(progress, max);
        }
        drawPrimaryProgress();
        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(getId(), this.progress, true, false);
        }
    }

    public float getSecondaryProgressWidth() {
        if (layoutSecondaryProgress != null) {
            return layoutSecondaryProgress.getWidth();
        }
        return 0;
    }

    public float getSecondaryProgress() {
        return secondaryProgress;
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress < 0) {
            this.secondaryProgress = 0;
        } else {
            this.secondaryProgress = Math.min(secondaryProgress, max);
        }
        drawSecondaryProgress();
        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(getId(), this.secondaryProgress, false, true);
        }
    }

    public int getProgressBackgroundColor() {
        return colorBackground;
    }

    public void setProgressBackgroundColor(@ColorInt int colorBackground) {
        this.colorBackground = colorBackground;
        drawBackgroundProgress();
    }

    public int getProgressColor() {
        return colorProgress;
    }

    public void setProgressColor(@ColorInt int progressColor) {
        this.colorProgress = progressColor;
        this.colorProgressArray = null;
        updateProgressDrawable();
        drawPrimaryProgress();
    }

    public int[] getProgressColors() {
        return colorProgressArray;
    }

    public void setProgressColor(int[] progressColors) {
        this.colorProgress = -1;
        this.colorProgressArray = progressColors;
        updateProgressDrawable();
        drawPrimaryProgress();
    }

    public int getSecondaryProgressColor() {
        return colorSecondaryProgress;
    }

    public void setSecondaryProgressColor(@ColorInt int colorSecondaryProgress) {
        this.colorSecondaryProgress = colorSecondaryProgress;
        updateSecondaryProgressDrawable();
        drawSecondaryProgress();
    }

    public void setOnProgressChangedListener(@Nullable OnProgressChangedListener listener) {
        progressChangedListener = listener;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        drawAll();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.max = this.max;
        ss.progress = this.progress;
        ss.secondaryProgress = this.secondaryProgress;

        ss.radius = this.radius;
        ss.padding = this.padding;

        ss.colorBackground = this.colorBackground;
        ss.colorProgress = this.colorProgress;
        ss.colorSecondaryProgress = this.colorSecondaryProgress;
        ss.colorProgressArray = this.colorProgressArray;

        ss.isReverse = this.isReverse;
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

        this.max = ss.max;
        this.progress = ss.progress;
        this.secondaryProgress = ss.secondaryProgress;

        this.radius = ss.radius;
        this.padding = ss.padding;

        this.colorBackground = ss.colorBackground;
        this.colorProgress = ss.colorProgress;
        this.colorSecondaryProgress = ss.colorSecondaryProgress;

        this.isReverse = ss.isReverse;

        progressDrawable = createGradientDrawable(colorProgress);
        secondaryProgressDrawable = createGradientDrawable(colorSecondaryProgress);
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress);
    }

    private static class SavedState extends BaseSavedState {
        float max;
        float progress;
        float secondaryProgress;

        int radius;
        int padding;

        int colorBackground;
        int colorProgress;
        int colorSecondaryProgress;
        int[] colorProgressArray;

        boolean isReverse;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.progress = in.readFloat();
            this.secondaryProgress = in.readFloat();

            this.radius = in.readInt();
            this.padding = in.readInt();

            this.colorBackground = in.readInt();
            this.colorProgress = in.readInt();
            this.colorSecondaryProgress = in.readInt();
            this.colorProgressArray = in.createIntArray();

            this.isReverse = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.progress);
            out.writeFloat(this.secondaryProgress);

            out.writeInt(this.radius);
            out.writeInt(this.padding);

            out.writeInt(this.colorBackground);
            out.writeInt(this.colorProgress);
            out.writeInt(this.colorSecondaryProgress);
            out.writeIntArray(this.colorProgressArray);

            out.writeByte((byte) (this.isReverse ? 1 : 0));
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
