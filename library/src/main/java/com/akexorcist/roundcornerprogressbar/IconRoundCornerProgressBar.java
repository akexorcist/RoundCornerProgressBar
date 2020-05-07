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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.customview.view.AbsSavedState;

import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar;


/**
 * Created by Akexorcist on 9/14/15 AD.
 */
@SuppressWarnings("unused")
@Keep
public class IconRoundCornerProgressBar extends AnimatedRoundCornerProgressBar {
    protected final static int DEFAULT_ICON_SIZE = 20;
    protected final static int DEFAULT_ICON_PADDING_LEFT = 0;
    protected final static int DEFAULT_ICON_PADDING_RIGHT = 0;
    protected final static int DEFAULT_ICON_PADDING_TOP = 0;
    protected final static int DEFAULT_ICON_PADDING_BOTTOM = 0;

    private ImageView ivProgressIcon;
    private int iconResource;
    private int iconSize;
    private int iconWidth;
    private int iconHeight;
    private int iconPadding;
    private int iconPaddingLeft;
    private int iconPaddingRight;
    private int iconPaddingTop;
    private int iconPaddingBottom;
    private int colorIconBackground;

    private Bitmap iconBitmap;
    private Drawable iconDrawable;

    private OnIconClickListener iconClickListener;

    public IconRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public IconRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int initLayout() {
        return R.layout.layout_icon_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconRoundCornerProgressBar);

        iconResource = typedArray.getResourceId(R.styleable.IconRoundCornerProgressBar_rcIconSrc, -1);

