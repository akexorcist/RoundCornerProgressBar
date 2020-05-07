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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.customview.view.AbsSavedState;

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

    protected LinearLayout layoutBackground;
    protected LinearLayout layoutProgress;
    protected LinearLayout layoutSecondaryProgress;

    protected GradientDrawable progressDrawable;
    protected GradientDrawable secondaryProgressDrawable;

    protected int radius;
    protected int padding;
    protected int totalWidth;

    protected float max;
    protected float progress;
    protected float secondaryProgress;

    protected int backgroundColor;
    private int progressColor;
    protected int secondaryProgressColor;
    protected int[] progressColors;
    protected int[] secondaryProgressColors;

    protected boolean isReverse;

    protected OnProgressChangedListener progressChangedListener;

    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context, attrs);
    }

    // Setup a progress bar layout by sub class
    protected abstract int initLayout();

    // Setup an attribute parameter on sub class
    protected abstract void initStyleable(@NonNull Context context, @NonNull AttributeSet attrs);

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

    public void setup(@NonNull Context context, @NonNull AttributeSet attrs) {
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
    public void setupStyleable(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRoundCornerProgressBar);

        radius = (int) typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS));
        padding = (int) typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));

        isReverse = typedArray.getBoolean(R.styleable.BaseRoundCornerProgressBar_rcReverse, false);

        max = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcProgress, DEFAULT_PROGRESS);
        secondaryProgress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS);

        int defaultBackgroundColor = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        backgroundColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcBackgroundColor, defaultBackgroundColor);

        progressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcProgressColor, -1);
        int progressColorResourceId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcProgressColors, 0);
        if (progressColorResourceId != 0) {
            progressColors = getResources().getIntArray(progressColorResourceId);
        } else {
            progressColors = null;
        }

        secondaryProgressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColor, -1);
        int secondaryProgressColorResourceId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColors, 0);
        if (secondaryProgressColorResourceId != 0) {
            secondaryProgressColors = getResources().getIntArray(secondaryProgressColorResourceId);
        } else {
            secondaryProgressColors = null;
        }
        typedArray.recycle();

        updateProgressDrawable();
        updateSecondaryProgressDrawable();
        initStyleable(context, attrs);
    }

    // Progress bar always refresh when view size has changed
    @Override
    protected void onSizeChanged(int newWidth,
                                 int newHeight,
                                 int oldWidth,
                                 int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        totalWidth = newWidth;
        drawBackgroundProgress();
        drawPadding();
        drawProgressReverse();
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
        GradientDrawable backgroundDrawable = createGradientDrawable(backgroundColor);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        layoutBackground.setBackground(backgroundDrawable);
    }

    // Create an color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(@ColorInt int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    // Create an multi-color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(int[] colors) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setOrientation(!isReverse() ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.RIGHT_LEFT);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }

    // Create gradient drawable depends on progressColor and progressColors value
    private void updateProgressDrawable() {
        if (progressColor != -1) {
            progressDrawable = createGradientDrawable(progressColor);
        } else if (progressColors != null && progressColors.length > 0) {
            progressDrawable = createGradientDrawable(progressColors);
        } else {
            int defaultColor = getResources().getColor(R.color.round_corner_progress_bar_progress_default);
            progressDrawable = createGradientDrawable(defaultColor);
        }
    }

    // Create gradient drawable depends on secondaryProgressColor and secondaryProgressColors value
    private void updateSecondaryProgressDrawable() {
        if (secondaryProgressColor != -1) {
            secondaryProgressDrawable = createGradientDrawable(secondaryProgressColor);
        } else if (secondaryProgressColors != null && secondaryProgressColors.length > 0) {
            secondaryProgressDrawable = createGradientDrawable(secondaryProgressColors);
        } else {
            int defaultColor = getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default);
            secondaryProgressDrawable = createGradientDrawable(defaultColor);
        }
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
        setupProgressReversing(layoutProgress, isReverse);
        setupProgressReversing(layoutSecondaryProgress, isReverse);
    }

    // Change progress position by depending on isReverse flag
    private void setupProgressReversing(@NonNull LinearLayout layoutProgress, boolean isReverse) {
        RelativeLayout.LayoutParams progressParams = (RelativeLayout.LayoutParams) layoutProgress.getLayoutParams();
        removeLayoutParamsRule(progressParams);
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            }
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            }
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

    public void setProgress(int progress) {
        setProgress((float) progress);
    }

    public void setProgress(float progress) {
        if (progress < 0) {
            this.progress = 0;
        } else {
            this.progress = Math.min(progress, max);
        }
        drawPrimaryProgress();
        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(this, this.progress, true, false);
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

    public void setSecondaryProgress(int progress) {
        setSecondaryProgress((float) progress);
    }

    public void setSecondaryProgress(float progress) {
        if (progress < 0) {
            this.secondaryProgress = 0;
        } else {
            this.secondaryProgress = Math.min(progress, max);
        }
        drawSecondaryProgress();
        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(this, this.secondaryProgress, false, true);
        }
    }

    public int getProgressBackgroundColor() {
        return backgroundColor;
    }

    public void setProgressBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        drawBackgroundProgress();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(@ColorInt int color) {
        this.progressColor = color;
        this.progressColors = null;
        updateProgressDrawable();
        drawPrimaryProgress();
    }

    @Nullable
    public int[] getProgressColors() {
        return progressColors;
    }

    public void setProgressColors(int[] colors) {
        this.progressColor = -1;
        this.progressColors = colors;
        updateProgressDrawable();
        drawPrimaryProgress();
    }

    public int getSecondaryProgressColor() {
        return secondaryProgressColor;
    }

    public void setSecondaryProgressColor(@ColorInt int color) {
        this.secondaryProgressColor = color;
        this.secondaryProgressColors = null;
        updateSecondaryProgressDrawable();
        drawSecondaryProgress();
    }

    @Nullable
    public int[] getSecondaryProgressColors() {
        return secondaryProgressColors;
    }

    public void setSecondaryProgressColors(int[] colors) {
        this.secondaryProgressColor = -1;
        this.secondaryProgressColors = colors;
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

        ss.colorBackground = this.backgroundColor;
        ss.colorProgress = this.progressColor;
        ss.colorSecondaryProgress = this.secondaryProgressColor;
        ss.colorProgressArray = this.progressColors;
        ss.colorSecondaryProgressArray = this.secondaryProgressColors;
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

        this.backgroundColor = ss.colorBackground;
        this.progressColor = ss.colorProgress;
        this.secondaryProgressColor = ss.colorSecondaryProgress;
        this.progressColors = ss.colorProgressArray;
        this.secondaryProgressColors = ss.colorSecondaryProgressArray;

        this.isReverse = ss.isReverse;

        updateProgressDrawable();
        updateSecondaryProgressDrawable();
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(View view, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress);
    }

    protected static class SavedState extends AbsSavedState {
        float max;
        float progress;
        float secondaryProgress;

        int radius;
        int padding;

        int colorBackground;
        int colorProgress;
        int colorSecondaryProgress;
        int[] colorProgressArray;
        int[] colorSecondaryProgressArray;

        boolean isReverse;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            this(in, null);
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.max = in.readFloat();
            this.progress = in.readFloat();
            this.secondaryProgress = in.readFloat();

            this.radius = in.readInt();
            this.padding = in.readInt();

            this.colorBackground = in.readInt();
            this.colorProgress = in.readInt();
            this.colorSecondaryProgress = in.readInt();
            this.colorProgressArray = new int[in.readInt()];
            in.readIntArray(this.colorProgressArray);
            this.colorSecondaryProgressArray = new int[in.readInt()];
            in.readIntArray(this.colorSecondaryProgressArray);

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
            out.writeInt(this.colorProgressArray != null ? this.colorProgressArray.length : 0);
            out.writeIntArray(this.colorProgressArray != null ? this.colorProgressArray : new int[0]);
            out.writeInt(this.colorSecondaryProgressArray != null ? this.colorSecondaryProgressArray.length : 0);
            out.writeIntArray(this.colorSecondaryProgressArray != null ? this.colorSecondaryProgressArray : new int[0]);

            out.writeByte((byte) (this.isReverse ? 1 : 0));
        }

        public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
