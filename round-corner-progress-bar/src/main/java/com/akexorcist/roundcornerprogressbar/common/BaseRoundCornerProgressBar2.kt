package com.akexorcist.roundcornerprogressbar.common

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.ClassLoaderCreator
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.customview.view.AbsSavedState
import com.akexorcist.roundcornerprogressbar.R

@Suppress("unused")
@Keep
abstract class BaseRoundCornerProgressBar2 : LinearLayout {
    companion object {
        protected const val DEFAULT_MAX_PROGRESS = 100
        protected const val DEFAULT_PROGRESS = 0
        protected const val DEFAULT_SECONDARY_PROGRESS = 0
        protected const val DEFAULT_PROGRESS_RADIUS = 30
        protected const val DEFAULT_BACKGROUND_PADDING = 0
    }

    protected val layoutBackground: LinearLayout by lazy {
        findViewById(R.id.layout_background)
    }

    protected val layoutProgress: LinearLayout by lazy {
        findViewById(R.id.layout_progress)
    }

    protected val layoutSecondaryProgress: LinearLayout by lazy {
        findViewById(R.id.layout_secondary_progress)
    }

    protected var radius = 0
    protected var padding = 0
    protected var totalWidth = 0

    protected var max = 0f
    protected var progress = 0f
    protected var secondaryProgress = 0f

    protected var backgroundColor = 0
    private var progressColor = 0
    protected var secondaryProgressColor = 0
    protected var progressColors: IntArray? = null
    protected var secondaryProgressColors: IntArray? = null

    protected var isReverse = false

