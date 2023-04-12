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
abstract class AnimatedRoundCornerProgressBar2 : BaseRoundCornerProgressBar2 {
    companion object {
        const val DEFAULT_DURATION = 500L
    }

    private var isProgressAnimating = false
    private var isSecondaryProgressAnimating = false
    private var lastProgress = 0f
    private var lastSecondaryProgress = 0f

    private var animationSpeedScale = 1f
    private var isAnimationEnabled = false

    private var progressAnimator: ValueAnimator? = null
    private var secondaryProgressAnimator: ValueAnimator? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun setupStyleable(context: Context, attrs: AttributeSet?) {
        super.setupStyleable(context, attrs)
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedRoundCornerProgressBar)
        isAnimationEnabled = typedArray.getBoolean(R.styleable.AnimatedRoundCornerProgressBar_rcAnimationEnable, false)
        animationSpeedScale = typedArray.getFloat(R.styleable.AnimatedRoundCornerProgressBar_rcAnimationSpeedScale, 1f)

        typedArray.recycle()

        lastProgress = super.getProgress()
        lastSecondaryProgress = super.getSecondaryProgress()
    }

    override fun getProgress(): Float {
        return if (!isAnimationEnabled && !isProgressAnimating) {
            super.getProgress()
        } else {
            lastProgress
        }
    }

    override fun setProgress(progress: Int) {
        setProgress(progress.toFloat())
    }

    override fun setProgress(progress: Float) {
        val actualProgress =
            if (progress < 0) 0f
            else progress.coerceAtMost(max)

        clearProgressAnimation()
        this.lastProgress = actualProgress

        if (this.isAnimationEnabled) {
            startProgressAnimation(super.getProgress(), actualProgress)
        } else {
            super.setProgress(actualProgress)
        }
    }

    override fun getSecondaryProgress(): Float {
        return if (!isAnimationEnabled && !isSecondaryProgressAnimating) {
            super.getSecondaryProgress()
        } else {
            lastSecondaryProgress
        }
    }

    override fun setSecondaryProgress(progress: Int) {
        setSecondaryProgress(progress.toFloat())
    }

    override fun setSecondaryProgress(progress: Float) {
        val actualProgress =
            if (progress < 0) 0f
            else progress.coerceAtMost(max)
        clearSecondaryProgressAnimation()
        lastSecondaryProgress = actualProgress
        if (isAnimationEnabled) {
            startSecondaryProgressAnimation(super.getSecondaryProgress(), actualProgress)
        } else {
            super.setSecondaryProgress(actualProgress)
        }
    }

    @FloatRange(from = 0.2, to = 5.toDouble())
    open fun getAnimationSpeedScale(): Float {
        return animationSpeedScale
    }

    fun enableAnimation() {
        isAnimationEnabled = true
    }

    fun disableAnimation() {
        isAnimationEnabled = false
    }

    fun setAnimationSpeedScale(@FloatRange(from = 0.2, to = 5.toDouble()) scale: Float) {
        animationSpeedScale = scale.coerceIn(0.2f, 5f)
    }

    fun isProgressAnimating(): Boolean = isProgressAnimating

    fun isSecondaryProgressAnimating(): Boolean = isSecondaryProgressAnimating

    protected open fun onProgressChangeAnimationUpdate(layout: LinearLayout?, current: Float, to: Float) {}

    protected open fun onProgressChangeAnimationEnd(layout: LinearLayout?) {}

    protected open fun stopProgressAnimationImmediately() {
        clearProgressAnimation()
        clearSecondaryProgressAnimation()
        if (isAnimationEnabled && isProgressAnimating) {
            disableAnimation()
            if (isProgressAnimating) {
                super.setProgress(lastProgress)
            }
            if (isSecondaryProgressAnimating) {
                super.setSecondaryProgress(lastProgress)
            }
            enableAnimation()
        }
    }

    private fun getAnimationDuration(from: Float, to: Float, max: Float, scale: Float): Long {
        val diff = abs(from - to)
        return (diff * DEFAULT_DURATION / max * scale).toLong()
    }

    private fun startProgressAnimation(from: Float, to: Float) {
        isProgressAnimating = true
        progressAnimator?.apply {
            removeUpdateListener(progressAnimationUpdateListener)
            removeListener(progressAnimationAdapterListener)
            cancel()
        }
        progressAnimator = ValueAnimator.ofFloat(from, to).apply {
            duration = getAnimationDuration(from, to, max, animationSpeedScale)
            addUpdateListener(progressAnimationUpdateListener)
            addListener(progressAnimationAdapterListener)
            start()
        }
    }

    private val progressAnimationUpdateListener = AnimatorUpdateListener { animation ->
        onUpdateProgressByAnimation(animation.animatedValue as Float)
    }

    private val progressAnimationAdapterListener: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            isProgressAnimating = false
            onProgressAnimationEnd()
        }

        override fun onAnimationCancel(animation: Animator) {
            isProgressAnimating = false
        }
    }

    private fun onUpdateProgressByAnimation(progress: Float) {
        super.setProgress(progress)
        onProgressChangeAnimationUpdate(layoutProgress, progress, lastProgress)
    }

    private fun onProgressAnimationEnd() {
        onProgressChangeAnimationEnd(layoutProgress)
    }

    private fun restoreProgressAnimationState() {
        if (isProgressAnimating) {
            startProgressAnimation(super.getProgress(), lastProgress)
        }
    }

    private fun clearProgressAnimation() {
        if (progressAnimator?.isRunning == true) {
            progressAnimator?.cancel()
        }
    }

    private fun startSecondaryProgressAnimation(from: Float, to: Float) {
        isSecondaryProgressAnimating = true
        secondaryProgressAnimator?.apply {
            removeUpdateListener(secondaryProgressAnimationUpdateListener)
            removeListener(secondaryProgressAnimationAdapterListener)
            cancel()
        }
        secondaryProgressAnimator = ValueAnimator.ofFloat(from, to).apply {
            setDuration(getAnimationDuration(from, to, max, animationSpeedScale))
            addUpdateListener(secondaryProgressAnimationUpdateListener)
            addListener(secondaryProgressAnimationAdapterListener)
            start()
        }
    }

    private val secondaryProgressAnimationUpdateListener = AnimatorUpdateListener { animation ->
        onUpdateSecondaryProgressByAnimation(animation.animatedValue as Float)
    }

    private val secondaryProgressAnimationAdapterListener: AnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            isSecondaryProgressAnimating = false
            onSecondaryProgressAnimationEnd()
        }

        override fun onAnimationCancel(animation: Animator) {
            isSecondaryProgressAnimating = false
        }
    }

    private fun onUpdateSecondaryProgressByAnimation(progress: Float) {
        super.setSecondaryProgress(progress)
        onProgressChangeAnimationUpdate(layoutSecondaryProgress, progress, lastProgress)
    }

    private fun onSecondaryProgressAnimationEnd() {
        onProgressChangeAnimationEnd(layoutSecondaryProgress)
    }

    private fun restoreSecondaryProgressAnimationState() {
        if (isSecondaryProgressAnimating) {
            startSecondaryProgressAnimation(super.getSecondaryProgress(), lastSecondaryProgress)
        }
    }

    private fun clearSecondaryProgressAnimation() {
        if (secondaryProgressAnimator?.isRunning == true) {
            secondaryProgressAnimator?.cancel()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState() ?: return null
        val state = SavedState(superState)
        state.isProgressAnimating = this.isProgressAnimating
        state.isSecondaryProgressAnimating = this.isSecondaryProgressAnimating
        state.lastProgress = this.lastProgress
        state.lastSecondaryProgress = this.lastSecondaryProgress
        state.animationSpeedScale = this.animationSpeedScale
        state.isAnimationEnabled = this.isAnimationEnabled
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        this.isProgressAnimating = state.isProgressAnimating
        this.isSecondaryProgressAnimating = state.isSecondaryProgressAnimating
        this.lastProgress = state.lastProgress
        this.lastSecondaryProgress = state.lastSecondaryProgress
        this.animationSpeedScale = state.animationSpeedScale
        this.isAnimationEnabled = state.isAnimationEnabled
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
            isProgressAnimating = source.readByte().toInt() != 0
            isSecondaryProgressAnimating = source.readByte().toInt() != 0
            lastProgress = source.readFloat()
            lastSecondaryProgress = source.readFloat()
            animationSpeedScale = source.readFloat()
            isAnimationEnabled = source.readByte().toInt() != 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeByte((if (isProgressAnimating) 1 else 0).toByte())
            dest.writeByte((if (isSecondaryProgressAnimating) 1 else 0).toByte())
            dest.writeFloat(lastProgress)
            dest.writeFloat(lastSecondaryProgress)
            dest.writeFloat(animationSpeedScale)
            dest.writeByte((if (isAnimationEnabled) 1 else 0).toByte())
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
