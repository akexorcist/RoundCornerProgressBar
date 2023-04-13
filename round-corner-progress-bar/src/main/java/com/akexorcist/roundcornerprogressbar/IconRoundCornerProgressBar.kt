package com.akexorcist.roundcornerprogressbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.customview.view.AbsSavedState
import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar

@Suppress("unused")
@Keep
open class IconRoundCornerProgressBar : AnimatedRoundCornerProgressBar {
    companion object {
        protected const val DEFAULT_ICON_SIZE = 20
        protected const val DEFAULT_ICON_PADDING_LEFT = 0
        protected const val DEFAULT_ICON_PADDING_RIGHT = 0
        protected const val DEFAULT_ICON_PADDING_TOP = 0
        protected const val DEFAULT_ICON_PADDING_BOTTOM = 0
    }

    private var iconResource = 0
    private var iconSize = 0
    private var iconWidth = 0
    private var iconHeight = 0
    private var iconPadding = 0
    private var iconPaddingLeft = 0
    private var iconPaddingRight = 0
    private var iconPaddingTop = 0
    private var iconPaddingBottom = 0
    private var colorIconBackground = 0

    private var iconBitmap: Bitmap? = null
    private var iconDrawable: Drawable? = null

    private lateinit var ivProgressIcon: ImageView

    private var onIconClick: (() -> Unit)? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun initLayout(): Int = R.layout.layout_icon_round_corner_progress_bar

    override fun initStyleable(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconRoundCornerProgressBar)

        iconResource = typedArray.getResourceId(R.styleable.IconRoundCornerProgressBar_rcIconSrc, -1)