    protected var progressChangedListener: OnProgressChangedListener? = null

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        setup(context, attrs)
    }

    protected abstract fun initLayout(): Int

    protected abstract fun initStyleable(context: Context, attrs: AttributeSet?)

    protected abstract fun initView()

    protected abstract fun drawProgress(
        layoutProgress: LinearLayout,
        progressDrawable: GradientDrawable,
        max: Float,
        progress: Float,
        totalWidth: Float,
        radius: Int,
        padding: Int,
        isReverse: Boolean,
    )

    protected abstract fun onViewDraw()

    private fun setup(context: Context, attrs: AttributeSet?) {
        setupStyleable(context, attrs)

        removeAllViews()
        LayoutInflater.from(context).inflate(initLayout(), this)
        initView()
    }

    protected open fun setupStyleable(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRoundCornerProgressBar)
        radius = typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS.toFloat())).toInt()
        padding = typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING.toFloat())).toInt()

        isReverse = typedArray.getBoolean(R.styleable.BaseRoundCornerProgressBar_rcReverse, false)

        max = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcMax, DEFAULT_MAX_PROGRESS.toFloat())
        progress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcProgress, DEFAULT_PROGRESS.toFloat())
        secondaryProgress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS.toFloat())

        val defaultBackgroundColor = context.resources.getColor(R.color.round_corner_progress_bar_background_default)
        backgroundColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcBackgroundColor, defaultBackgroundColor)

        progressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcProgressColor, -1)
        val progressColorResourceId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcProgressColors, 0)
        progressColors = progressColorResourceId.takeIf { it != 0 }
            ?.let { resources.getIntArray(progressColorResourceId) }

        secondaryProgressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColor, -1)
        val secondaryProgressColorResourceId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColors, 0)
        secondaryProgressColors = secondaryProgressColorResourceId.takeIf { it != 0 }
            ?.let { resources.getIntArray(secondaryProgressColorResourceId) }

        typedArray.recycle()

        initStyleable(context, attrs)
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        totalWidth = newWidth
        drawBackgroundProgress()
        drawPadding()
        drawProgressReverse()

        // Can't instantly change the size of child views (primary & secondary progress)
        // when `onSizeChanged(...)` called. Using `post` method then
        // call these methods inside the Runnable will solved this.
        // And I can't reuse the `drawAll()` method because this problem.
        post {
            drawPrimaryProgress()
            drawSecondaryProgress()
        }
        onViewDraw()
    }

    // Redraw all view
    private fun drawAll() {
        drawBackgroundProgress()
        drawPadding()
        drawProgressReverse()
        drawPrimaryProgress()
        drawSecondaryProgress()
        onViewDraw()
    }

    // Draw progress background
    private fun drawBackgroundProgress() {
        val backgroundDrawable: GradientDrawable = createGradientDrawable(backgroundColor)
        val newRadius = radius - (padding / 2f)
        backgroundDrawable.cornerRadii = floatArrayOf(
            newRadius,
            newRadius,
            newRadius,
            newRadius,
            newRadius,
            newRadius,
            newRadius,
            newRadius,
        )
        layoutBackground.background = backgroundDrawable
    }

    // Create an color rectangle gradient drawable
    protected fun createGradientDrawable(@ColorInt color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(color)
    }

    // Create an multi-color rectangle gradient drawable
    protected open fun createGradientDrawable(colors: IntArray?) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        orientation = if (!isReverse()) GradientDrawable.Orientation.LEFT_RIGHT else GradientDrawable.Orientation.RIGHT_LEFT
        setColors(colors)
    }

    // Create gradient drawable depends on progressColor and progressColors value
    private fun createProgressDrawable(): GradientDrawable = when {
        progressColor != -1 -> {
            createGradientDrawable(progressColor)
        }
        progressColors != null && progressColors?.isNotEmpty() == true -> {
            createGradientDrawable(progressColors)
        }
        else -> {
            val defaultColor = resources.getColor(R.color.round_corner_progress_bar_progress_default)
            createGradientDrawable(defaultColor)
        }
    }

    // Create gradient drawable depends on secondaryProgressColor and secondaryProgressColors value
    private fun createSecondaryProgressDrawable(): GradientDrawable = when {
        secondaryProgressColor != -1 -> {
            createGradientDrawable(secondaryProgressColor)
        }
        secondaryProgressColors != null && secondaryProgressColors?.isNotEmpty() == true -> {
            createGradientDrawable(secondaryProgressColors)
        }
        else -> {
            val defaultColor = resources.getColor(R.color.round_corner_progress_bar_secondary_progress_default)
            createGradientDrawable(defaultColor)
        }
    }

    private fun drawPrimaryProgress() {
        val possibleRadius = radius.coerceAtMost(layoutBackground.measuredHeight / 2)
        drawProgress(
            layoutProgress = layoutProgress,
            progressDrawable = createProgressDrawable(),
            max = max,
            progress = progress,
            totalWidth = totalWidth.toFloat(),
            radius = possibleRadius,
            padding = padding,
            isReverse = isReverse,
        )
    }

    private fun drawSecondaryProgress() {
        val possibleRadius = radius.coerceAtMost(layoutBackground.measuredHeight / 2)
        drawProgress(
            layoutProgress = layoutSecondaryProgress,
            progressDrawable = createSecondaryProgressDrawable(),
            max = max,
            progress = secondaryProgress,
            totalWidth = totalWidth.toFloat(),
            radius = possibleRadius,
            padding = padding,
            isReverse = isReverse,
        )
    }

    private fun drawProgressReverse() {
        setupProgressReversing(layoutProgress, isReverse)
        setupProgressReversing(layoutSecondaryProgress, isReverse)
    }

    // Change progress position by depending on isReverse flag
    private fun setupProgressReversing(layoutProgress: LinearLayout, isReverse: Boolean) {
        val progressParams = layoutProgress.layoutParams as RelativeLayout.LayoutParams
        removeLayoutParamsRule(progressParams)
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_START)
        }
        layoutProgress.layoutParams = progressParams
    }

    private fun drawPadding() {
        layoutBackground.setPadding(padding, padding, padding, padding)
    }

    // Remove all of relative align rule
    private fun removeLayoutParamsRule(layoutParams: RelativeLayout.LayoutParams) {
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
    }

    protected fun dp2px(dp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
    }

    fun isReverse(): Boolean {
        return isReverse
    }

    fun setReverse(isReverse: Boolean) {
        this.isReverse = isReverse
        drawProgressReverse()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getRadius(): Int {
        return radius
    }

    fun setRadius(@Px radius: Int) {
        if (radius >= 0) {
            this.radius = radius
        }
        drawBackgroundProgress()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getPadding(): Int {
        return padding
    }

    fun setPadding(@Px padding: Int) {
        if (padding >= 0) {
            this.padding = padding
        }
        drawPadding()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getMax(): Float {
        return max
    }

    fun setMax(max: Float) {
        if (max >= 0) {
            this.max = max
        }
        if (progress > max) {
            progress = max
        }
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getLayoutWidth(): Float {
        return totalWidth.toFloat()
    }

    open fun getProgress(): Float {
        return progress
    }

    open fun setProgress(progress: Int) {
        setProgress(progress.toFloat())
    }

    open fun setProgress(progress: Float) {
        this.progress =
            if (progress < 0) 0f
            else progress.coerceAtMost(max)

        drawPrimaryProgress()
        progressChangedListener?.onProgressChanged(
            view = this,
            progress = this.progress,
            isPrimaryProgress = true,
            isSecondaryProgress = false
        )
    }

    fun getSecondaryProgressWidth() = layoutSecondaryProgress.width.toFloat()

    open fun getSecondaryProgress() = secondaryProgress

    open fun setSecondaryProgress(progress: Int) {
        setSecondaryProgress(progress.toFloat())
    }

    open fun setSecondaryProgress(progress: Float) {
        this.secondaryProgress =
            if (progress < 0) 0f
            else progress.coerceAtMost(max)

        drawSecondaryProgress()
        progressChangedListener?.onProgressChanged(
            view = this,
            progress = secondaryProgress,
            isPrimaryProgress = false,
            isSecondaryProgress = true
        )
    }

    fun getProgressBackgroundColor(): Int = backgroundColor

    fun setProgressBackgroundColor(@ColorInt color: Int) {
        backgroundColor = color
        drawBackgroundProgress()
    }

    fun getProgressColor(): Int {
        return progressColor
    }

    fun setProgressColor(@ColorInt color: Int) {
        progressColor = color
        progressColors = null
        drawPrimaryProgress()
    }

    fun getProgressColors(): IntArray? = progressColors

    fun setProgressColors(colors: IntArray?) {
        progressColor = -1
        progressColors = colors
        drawPrimaryProgress()
    }

    fun getSecondaryProgressColor(): Int = secondaryProgressColor

    fun setSecondaryProgressColor(@ColorInt color: Int) {
        secondaryProgressColor = color
        secondaryProgressColors = null
        drawSecondaryProgress()
    }

    fun getSecondaryProgressColors(): IntArray? = secondaryProgressColors

    fun setSecondaryProgressColors(colors: IntArray?) {
        secondaryProgressColor = -1
        secondaryProgressColors = colors
        drawSecondaryProgress()
    }

    override fun invalidate() {
        super.invalidate()
        drawAll()
    }

    fun setOnProgressChangedListener(listener: OnProgressChangedListener?) {
        progressChangedListener = listener
    }

    interface OnProgressChangedListener {
        fun onProgressChanged(view: View?, progress: Float, isPrimaryProgress: Boolean, isSecondaryProgress: Boolean)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState() ?: return null
        val state = SavedState(superState)

        state.max = this.max
        state.progress = this.progress
        state.secondaryProgress = this.secondaryProgress

        state.radius = this.radius
        state.padding = this.padding

        state.colorBackground = this.backgroundColor
        state.colorProgress = this.progressColor
        state.colorSecondaryProgress = this.secondaryProgressColor

        state.colorProgressArray = this.progressColors
        state.colorSecondaryProgressArray = this.secondaryProgressColors

        state.isReverse = this.isReverse
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        this.max = state.max
        this.progress = state.progress
        this.secondaryProgress = state.secondaryProgress

        this.radius = state.radius
        this.padding = state.padding

        this.backgroundColor = state.colorBackground
        this.progressColor = state.colorProgress
        this.secondaryProgressColor = state.colorSecondaryProgress

        this.progressColors = state.colorProgressArray
        this.secondaryProgressColors = state.colorSecondaryProgressArray

        this.isReverse = state.isReverse
    }

    protected class SavedState : AbsSavedState {
        var max = 0f
        var progress = 0f
        var secondaryProgress = 0f
        var radius = 0
        var padding = 0
        var colorBackground = 0
        var colorProgress = 0
        var colorSecondaryProgress = 0
        var colorProgressArray: IntArray? = null
        var colorSecondaryProgressArray: IntArray? = null
        var isReverse = false

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel) : super(source, null)

        constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {
            this.max = source.readFloat()
            this.progress = source.readFloat()
            this.secondaryProgress = source.readFloat()

            this.radius = source.readInt()
            this.padding = source.readInt()

            this.colorBackground = source.readInt()
            this.colorProgress = source.readInt()
            this.colorSecondaryProgress = source.readInt()

            val colorProgressArray = IntArray(source.readInt())
            source.readIntArray(colorProgressArray)
            this.colorProgressArray = colorProgressArray

            val colorSecondaryProgressArray = IntArray(source.readInt())
            source.readIntArray(colorSecondaryProgressArray)
            this.colorSecondaryProgressArray = colorSecondaryProgressArray

            this.isReverse = source.readByte().toInt() != 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeFloat(this.max)
            dest.writeFloat(this.progress)
            dest.writeFloat(this.secondaryProgress)

            dest.writeInt(this.radius)
            dest.writeInt(this.padding)

            dest.writeInt(this.colorBackground)
            dest.writeInt(this.colorProgress)
            dest.writeInt(this.colorSecondaryProgress)

            dest.writeInt(this.colorProgressArray?.size ?: 0)
            dest.writeIntArray(this.colorProgressArray ?: intArrayOf())

            dest.writeInt(this.colorSecondaryProgressArray?.size ?: 0)
            dest.writeIntArray(this.colorSecondaryProgressArray ?: intArrayOf())

            dest.writeByte((if (this.isReverse) 1 else 0).toByte())
        }

        companion object {
            @JvmField
            val CREATOR: ClassLoaderCreator<SavedState> = object : ClassLoaderCreator<SavedState> {
                override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState = SavedState(source, loader)

                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)

                override fun newArray(size: Int): Array<SavedState> = newArray(size)
            }
        }
    }
}
