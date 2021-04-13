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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.customview.view.AbsSavedState;

import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Akexorcist on 9/16/15 AD.
 */
@SuppressWarnings("unused")
@Keep
public class TextRoundCornerProgressBar extends AnimatedRoundCornerProgressBar implements ViewTreeObserver.OnGlobalLayoutListener {
    protected final static int DEFAULT_TEXT_SIZE = 16;
    protected final static int DEFAULT_TEXT_MARGIN = 10;
    public final static int GRAVITY_START = 0;
    public final static int GRAVITY_END = 1;
    public final static int PRIORITY_INSIDE = 0;
    public final static int PRIORITY_OUTSIDE = 1;

    @SuppressWarnings("WeakerAccess")
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRAVITY_START, GRAVITY_END})
    public @interface TEXT_PROGRESS_GRAVITY {
    }

    @SuppressWarnings("WeakerAccess")
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PRIORITY_INSIDE, PRIORITY_OUTSIDE})
    public @interface TEXT_POSITION_PRIORITY {
    }

    private TextView tvProgress;
    private int colorTextProgress;
    private int textProgressSize;
    private int textProgressMargin;
    private String textProgress;
    private int textInsideGravity;
    private int textOutsideGravity;
    private int textPositionPriority;

    public TextRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_text_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextRoundCornerProgressBar);

        colorTextProgress = typedArray.getColor(R.styleable.TextRoundCornerProgressBar_rcTextProgressColor, Color.WHITE);

        textProgressSize = (int) typedArray.getDimension(R.styleable.TextRoundCornerProgressBar_rcTextProgressSize, dp2px(DEFAULT_TEXT_SIZE));
        textProgressMargin = (int) typedArray.getDimension(R.styleable.TextRoundCornerProgressBar_rcTextProgressMargin, dp2px(DEFAULT_TEXT_MARGIN));

        textProgress = typedArray.getString(R.styleable.TextRoundCornerProgressBar_rcTextProgress);

        textInsideGravity = typedArray.getInt(R.styleable.TextRoundCornerProgressBar_rcTextInsideGravity, GRAVITY_START);
        textOutsideGravity = typedArray.getInt(R.styleable.TextRoundCornerProgressBar_rcTextOutsideGravity, GRAVITY_START);
        textPositionPriority = typedArray.getInt(R.styleable.TextRoundCornerProgressBar_rcTextPositionPriority, PRIORITY_INSIDE);

        typedArray.recycle();
    }

    @Override
    protected void initView() {
        tvProgress = findViewById(R.id.tv_progress);
        tvProgress.getViewTreeObserver().addOnGlobalLayoutListener(this);
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
        if (padding + (progressWidth / 2) < radius) {
            int margin = Math.max(radius - padding, 0) - (progressWidth / 2);
            progressParams.topMargin = margin;
            progressParams.bottomMargin = margin;
        } else {
            progressParams.topMargin = 0;
            progressParams.bottomMargin = 0;
        }
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }

    @Override
    protected void onViewDraw() {
        drawTextProgress();
        drawTextProgressSize();
        drawTextProgressMargin();
        // Can't instantly change the text position of child view
        // when `onSizeChanged(...)` called. Using `post` method then
        // call these methods inside the Runnable will solved this.
        post(new Runnable() {
            @Override
            public void run() {
                drawTextProgressPosition();
            }
        });
        drawTextProgressColor();
    }

    private void drawTextProgress() {
        tvProgress.setText(textProgress);
    }

    private void drawTextProgressColor() {
        tvProgress.setTextColor(colorTextProgress);
    }

    private void drawTextProgressSize() {
        tvProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX, textProgressSize);
    }

    private void drawTextProgressMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tvProgress.getLayoutParams();
        params.setMargins(textProgressMargin, 0, textProgressMargin, 0);
        tvProgress.setLayoutParams(params);
    }

    private void drawTextProgressPosition() {
        clearTextProgressAlign();
        int textProgressWidth = tvProgress.getMeasuredWidth() + (getTextProgressMargin() * 2);
        float ratio = getMax() / getProgress();
        int progressWidth = (int) ((getLayoutWidth() - (getPadding() * 2)) / ratio);
        if (textPositionPriority == PRIORITY_OUTSIDE) {
            if (getLayoutWidth() - progressWidth > textProgressWidth) {
                alignTextProgressOutsideProgress();
            } else {
                alignTextProgressInsideProgress();
            }
        } else {
            if (textProgressWidth + textProgressMargin > progressWidth) {
                alignTextProgressOutsideProgress();
            } else {
                alignTextProgressInsideProgress();
            }
        }
    }

    private void clearTextProgressAlign() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
        params.removeRule(RelativeLayout.LEFT_OF);
        params.removeRule(RelativeLayout.RIGHT_OF);
        params.removeRule(RelativeLayout.ALIGN_LEFT);
        params.removeRule(RelativeLayout.ALIGN_RIGHT);
        params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.removeRule(RelativeLayout.START_OF);
            params.removeRule(RelativeLayout.END_OF);
            params.removeRule(RelativeLayout.ALIGN_START);
            params.removeRule(RelativeLayout.ALIGN_END);
            params.removeRule(RelativeLayout.ALIGN_PARENT_START);
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
        }
        tvProgress.setLayoutParams(params);
    }

    private void alignTextProgressInsideProgress() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
        if (isReverse()) {
            if (textInsideGravity == GRAVITY_END) {
                params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_END, R.id.layout_progress);
                }
            } else {
                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_START, R.id.layout_progress);
                }
            }
        } else {
            if (textInsideGravity == GRAVITY_END) {
                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_START, R.id.layout_progress);
                }
            } else {
                params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_END, R.id.layout_progress);
                }
            }
        }
        tvProgress.setLayoutParams(params);
    }

    private void alignTextProgressOutsideProgress() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
        if (isReverse()) {
            if (textOutsideGravity == GRAVITY_END) {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                }
            } else {
                params.addRule(RelativeLayout.LEFT_OF, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.START_OF, R.id.layout_progress);
                }
            }
        } else {
            if (textOutsideGravity == GRAVITY_END) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                }
            } else {
                params.addRule(RelativeLayout.RIGHT_OF, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.END_OF, R.id.layout_progress);
                }
            }
        }
        tvProgress.setLayoutParams(params);
    }

    public String getProgressText() {
        return textProgress;
    }

    public void setProgressText(String text) {
        textProgress = text;
        drawTextProgress();
        drawTextProgressPosition();
    }

    @Override
    public void setProgress(int progress) {
        setProgress((float) progress);
    }

    @Override
    public void setProgress(float progress) {
        super.setProgress(progress);
        drawTextProgressPosition();
    }

    public int getTextProgressColor() {
        return colorTextProgress;
    }

    public void setTextProgressColor(@ColorInt int color) {
        this.colorTextProgress = color;
        drawTextProgressColor();
    }

    public int getTextProgressSize() {
        return textProgressSize;
    }

    public void setTextProgressSize(@Px int size) {
        this.textProgressSize = size;
        drawTextProgressSize();
        drawTextProgressPosition();
    }

    public int getTextProgressMargin() {
        return textProgressMargin;
    }

    public void setTextProgressMargin(@Px int margin) {
        this.textProgressMargin = margin;
        drawTextProgressMargin();
        drawTextProgressPosition();
    }

    @TEXT_POSITION_PRIORITY
    public int getTextPositionPriority() {
        return textPositionPriority;
    }

    public void setTextPositionPriority(@TEXT_POSITION_PRIORITY int priority) {
        this.textPositionPriority = priority;
        drawTextProgressPosition();
    }

    @TEXT_PROGRESS_GRAVITY
    public int getTextInsideGravity() {
        return textInsideGravity;
    }

    public void setTextInsideGravity(@TEXT_PROGRESS_GRAVITY int gravity) {
        this.textInsideGravity = gravity;
        drawTextProgressPosition();
    }

    @TEXT_PROGRESS_GRAVITY
    public int getTextOutsideGravity() {
        return textOutsideGravity;
    }

    public void setTextOutsideGravity(@TEXT_PROGRESS_GRAVITY int gravity) {
        this.textOutsideGravity = gravity;
        drawTextProgressPosition();
    }

    @Override
    public void onGlobalLayout() {
        tvProgress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        drawTextProgressPosition();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.colorTextProgress = this.colorTextProgress;
        ss.textProgressSize = this.textProgressSize;
        ss.textProgressMargin = this.textProgressMargin;

        ss.textProgress = this.textProgress;

        ss.textInsideGravity = this.textInsideGravity;
        ss.textOutsideGravity = this.textOutsideGravity;
        ss.textPositionPriority = this.textPositionPriority;
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

        this.colorTextProgress = ss.colorTextProgress;
        this.textProgressSize = ss.textProgressSize;
        this.textProgressMargin = ss.textProgressMargin;

        this.textProgress = ss.textProgress;

        this.textInsideGravity = ss.textInsideGravity;
        this.textOutsideGravity = ss.textOutsideGravity;
        this.textPositionPriority = ss.textPositionPriority;
    }

    protected static class SavedState extends AbsSavedState {
        int colorTextProgress;
        int textProgressSize;
        int textProgressMargin;

        String textProgress;

        int textInsideGravity;
        int textOutsideGravity;
        int textPositionPriority;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            this(in, null);
        }

        protected SavedState(@NonNull Parcel in, @Nullable ClassLoader loader) {
            super(in, loader);

            this.colorTextProgress = in.readInt();
            this.textProgressSize = in.readInt();
            this.textProgressMargin = in.readInt();

            this.textProgress = in.readString();

            this.textInsideGravity = in.readInt();
            this.textOutsideGravity = in.readInt();
            this.textPositionPriority = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(this.colorTextProgress);
            out.writeInt(this.textProgressSize);
            out.writeInt(this.textProgressMargin);

            out.writeString(this.textProgress);

            out.writeInt(this.textInsideGravity);
            out.writeInt(this.textOutsideGravity);
            out.writeInt(this.textPositionPriority);
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
