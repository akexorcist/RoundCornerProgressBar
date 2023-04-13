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
import androidx.core.content.ContextCompat
import androidx.customview.view.AbsSavedState
import com.akexorcist.roundcornerprogressbar.R

@Suppress("unused", "MemberVisibilityCanBePrivate", "PropertyName")
@Keep
abstract class BaseRoundCornerProgressBar : LinearLayout {
    companion object {
        protected const val DEFAULT_MAX_PROGRESS = 100
        protected const val DEFAULT_PROGRESS = 0
        protected const val DEFAULT_SECONDARY_PROGRESS = 0
        protected const val DEFAULT_PROGRESS_RADIUS = 30
        protected const val DEFAULT_BACKGROUND_PADDING = 0
    }

    protected lateinit var layoutBackground: LinearLayout
    protected lateinit var layoutProgress: LinearLayout
    protected lateinit var layoutSecondaryProgress: LinearLayout

    protected var _radius = 0
    protected var _padding = 0
    protected var _totalWidth = 0

    protected var _max = 0f
    protected var _progress = 0f
    protected var _secondaryProgress = 0f

    protected var _backgroundColor = 0
    private var _progressColor = 0
    protected var _secondaryProgressColor = 0
    protected var _progressColors: IntArray? = null
    protected var _secondaryProgressColors: IntArray? = null

    protected var _isReverse = false

    protected var _progressChangedListener: OnProgressChangedListener? = null
    private var _onProgressChanged: ((
        view: View,
        progress: Float,
        isPrimaryProgress: Boolean,
        isSecondaryProgress: Boolean
    ) -> Unit)? = null

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

        layoutBackground = findViewById(R.id.layout_background)
        layoutProgress = findViewById(R.id.layout_progress)
        layoutSecondaryProgress = findViewById(R.id.layout_secondary_progress)

