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
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.R;

/**
 * Created by Akexorcist on 9/14/15 AD.
 */
public abstract class BaseRoundCornerProgressBar extends LinearLayout {
    protected final static int DEFAULT_MAX_PROGRESS = 100;
    protected final static int DEFAULT_PROGRESS = 0;
    protected final static int DEFAULT_SECONDARY_PROGRESS = 0;
    protected final static int DEFAULT_PROGRESS_RADIUS = 30;
    protected final static int DEFAULT_BACKGROUND_PADDING = 0;

    private LinearLayout layoutBackground;
    private LinearLayout layoutProgress;
    private LinearLayout layoutSecondaryProgress;

    private int radius;
    private int padding;
    private int totalWidth;

    private float max;
    private float progress;
    private float secondaryProgress;

    private int colorBackground;
    private int colorProgress;
    private int colorSecondaryProgress;

    private boolean isReverse;

    private OnProgressChangedListener progressChangedListener;

    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }
    }

    private void previewLayout(Context context) {
        setGravity(Gravity.CENTER);
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(getClass().getSimpleName());
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.GRAY);
        addView(tv);
    }

    // Setup a progress bar layout by sub class
    protected abstract int initLayout();

    // Setup an attribute parameter on sub class
    protected abstract void initStyleable(Context context, AttributeSet attrs);

    // Initial any view on sub class
    protected abstract void initView();

    // Draw a progress by sub class
    protected abstract void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth,
                                         int radius, int padding, int colorProgress, boolean isReverse);

    // Draw all view on sub class
    protected abstract void onViewDraw();

    public void setup(Context context, AttributeSet attrs) {
        setupStyleable(context, attrs);

        removeAllViews();
        // Setup layout for sub class
        LayoutInflater.from(context).inflate(initLayout(), this);
        // Initial default view
        layoutBackground = (LinearLayout) findViewById(R.id.layout_background);
        layoutProgress = (LinearLayout) findViewById(R.id.layout_progress);
        layoutSecondaryProgress = (LinearLayout) findViewById(R.id.layout_secondary_progress);

        initView();
    }

    // Retrieve initial parameter from view attribute
    public void setupStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerProgress);

        radius = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS));
        padding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));

        isReverse = typedArray.getBoolean(R.styleable.RoundCornerProgress_rcReverse, false);

        isUserSeekable = typedArray.getBoolean(R.styleable.RoundCornerProgress_rcSeekAllowed, false);

        max = typedArray.getFloat(R.styleable.RoundCornerProgress_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.RoundCornerProgress_rcProgress, DEFAULT_PROGRESS);
        secondaryProgress = typedArray.getFloat(R.styleable.RoundCornerProgress_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS);

        int colorBackgroundDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorBackground = typedArray.getColor(R.styleable.RoundCornerProgress_rcBackgroundColor, colorBackgroundDefault);
        int colorProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_progress_default);
        colorProgress = typedArray.getColor(R.styleable.RoundCornerProgress_rcProgressColor, colorProgressDefault);
        int colorSecondaryProgressDefault = context.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default);
        colorSecondaryProgress = typedArray.getColor(R.styleable.RoundCornerProgress_rcSecondaryProgressColor, colorSecondaryProgressDefault);
        typedArray.recycle();

        initStyleable(context, attrs);
    }

    // Progress bar always refresh when view size has changed
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        if(!isInEditMode()) {
            totalWidth = newWidth;
            drawAll();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawPrimaryProgress();
                    drawSecondaryProgress();
                }
            }, 5);
        }
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
    @SuppressWarnings("deprecation")
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

    // Create an empty color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    private void drawPrimaryProgress() {
        drawProgress(layoutProgress, max, progress, totalWidth, radius, padding, colorProgress, isReverse);
    }

    private void drawSecondaryProgress() {
        drawProgress(layoutSecondaryProgress, max, secondaryProgress, totalWidth, radius, padding, colorSecondaryProgress, isReverse);
    }

    private void drawProgressReverse() {
        setupReverse(layoutProgress);
        setupReverse(layoutSecondaryProgress);
    }

    // Change progress position by depending on isReverse flag
    private void setupReverse(LinearLayout layoutProgress) {
        RelativeLayout.LayoutParams progressParams = (RelativeLayout.LayoutParams) layoutProgress.getLayoutParams();
        removeLayoutParamsRule(progressParams);
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        layoutProgress.setLayoutParams(progressParams);
    }

    private void drawPadding() {
        layoutBackground.setPadding(padding, padding, padding, padding);
    }

    // Remove all of relative align rule
    private void removeLayoutParamsRule(RelativeLayout.LayoutParams layoutParams) {
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
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
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

    public void setRadius(int radius) {
        if (radius >= 0)
            this.radius = radius;
        drawBackgroundProgress();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        if (padding >= 0)
            this.padding = padding;
        drawPadding();
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max >= 0)
            this.max = max;
        if (this.progress > max)
            this.progress = max;
        drawPrimaryProgress();
        drawSecondaryProgress();
    }

    public float getLayoutWidth() {
        return totalWidth;
    }

    public float getProgress() {
        return progress;
    }

    public boolean isUserSeekable()
    {
        return isUserSeekable;
    }

    public void setIsUserSeekable( boolean isUserSeekable )
    {
        this.isUserSeekable = isUserSeekable;
    }

    public void setProgress( float progress )
    {
        setProgress(progress, false);
    }

    public void setProgress( float progress, boolean fromUser )
    {
        if( progress < 0 )
        { this.progress = 0; }
        else if( progress > max )
        { this.progress = max; }
        else
        { this.progress = progress; }
        drawPrimaryProgress();
        if( progressChangedListener != null )
        { progressChangedListener.onProgressChanged(getId(), this.progress, true, false); }

        if( mOnSeekBarChangeListener != null )
        { mOnSeekBarChangeListener.onProgressChanged(this, this.progress, fromUser); }
    }

    public float getSecondaryProgressWidth() {
        if (layoutSecondaryProgress != null)
            return layoutSecondaryProgress.getWidth();
        return 0;
    }

    public float getSecondaryProgress() {
        return secondaryProgress;
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress < 0)
            this.secondaryProgress = 0;
        else if (secondaryProgress > max)
            this.secondaryProgress = max;
        else
            this.secondaryProgress = secondaryProgress;
        drawSecondaryProgress();
        if(progressChangedListener != null)
            progressChangedListener.onProgressChanged(getId(), this.secondaryProgress, false, true);
    }

    public int getProgressBackgroundColor() {
        return colorBackground;
    }

    public void setProgressBackgroundColor(int colorBackground) {
        this.colorBackground = colorBackground;
        drawBackgroundProgress();
    }

    public int getProgressColor() {
        return colorProgress;
    }

    public void setProgressColor(int colorProgress) {
        this.colorProgress = colorProgress;
        drawPrimaryProgress();
    }

    public int getSecondaryProgressColor() {
        return colorSecondaryProgress;
    }

    public void setSecondaryProgressColor(int colorSecondaryProgress) {
        this.colorSecondaryProgress = colorSecondaryProgress;
        drawSecondaryProgress();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
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

        ss.radius = this.radius;
        ss.padding = this.padding;

        ss.colorBackground = this.colorBackground;
        ss.colorProgress = this.colorProgress;
        ss.colorSecondaryProgress = this.colorSecondaryProgress;

        ss.max = this.max;
        ss.progress = this.progress;
        ss.secondaryProgress = this.secondaryProgress;

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

        this.radius = ss.radius;
        this.padding = ss.padding;

        this.colorBackground = ss.colorBackground;
        this.colorProgress = ss.colorProgress;
        this.colorSecondaryProgress = ss.colorSecondaryProgress;

        this.max = ss.max;
        this.progress = ss.progress;
        this.secondaryProgress = ss.secondaryProgress;

        this.isReverse = ss.isReverse;
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

        boolean isReverse;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.progress = in.readFloat();
            this.secondaryProgress = in.readFloat();

            this.radius = in.readInt();
            this.padding = in.readInt();

            this.colorBackground = in.readInt();
            this.colorProgress = in.readInt();
            this.colorSecondaryProgress = in.readInt();

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

            out.writeByte((byte) ( this.isReverse ? 1 : 0 ));
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
        {
            public SavedState createFromParcel( Parcel in )
            {
                return new SavedState(in);
            }

            public SavedState[] newArray( int size )
            {
                return new SavedState[size];
            }
        };
    }

    public interface OnProgressChangedListener
    {
        public void onProgressChanged( int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress );
    }


    /**********************
     * TOUCH FUNCTIONALITY
     **********************/

    // Distance in pixels a touch can wander before we think the user is scrolling
    protected int scaledTouchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();

    // Whether the progress bar is seekable.
    protected boolean isUserSeekable;

    /**
     * On touch, this offset plus the scaled value from the position of the
     * touch will form the progress value. Usually 0.
     */
    protected float touchProgressOffset;

    protected boolean isDragging;

    protected OnSeekBarChangeListener mOnSeekBarChangeListener;


    /**
     * Sets a listener to receive notifications of changes to the SeekBar's progress level. Also
     * provides notifications of when the user starts and stops a touch gesture within the SeekBar.
     *
     * @param listener The seek bar notification listener
     */
    public void setOnSeekBarChangeListener( OnSeekBarChangeListener listener )
    {
        mOnSeekBarChangeListener = listener;
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch()
    {
        isDragging = true;

        if( mOnSeekBarChangeListener != null )
        {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    /**
     * This is called when the user either releases his touch or the touch is canceled.
     */
    void onStopTrackingTouch()
    {
        isDragging = false;

        if( mOnSeekBarChangeListener != null )
        {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    @Override
    public boolean onTouchEvent( MotionEvent event )
    {
        if( !isUserSeekable || !isEnabled() )
        {
            return false;
        }

        switch( event.getAction() )
        {
            case MotionEvent.ACTION_DOWN: // Finger pressed "DOWN" on the screen
            {
                setPressed(true);
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();

                break;
            }
            case MotionEvent.ACTION_MOVE: // Finger "MOVES" on the screen
            {
                if( isDragging )
                {
                    trackTouchEvent(event);
                }
                else
                {
                    final float x = event.getX();
                    if( Math.abs(x) > scaledTouchSlop )
                    {
                        setPressed(true);
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: // Lifts finger "UP" from the screen
            {
                if( isDragging )
                {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                }
                else
                {
                    // Touch up when we never crossed the touch slop threshold should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }

                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL: // Actions have been cancelled.
            {
                if( isDragging )
                {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
            }
        }
        return true;
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     * This is specifically used to stop the touch events from spilling over to
     * any other views, like scrollable views.
     */
    private void attemptClaimDrag()
    {
        this.requestDisallowInterceptTouchEvent(true);
    }

    private void trackTouchEvent( MotionEvent event )
    {
        final int width = getWidth();
        final int available = width - ( padding * 2 );
        final int x = (int) event.getX();
        float scale;
        float progress = 0;
        if( isReverse() )
        {
            if( x > width - padding )
            {
                scale = 0.0f;
            }
            else if( x < padding )
            {
                scale = 1.0f;
            }
            else
            {
                scale = (float) ( available - x + padding ) / (float) available;
                progress = touchProgressOffset;
            }
        }
        else
        {
            if( x < padding )
            {
                scale = 0.0f;
            }
            else if( x > width - padding )
            {
                scale = 1.0f;
            }
            else
            {
                scale = (float) ( x - padding ) / (float) available;
                progress = touchProgressOffset;
            }
        }
        final float max = getMax();
        progress += scale * max;

        setProgress(progress, true);
    }

    /**
     * A callback that notifies clients when the progress level has been
     * changed. This includes changes that were initiated by the user through a
     * touch gesture or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnSeekBarChangeListener
    {
        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param roundedSeekBar The BaseRoundCornerProgressBar whose progress has changed
         * @param progress       The current progress level. This will be in the range 0..max where max
         *                       was set by {@link BaseRoundCornerProgressBar#setMax(float)}. (The default value for max is 10000.)
         * @param fromUser       True if the progress change was initiated by the user.
         */
        void onProgressChanged( BaseRoundCornerProgressBar roundedSeekBar, float progress, boolean fromUser );

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the BaseRoundCornerProgressBar.
         *
         * @param roundedSeekBar The BaseRoundCornerProgressBar in which the touch gesture began
         */
        void onStartTrackingTouch( BaseRoundCornerProgressBar roundedSeekBar );

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the BaseRoundCornerProgressBar.
         *
         * @param roundedSeekBar The BaseRoundCornerProgressBar in which the touch gesture began
         */
        void onStopTrackingTouch( BaseRoundCornerProgressBar roundedSeekBar );
    }
}