        iconSize = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconSize, -1);
        iconWidth = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconWidth, dp2px(DEFAULT_ICON_SIZE));
        iconHeight = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconHeight, dp2px(DEFAULT_ICON_SIZE));
        iconPadding = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPadding, -1);
        iconPaddingLeft = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingLeft, dp2px(DEFAULT_ICON_PADDING_LEFT));
        iconPaddingRight = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingRight, dp2px(DEFAULT_ICON_PADDING_RIGHT));
        iconPaddingTop = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingTop, dp2px(DEFAULT_ICON_PADDING_TOP));
        iconPaddingBottom = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingBottom, dp2px(DEFAULT_ICON_PADDING_BOTTOM));

        int defaultIconBackgroundColor = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorIconBackground = typedArray.getColor(R.styleable.IconRoundCornerProgressBar_rcIconBackgroundColor, defaultIconBackgroundColor);

        typedArray.recycle();
    }

    @Override
    protected void initView() {
        ivProgressIcon = findViewById(R.id.iv_progress_icon);
        ivProgressIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iconClickListener != null) {
                    iconClickListener.onIconClick();
                }
            }
        });
    }

    public void setOnIconClickListener(@Nullable OnIconClickListener listener) {
        iconClickListener = listener;
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
        if (isReverse && progress != max) {
            progressDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        } else {
            progressDrawable.setCornerRadii(new float[]{0, 0, newRadius, newRadius, newRadius, newRadius, 0, 0});
        }
        layoutProgress.setBackground(progressDrawable);

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - ((padding * 2) + ivProgressIcon.getWidth())) / ratio);
        ViewGroup.MarginLayoutParams progressParams = (ViewGroup.MarginLayoutParams) layoutProgress.getLayoutParams();
        if (isReverse) {
            if (padding + (progressWidth / 2) < radius) {
                int margin = Math.max(radius - padding, 0) - (progressWidth / 2);
                progressParams.topMargin = margin;
                progressParams.bottomMargin = margin;
            } else {
                progressParams.topMargin = 0;
                progressParams.bottomMargin = 0;
            }
        }
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }

    @Override
    protected void onViewDraw() {
        drawImageIcon();
        drawImageIconSize();
        drawImageIconPadding();
        drawIconBackgroundColor();
    }

    private void drawImageIcon() {
        if (iconResource != -1) {
            ivProgressIcon.setImageResource(iconResource);
        } else if (iconBitmap != null) {
            ivProgressIcon.setImageBitmap(iconBitmap);
        } else if (iconDrawable != null) {
            ivProgressIcon.setImageDrawable(iconDrawable);
        }
    }

    private void drawImageIconSize() {
        if (iconSize == -1) {
            ivProgressIcon.setLayoutParams(new LayoutParams(iconWidth, iconHeight));
        } else {
            ivProgressIcon.setLayoutParams(new LayoutParams(iconSize, iconSize));
        }
    }

    private void drawImageIconPadding() {
        if (iconPadding == -1 || iconPadding == 0) {
            ivProgressIcon.setPadding(iconPaddingLeft, iconPaddingTop, iconPaddingRight, iconPaddingBottom);
        } else {
            ivProgressIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
        }
        ivProgressIcon.invalidate();
    }

    private void drawIconBackgroundColor() {
        GradientDrawable iconBackgroundDrawable = createGradientDrawable(colorIconBackground);
        int radius = getRadius() - (getPadding() / 2);
        iconBackgroundDrawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        ivProgressIcon.setBackground(iconBackgroundDrawable);
    }

    public int getIconImageResource() {
        return iconResource;
    }

    public void setIconImageResource(int resId) {
        this.iconResource = resId;
        this.iconBitmap = null;
        this.iconDrawable = null;
        drawImageIcon();
    }

    public Bitmap getIconImageBitmap() {
        return iconBitmap;
    }

    public void setIconImageBitmap(Bitmap bitmap) {
        this.iconResource = -1;
        this.iconBitmap = bitmap;
        this.iconDrawable = null;
        drawImageIcon();
    }

    public Drawable getIconImageDrawable() {
        return iconDrawable;
    }

    public void setIconImageDrawable(Drawable drawable) {
        this.iconResource = -1;
        this.iconBitmap = null;
        this.iconDrawable = drawable;
        drawImageIcon();
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int size) {
        if (size >= 0) {
            this.iconSize = size;
        }
        drawImageIconSize();
    }

    public int getIconPadding() {
        return iconPadding;
    }

    public void setIconPadding(int padding) {
        if (padding >= 0) {
            this.iconPadding = padding;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingLeft() {
        return iconPaddingLeft;
    }

    public void setIconPaddingLeft(int padding) {
        if (padding > 0) {
            this.iconPaddingLeft = padding;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingRight() {
        return iconPaddingRight;
    }

    public void setIconPaddingRight(int padding) {
        if (padding > 0) {
            this.iconPaddingRight = padding;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingTop() {
        return iconPaddingTop;
    }

    public void setIconPaddingTop(int padding) {
        if (padding > 0) {
            this.iconPaddingTop = padding;
        }
        drawImageIconPadding();
    }

    public int getIconPaddingBottom() {
        return iconPaddingBottom;
    }

    public void setIconPaddingBottom(int padding) {
        if (padding > 0) {
            this.iconPaddingBottom = padding;
        }
        drawImageIconPadding();
    }

    public int getColorIconBackground() {
        return colorIconBackground;
    }

    public void setIconBackgroundColor(int color) {
        this.colorIconBackground = color;
        drawIconBackgroundColor();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.iconResource = this.iconResource;
        ss.iconSize = this.iconSize;
        ss.iconWidth = this.iconWidth;
        ss.iconHeight = this.iconHeight;

        ss.iconPadding = this.iconPadding;
        ss.iconPaddingLeft = this.iconPaddingLeft;
        ss.iconPaddingRight = this.iconPaddingRight;

        ss.iconPaddingTop = this.iconPaddingTop;
        ss.iconPaddingBottom = this.iconPaddingBottom;
        ss.colorIconBackground = this.colorIconBackground;
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

        this.iconResource = ss.iconResource;
        this.iconSize = ss.iconSize;
        this.iconWidth = ss.iconWidth;
        this.iconHeight = ss.iconHeight;
        this.iconPadding = ss.iconPadding;
        this.iconPaddingLeft = ss.iconPaddingLeft;
        this.iconPaddingRight = ss.iconPaddingRight;
        this.iconPaddingTop = ss.iconPaddingTop;
        this.iconPaddingBottom = ss.iconPaddingBottom;
        this.colorIconBackground = ss.colorIconBackground;
    }

    public interface OnIconClickListener {
        void onIconClick();
    }

    protected static class SavedState extends AbsSavedState {
        int iconResource;
        int iconSize;
        int iconWidth;
        int iconHeight;
        int iconPadding;
        int iconPaddingLeft;
        int iconPaddingRight;
        int iconPaddingTop;
        int iconPaddingBottom;
        int colorIconBackground;

         SavedState(Parcelable superState) {
            super(superState);
        }

         SavedState(Parcel in) {
            this(in, null);
        }

         SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.iconResource = in.readInt();
            this.iconSize = in.readInt();
            this.iconWidth = in.readInt();
            this.iconHeight = in.readInt();
            this.iconPadding = in.readInt();
            this.iconPaddingLeft = in.readInt();
            this.iconPaddingRight = in.readInt();
            this.iconPaddingTop = in.readInt();
            this.iconPaddingBottom = in.readInt();
            this.colorIconBackground = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeInt(this.iconResource);
            out.writeInt(this.iconSize);
            out.writeInt(this.iconWidth);
            out.writeInt(this.iconHeight);
            out.writeInt(this.iconPadding);
            out.writeInt(this.iconPaddingLeft);
            out.writeInt(this.iconPaddingRight);
            out.writeInt(this.iconPaddingTop);
            out.writeInt(this.iconPaddingBottom);
            out.writeInt(this.colorIconBackground);
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