        iconSize = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconSize, -1f).toInt()
        iconWidth = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconWidth, dp2px(DEFAULT_ICON_SIZE.toFloat())).toInt()
        iconHeight = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconHeight, dp2px(DEFAULT_ICON_SIZE.toFloat())).toInt()
        iconPadding = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPadding, -1f).toInt()
        iconPaddingLeft = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingLeft, dp2px(DEFAULT_ICON_PADDING_LEFT.toFloat())).toInt()
        iconPaddingRight = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingRight, dp2px(DEFAULT_ICON_PADDING_RIGHT.toFloat())).toInt()
        iconPaddingTop = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingTop, dp2px(DEFAULT_ICON_PADDING_TOP.toFloat())).toInt()
        iconPaddingBottom = typedArray.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingBottom, dp2px(DEFAULT_ICON_PADDING_BOTTOM.toFloat())).toInt()

        val defaultIconBackgroundColor = ContextCompat.getColor(context, R.color.round_corner_progress_bar_background_default)
        colorIconBackground = typedArray.getColor(R.styleable.IconRoundCornerProgressBar_rcIconBackgroundColor, defaultIconBackgroundColor)

        typedArray.recycle()
    }

    override fun initView() {
        ivProgressIcon = findViewById(R.id.iv_progress_icon)
        ivProgressIcon.setOnClickListener {
            onIconClick?.invoke()
        }
    }

    fun setOnIconClickListener(onIconClick: (() -> Unit)? = null) {
        this.onIconClick = onIconClick
    }

    override fun drawProgress(
        layoutProgress: LinearLayout,
        progressDrawable: GradientDrawable,
        max: Float,
        progress: Float,
        totalWidth: Float,
        radius: Int,
        padding: Int,
        isReverse: Boolean,
    ) {
        val newRadius = radius - (padding / 2f)
        if (isReverse && progress != max) {
            progressDrawable.cornerRadii = floatArrayOf(
                newRadius,
                newRadius,
                newRadius,
                newRadius,
                newRadius,
                newRadius,
                newRadius,
                newRadius,
            )
        } else {
            progressDrawable.cornerRadii = floatArrayOf(
                0f,
                0f,
                newRadius,
                newRadius,
                newRadius,
                newRadius,
                0f,
                0f
            )
        }
        layoutProgress.background = progressDrawable

        val ratio = max / progress
        val progressWidth = ((totalWidth - ((padding * 2) + ivProgressIcon.width)) / ratio).toInt()
        val progressParams = layoutProgress.layoutParams as MarginLayoutParams
        if (isReverse) {
            if (padding + (progressWidth / 2) < radius) {
                val margin = (radius - padding).coerceAtLeast(0) - (progressWidth / 2)
                progressParams.topMargin = margin
                progressParams.bottomMargin = margin
            } else {
                progressParams.topMargin = 0
                progressParams.bottomMargin = 0
            }
        }
        progressParams.width = progressWidth
        layoutProgress.layoutParams = progressParams
    }

    override fun onViewDraw() {
        drawImageIcon()
        drawImageIconSize()
        drawImageIconPadding()
        drawIconBackgroundColor()
    }

    private fun drawImageIcon() {
        when {
            iconResource != -1 -> ivProgressIcon.setImageResource(iconResource)
            iconBitmap != null -> ivProgressIcon.setImageBitmap(iconBitmap)
            iconDrawable != null -> ivProgressIcon.setImageDrawable(iconDrawable)
            else -> ivProgressIcon.setImageResource(0)
        }
    }

    private fun drawImageIconSize() {
        @Suppress("IntroduceWhenSubject")
        val layoutParams = when {
            iconSize == -1 -> LayoutParams(iconWidth, iconHeight)
            else -> LayoutParams(iconSize, iconSize)
        }
        ivProgressIcon.layoutParams = layoutParams
    }

    private fun drawImageIconPadding() {
        @Suppress("IntroduceWhenSubject")
        when {
            iconPadding == -1 || iconPadding == 0 ->
                ivProgressIcon.setPadding(iconPaddingLeft, iconPaddingTop, iconPaddingRight, iconPaddingBottom)
            else ->
                ivProgressIcon.setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
        }
        ivProgressIcon.invalidate()
    }

    private fun drawIconBackgroundColor() {
        val iconBackgroundDrawable = createGradientDrawable(colorIconBackground)
        val radius = getRadius() - (getPadding() / 2)
        iconBackgroundDrawable.cornerRadii = floatArrayOf(
            radius.toFloat(),
            radius.toFloat(),
            0f,
            0f,
            0f,
            0f,
            radius.toFloat(),
            radius.toFloat(),
        )
        ivProgressIcon.background = iconBackgroundDrawable
    }

    fun getIconImageResource(): Int = iconResource

    fun setIconImageResource(resId: Int) {
        iconResource = resId
        iconBitmap = null
        iconDrawable = null
        drawImageIcon()
    }

    fun getIconImageBitmap(): Bitmap? = iconBitmap

    fun setIconImageBitmap(bitmap: Bitmap?) {
        iconResource = -1
        iconBitmap = bitmap
        iconDrawable = null
        drawImageIcon()
    }

    fun getIconImageDrawable(): Drawable? = iconDrawable

    fun setIconImageDrawable(drawable: Drawable?) {
        iconResource = -1
        iconBitmap = null
        iconDrawable = drawable
        drawImageIcon()
    }

    fun getIconSize(): Int = iconSize

    fun setIconSize(size: Int) {
        if (size >= 0) {
            iconSize = size
        }
        drawImageIconSize()
    }

    fun getIconPadding(): Int = iconPadding

    fun setIconPadding(padding: Int) {
        if (padding >= 0) {
            iconPadding = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingLeft(): Int = iconPaddingLeft

    fun setIconPaddingLeft(padding: Int) {
        if (padding > 0) {
            iconPaddingLeft = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingRight(): Int = iconPaddingRight

    fun setIconPaddingRight(padding: Int) {
        if (padding > 0) {
            iconPaddingRight = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingTop(): Int = iconPaddingTop

    fun setIconPaddingTop(padding: Int) {
        if (padding > 0) {
            iconPaddingTop = padding
        }
        drawImageIconPadding()
    }

    fun getIconPaddingBottom(): Int = iconPaddingBottom

    fun setIconPaddingBottom(padding: Int) {
        if (padding > 0) {
            iconPaddingBottom = padding
        }
        drawImageIconPadding()
    }

    fun getColorIconBackground(): Int = colorIconBackground

    fun setIconBackgroundColor(color: Int) {
        colorIconBackground = color
        drawIconBackgroundColor()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState() ?: return null
        val state = SavedState(superState)
        state.iconResource = this.iconResource
        state.iconSize = this.iconSize
        state.iconWidth = this.iconWidth
        state.iconHeight = this.iconHeight
        state.iconPadding = this.iconPadding
        state.iconPaddingLeft = this.iconPaddingLeft
        state.iconPaddingRight = this.iconPaddingRight
        state.iconPaddingTop = this.iconPaddingTop
        state.iconPaddingBottom = this.iconPaddingBottom
        state.colorIconBackground = this.colorIconBackground
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        this.iconResource = state.iconResource
        this.iconSize = state.iconSize
        this.iconWidth = state.iconWidth
        this.iconHeight = state.iconHeight
        this.iconPadding = state.iconPadding
        this.iconPaddingLeft = state.iconPaddingLeft
        this.iconPaddingRight = state.iconPaddingRight
        this.iconPaddingTop = state.iconPaddingTop
        this.iconPaddingBottom = state.iconPaddingBottom
        this.colorIconBackground = state.colorIconBackground
    }

    protected class SavedState : AbsSavedState {
        var iconResource = 0
        var iconSize = 0
        var iconWidth = 0
        var iconHeight = 0
        var iconPadding = 0
        var iconPaddingLeft = 0
        var iconPaddingRight = 0
        var iconPaddingTop = 0
        var iconPaddingBottom = 0
        var colorIconBackground = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel) : super(source, null)

        constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {
            this.iconResource = source.readInt()
            this.iconSize = source.readInt()
            this.iconWidth = source.readInt()
            this.iconHeight = source.readInt()
            this.iconPadding = source.readInt()
            this.iconPaddingLeft = source.readInt()
            this.iconPaddingRight = source.readInt()
            this.iconPaddingTop = source.readInt()
            this.iconPaddingBottom = source.readInt()
            this.colorIconBackground = source.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(this.iconResource)
            dest.writeInt(this.iconSize)
            dest.writeInt(this.iconWidth)
            dest.writeInt(this.iconHeight)
            dest.writeInt(this.iconPadding)
            dest.writeInt(this.iconPaddingLeft)
            dest.writeInt(this.iconPaddingRight)
            dest.writeInt(this.iconPaddingTop)
            dest.writeInt(this.iconPaddingBottom)
            dest.writeInt(this.colorIconBackground)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> = object : Parcelable.ClassLoaderCreator<SavedState> {
                override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState = SavedState(source, loader)

                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)

                override fun newArray(size: Int): Array<SavedState> = newArray(size)
            }
        }
    }
}