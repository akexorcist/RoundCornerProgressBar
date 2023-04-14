package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class IconTextRoundCornerProgressBar extends TextRoundCornerProgressBar implements View.OnClickListener {

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

    private OnIconClickListener iconClickListener;

    public IconTextRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconTextRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int initLayout() {
        return R.layout.layout_icon_text_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(@NonNull Context context, AttributeSet attrs) {
        super.initStyleable(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconRoundCornerProgressBar);

        iconResource = typedArray
                .getResourceId(R.styleable.IconRoundCornerProgressBar_rcIconSrc, -1);

        iconSize = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconSize, -1);
        iconWidth = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconWidth, dp2px(DEFAULT_ICON_SIZE));
        iconHeight = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconHeight, dp2px(DEFAULT_ICON_SIZE));
        iconPadding = (int) typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPadding, -1);
        iconPaddingLeft = (int) typedArray
                .getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingLeft, dp2px(DEFAULT_ICON_PADDING_LEFT));
        iconPaddingRight = (int) typedArray
                .getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingRight, dp2px(DEFAULT_ICON_PADDING_RIGHT));
        iconPaddingTop = (int) typedArray
                .getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingTop, dp2px(DEFAULT_ICON_PADDING_TOP));
        iconPaddingBottom = (int) typedArray
                .getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingBottom, dp2px(DEFAULT_ICON_PADDING_BOTTOM));

        int colorIconBackgroundDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorIconBackground = typedArray
                .getColor(R.styleable.IconRoundCornerProgressBar_rcIconBackgroundColor, colorIconBackgroundDefault);

        typedArray.recycle();
    }

    @Override
    protected void initView() {
        super.initView();
        ivProgressIcon = findViewById(R.id.iv_progress_icon);
        ivProgressIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_progress_icon && iconClickListener != null) {
            iconClickListener.onIconClick();
        }
    }

    public void setOnIconClickListener(OnIconClickListener listener) {
        iconClickListener = listener;
    }

    @Override
    protected void drawProgress(@NonNull LinearLayout layoutProgress, @NonNull GradientDrawable progressDrawable, float max, float progress, float totalWidth, int radius, int padding, boolean isReverse) {
        layoutProgress.setBackground(progressDrawable);
        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - ((padding * 2) + ivProgressIcon.getWidth())) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
        super.drawProgress(layoutProgress, progressDrawable, get_max(), progress, totalWidth, radius, padding, isReverse);
    }

    @Override
    protected void onViewDraw() {
        drawImageIcon();
        drawImageIconSize();
        drawImageIconPadding();
        drawIconBackgroundColor();
        super.onViewDraw();
    }

    public ImageView getProgressIcon() {
        return ivProgressIcon;
    }

    private void drawImageIcon() {
        ivProgressIcon.setImageResource(iconResource);
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

    private static class SavedState extends BaseSavedState {

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

        private SavedState(Parcel in) {
            super(in);

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

        public static final Creator<SavedState> CREATOR = new Creator<IconTextRoundCornerProgressBar.SavedState>() {
            public IconTextRoundCornerProgressBar.SavedState createFromParcel(Parcel in) {
                return new IconTextRoundCornerProgressBar.SavedState(in);
            }

            public IconTextRoundCornerProgressBar.SavedState[] newArray(int size) {
                return new IconTextRoundCornerProgressBar.SavedState[size];
            }
        };
    }

    public interface OnIconClickListener {

        public void onIconClick();
    }
}
