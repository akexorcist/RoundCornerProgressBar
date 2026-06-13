package com.akexorcist.roundcornerprogressbar.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.ClassLoaderCreator
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.customview.view.AbsSavedState
import com.akexorcist.roundcornerprogressbar.R
import kotlin.math.abs

@Suppress("unused")
abstract class AnimatedRoundCornerProgressBar : BaseRoundCornerProgressBar {
    companion object {
        const val DEFAULT_DURATION = 500L
    }

    private var _isProgressAnimating = false
    private var _isSecondaryProgressAnimating = false
    private var _lastProgress = 0f
    private var _lastSecondaryProgress = 0f

    private var _animationSpeedScale = 1f
    private var _isAnimationEnabled = false

    private var _progressAnimator: ValueAnimator? = null
    private var _secondaryProgressAnimator: ValueAnimator? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun setupStyleable(context: Context, attrs: AttributeSet?) {
        super.setupStyleable(context, attrs)
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.AnimatedRoundCornerProgressBar
        )

        with(typedArray) {
            _isAnimationEnabled = getBoolean(
                R.styleable.AnimatedRoundCornerProgressBar_rcAnimationEnable, false
            )
            _animationSpeedScale = getFloat(
                R.styleable.AnimatedRoundCornerProgressBar_rcAnimationSpeedScale, 1f
            )
            recycle()
        }
        _lastProgress = super.getProgress()
        _lastSecondaryProgress = super.getSecondaryProgress()
    }

    override fun getProgress(): Float {
        return if (!_isAnimationEnabled && !_isProgressAnimating) {
            super.getProgress()
        } else {
            _lastProgress
        }
    }

    override fun setProgress(progress: Int) {
        setProgress(progress.toFloat())
    }

    override fun setProgress(progress: Float) {
        val actualProgress =
            if (progress < 0) 0f
            else progress.coerceAtMost(_max)

        clearProgressAnimation()
        this._lastProgress = actualProgress

        if (this._isAnimationEnabled) {
            startProgressAnimation(super.getProgress(), actualProgress)
        } else {
            super.setProgress(actualProgress)
        }
    }

    override fun getSecondaryProgress(): Float {
        return if (!_isAnimationEnabled && !_isSecondaryProgressAnimating) {
            super.getSecondaryProgress()
        } else {
            _lastSecondaryProgress
        }
    }

    override fun setSecondaryProgress(progress: Int) {
        setSecondaryProgress(progress.toFloat())
    }

    override fun setSecondaryProgress(progress: Float) {
        val actualProgress =
            if (progress < 0) 0f
            else progress.coerceAtMost(_max)
        clearSecondaryProgressAnimation()
        _lastSecondaryProgress = actualProgress
        if (_isAnimationEnabled) {
            startSecondaryProgressAnimation(super.getSecondaryProgress(), actualProgress)
        } else {
            super.setSecondaryProgress(actualProgress)
        }
    }

    @FloatRange(from = 0.2, to = 5.toDouble())
    open fun getAnimationSpeedScale(): Float {
        return _animationSpeedScale
    }

    fun enableAnimation() {
        _isAnimationEnabled = true
    }

    fun disableAnimation() {
        _isAnimationEnabled = false
    }

    fun setAnimationSpeedScale(@FloatRange(from = 0.2, to = 5.toDouble()) scale: Float) {
        _animationSpeedScale = scale.coerceIn(0.2f, 5f)
    }

    fun isProgressAnimating(): Boolean = _isProgressAnimating

    fun isSecondaryProgressAnimating(): Boolean = _isSecondaryProgressAnimating

    protected open fun onProgressChangeAnimationUpdate(
        layout: LinearLayout,
        current: Float,
        to: Float
    ) {
    }

    protected open fun onProgressChangeAnimationEnd(layout: LinearLayout) {}

    protected open fun stopProgressAnimationImmediately() {
        clearProgressAnimation()
        clearSecondaryProgressAnimation()
        if (_isAnimationEnabled && _isProgressAnimating) {
            disableAnimation()
            if (_isProgressAnimating) {
                super.setProgress(_lastProgress)
            }
            if (_isSecondaryProgressAnimating) {
                super.setSecondaryProgress(_lastProgress)
            }
            enableAnimation()
        }
    }

    private fun getAnimationDuration(from: Float, to: Float, max: Float, scale: Float): Long {
        val diff = abs(from - to)
        return (diff * DEFAULT_DURATION / max * scale).toLong()
    }

    private fun startProgressAnimation(from: Float, to: Float) {
        _isProgressAnimating = true
        _progressAnimator?.apply {
            removeUpdateListener(progressAnimationUpdateListener)
            removeListener(progressAnimationAdapterListener)
            cancel()
        }
        _progressAnimator = ValueAnimator.ofFloat(from, to).apply {
            duration = getAnimationDuration(from, to, _max, _animationSpeedScale)
            addUpdateListener(progressAnimationUpdateListener)
            addListener(progressAnimationAdapterListener)
            start()
        }
    }

    private val progressAnimationUpdateListener = AnimatorUpdateListener { animation ->
        onUpdateProgressByAnimation(animation.animatedValue as Float)
    }

    private val progressAnimationAdapterListener: AnimatorListenerAdapter =
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                _isProgressAnimating = false
                onProgressAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {
                _isProgressAnimating = false
            }
        }

    private fun onUpdateProgressByAnimation(progress: Float) {
        super.setProgress(progress)
        onProgressChangeAnimationUpdate(layoutProgress, progress, _lastProgress)
    }

    private fun onProgressAnimationEnd() {
        onProgressChangeAnimationEnd(layoutProgress)
    }

    private fun restoreProgressAnimationState() {
        if (_isProgressAnimating) {
            startProgressAnimation(super.getProgress(), _lastProgress)
        }
    }

    private fun clearProgressAnimation() {
        if (_progressAnimator?.isRunning == true) {
            _progressAnimator?.cancel()
        }
    }

    private fun startSecondaryProgressAnimation(from: Float, to: Float) {
        _isSecondaryProgressAnimating = true
        _secondaryProgressAnimator?.apply {
            removeUpdateListener(secondaryProgressAnimationUpdateListener)
            removeListener(secondaryProgressAnimationAdapterListener)
            cancel()
        }
        _secondaryProgressAnimator = ValueAnimator.ofFloat(from, to).apply {
            duration = getAnimationDuration(from, to, _max, _animationSpeedScale)
            addUpdateListener(secondaryProgressAnimationUpdateListener)
            addListener(secondaryProgressAnimationAdapterListener)
            start()
        }
    }

    private val secondaryProgressAnimationUpdateListener = AnimatorUpdateListener { animation ->
        onUpdateSecondaryProgressByAnimation(animation.animatedValue as Float)
    }

    private val secondaryProgressAnimationAdapterListener: AnimatorListenerAdapter =
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                _isSecondaryProgressAnimating = false
                onSecondaryProgressAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {
                _isSecondaryProgressAnimating = false
            }
        }

    private fun onUpdateSecondaryProgressByAnimation(progress: Float) {
        super.setSecondaryProgress(progress)
        onProgressChangeAnimationUpdate(layoutSecondaryProgress, progress, _lastProgress)
    }

    private fun onSecondaryProgressAnimationEnd() {
        onProgressChangeAnimationEnd(layoutSecondaryProgress)
    }

    private fun restoreSecondaryProgressAnimationState() {
        if (_isSecondaryProgressAnimating) {
            startSecondaryProgressAnimation(super.getSecondaryProgress(), _lastSecondaryProgress)
        }
    }

    private fun clearSecondaryProgressAnimation() {
        if (_secondaryProgressAnimator?.isRunning == true) {
            _secondaryProgressAnimator?.cancel()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState() ?: return null
        val state = SavedState(superState)
        with(state) {
            isProgressAnimating = _isProgressAnimating
            isSecondaryProgressAnimating = _isSecondaryProgressAnimating
            lastProgress = _lastProgress
            lastSecondaryProgress = _lastSecondaryProgress
            animationSpeedScale = _animationSpeedScale
            isAnimationEnabled = _isAnimationEnabled
        }
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        with(state) {
            _isProgressAnimating = isProgressAnimating
            _isSecondaryProgressAnimating = isSecondaryProgressAnimating
            _lastProgress = lastProgress
            _lastSecondaryProgress = lastSecondaryProgress
            _animationSpeedScale = animationSpeedScale
            _isAnimationEnabled = isAnimationEnabled
        }
        restoreProgressAnimationState()
        restoreSecondaryProgressAnimationState()
    }

    protected class SavedState : AbsSavedState {
        var isProgressAnimating = false
        var isSecondaryProgressAnimating = false
        var lastProgress = 0f
        var lastSecondaryProgress = 0f
        var animationSpeedScale = 0f
        var isAnimationEnabled = false

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel) : super(source)

        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            with(source) {
                isProgressAnimating = readByte().toInt() != 0
                isSecondaryProgressAnimating = readByte().toInt() != 0
                lastProgress = readFloat()
                lastSecondaryProgress = readFloat()
                animationSpeedScale = readFloat()
                isAnimationEnabled = readByte().toInt() != 0
            }
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            with(dest) {
                writeByte((if (isProgressAnimating) 1 else 0).toByte())
                writeByte((if (isSecondaryProgressAnimating) 1 else 0).toByte())
                writeFloat(lastProgress)
                writeFloat(lastSecondaryProgress)
                writeFloat(animationSpeedScale)
                writeByte((if (isAnimationEnabled) 1 else 0).toByte())
            }
        }

        companion object {
            @JvmField
            val CREATOR: ClassLoaderCreator<SavedState> = object : ClassLoaderCreator<SavedState> {
                override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState =
                    SavedState(source, loader)

                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)

                override fun newArray(size: Int): Array<SavedState> = newArray(size)
            }
        }
    }
}