        initView()
    }

    protected open fun setupStyleable(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRoundCornerProgressBar)
        _radius = typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcRadius, dp2px(DEFAULT_PROGRESS_RADIUS.toFloat())).toInt()
        _padding = typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING.toFloat())).toInt()

        _isReverse = typedArray.getBoolean(R.styleable.BaseRoundCornerProgressBar_rcReverse, false)

        _max = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcMax, DEFAULT_MAX_PROGRESS.toFloat())
        _progress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcProgress, DEFAULT_PROGRESS.toFloat())
        _secondaryProgress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgress, DEFAULT_SECONDARY_PROGRESS.toFloat())

        val defaultBackgroundColor = ContextCompat.getColor(context, R.color.round_corner_progress_bar_background_default)
        _backgroundColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcBackgroundColor, defaultBackgroundColor)

        _progressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcProgressColor, -1)
        val progressColorResourceId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcProgressColors, 0)
        _progressColors = progressColorResourceId.takeIf { it != 0 }
            ?.let { resources.getIntArray(progressColorResourceId) }

        _secondaryProgressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColor, -1)
        val secondaryProgressColorResourceId = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColors, 0)
        _secondaryProgressColors = secondaryProgressColorResourceId.takeIf { it != 0 }
            ?.let { resources.getIntArray(secondaryProgressColorResourceId) }

        typedArray.recycle()

        initStyleable(context, attrs)
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        _totalWidth = newWidth
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
        val backgroundDrawable: GradientDrawable = createGradientDrawable(_backgroundColor)
        val newRadius = _radius - (_padding / 2f)
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
        _progressColor != -1 -> {
            createGradientDrawable(_progressColor)
        }
        _progressColors != null && _progressColors?.isNotEmpty() == true -> {
            createGradientDrawable(_progressColors)
        }
        else -> {
            val defaultColor = ContextCompat.getColor(context, R.color.round_corner_progress_bar_progress_default)
            createGradientDrawable(defaultColor)
        }
    }

    // Create gradient drawable depends on secondaryProgressColor and secondaryProgressColors value
    private fun createSecondaryProgressDrawable(): GradientDrawable = when {
        _secondaryProgressColor != -1 -> {
            createGradientDrawable(_secondaryProgressColor)
        }
        _secondaryProgressColors != null && _secondaryProgressColors?.isNotEmpty() == true -> {
            createGradientDrawable(_secondaryProgressColors)
        }
        else -> {
            val defaultColor = ContextCompat.getColor(context, R.color.round_corner_progress_bar_secondary_progress_default)
            createGradientDrawable(defaultColor)
        }
    }

    private fun drawPrimaryProgress() {
        val possibleRadius = _radius.coerceAtMost(layoutBackground.measuredHeight / 2)
        drawProgress(
            layoutProgress = layoutProgress,
            progressDrawable = createProgressDrawable(),
            max = _max,
            progress = _progress,
            totalWidth = _totalWidth.toFloat(),
            radius = possibleRadius,
            padding = _padding,
            isReverse = _isReverse,
        )
    }

    private fun drawSecondaryProgress() {
        val possibleRadius = _radius.coerceAtMost(layoutBackground.measuredHeight / 2)
        drawProgress(
            layoutProgress = layoutSecondaryProgress,
            progressDrawable = createSecondaryProgressDrawable(),
            max = _max,
            progress = _secondaryProgress,
            totalWidth = _totalWidth.toFloat(),
            radius = possibleRadius,
            padding = _padding,
            isReverse = _isReverse,
        )
    }

    private fun drawProgressReverse() {
        setupProgressReversing(layoutProgress, _isReverse)
        setupProgressReversing(layoutSecondaryProgress, _isReverse)
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
        layoutBackground.setPadding(_padding, _padding, _padding, _padding)
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
        return _isReverse
    }

    fun setReverse(isReverse: Boolean) {
        _isReverse = isReverse
        drawProgressReverse()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getRadius(): Int {
        return _radius
    }

    fun setRadius(@Px radius: Int) {
        if (radius >= 0) {
            _radius = radius
        }
        drawBackgroundProgress()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getPadding(): Int {
        return _padding
    }

    fun setPadding(@Px padding: Int) {
        if (padding >= 0) {
            _padding = padding
        }
        drawPadding()
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getMax(): Float {
        return _max
    }

    fun setMax(max: Int) {
        setMax(max.toFloat())
    }

    fun setMax(max: Float) {
        if (max >= 0) {
            _max = max
        }
        if (_progress > max) {
            _progress = max
        }
        drawPrimaryProgress()
        drawSecondaryProgress()
    }

    fun getLayoutWidth(): Float {
        return _totalWidth.toFloat()
    }

    open fun getProgress(): Float {
        return _progress
    }

    open fun setProgress(progress: Int) {
        setProgress(progress.toFloat())
    }

    open fun setProgress(progress: Float) {
        _progress =
            if (progress < 0) 0f
            else progress.coerceAtMost(_max)
        drawPrimaryProgress()
        _onProgressChanged?.invoke(this, _progress, true, false)
    }

    fun getSecondaryProgressWidth() = layoutSecondaryProgress.width.toFloat()

    open fun getSecondaryProgress() = _secondaryProgress

    open fun setSecondaryProgress(progress: Int) {
        setSecondaryProgress(progress.toFloat())
    }

    open fun setSecondaryProgress(progress: Float) {
        _secondaryProgress =
            if (progress < 0) 0f
            else progress.coerceAtMost(_max)
        drawSecondaryProgress()
        _onProgressChanged?.invoke(this, _secondaryProgress, false, true)
    }

    fun getProgressBackgroundColor(): Int = _backgroundColor

    fun setProgressBackgroundColor(@ColorInt color: Int) {
        _backgroundColor = color
        drawBackgroundProgress()
    }

    fun getProgressColor(): Int {
        return _progressColor
    }

    fun setProgressColor(@ColorInt color: Int) {
        _progressColor = color
        _progressColors = null
        drawPrimaryProgress()
    }

    fun getProgressColors(): IntArray? = _progressColors

    fun setProgressColors(colors: IntArray?) {
        _progressColor = -1
        _progressColors = colors
        drawPrimaryProgress()
    }

    fun getSecondaryProgressColor(): Int = _secondaryProgressColor

    fun setSecondaryProgressColor(@ColorInt color: Int) {
        _secondaryProgressColor = color
        _secondaryProgressColors = null
        drawSecondaryProgress()
    }

    fun getSecondaryProgressColors(): IntArray? = _secondaryProgressColors

    fun setSecondaryProgressColors(colors: IntArray?) {
        _secondaryProgressColor = -1
        _secondaryProgressColors = colors
        drawSecondaryProgress()
    }

    override fun invalidate() {
        super.invalidate()
        drawAll()
    }

    fun setOnProgressChangedListener(
        onProgressChanged: ((
            view: View,
            progress: Float,
            isPrimaryProgress: Boolean,
            isSecondaryProgress: Boolean
        ) -> Unit)?
    ) {
        _onProgressChanged = onProgressChanged
    }

    interface OnProgressChangedListener {
        fun onProgressChanged(view: View, progress: Float, isPrimaryProgress: Boolean, isSecondaryProgress: Boolean)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState() ?: return null
        val state = SavedState(superState)

        state.max = this._max
        state.progress = this._progress
        state.secondaryProgress = this._secondaryProgress

        state.radius = this._radius
        state.padding = this._padding

        state.colorBackground = this._backgroundColor
        state.colorProgress = this._progressColor
        state.colorSecondaryProgress = this._secondaryProgressColor

        state.colorProgressArray = this._progressColors
        state.colorSecondaryProgressArray = this._secondaryProgressColors

        state.isReverse = this._isReverse
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        this._max = state.max
        this._progress = state.progress
        this._secondaryProgress = state.secondaryProgress

        this._radius = state.radius
        this._padding = state.padding

        this._backgroundColor = state.colorBackground
        this._progressColor = state.colorProgress
        this._secondaryProgressColor = state.colorSecondaryProgress

        this._progressColors = state.colorProgressArray
        this._secondaryProgressColors = state.colorSecondaryProgressArray

        this._isReverse = state.isReverse
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
