package com.akexorcist.roundcornerprogressbar

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar

@Keep
open class RoundCornerProgressBar : AnimatedRoundCornerProgressBar {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun initLayout(): Int = R.layout.layout_round_corner_progress_bar

    override fun initStyleable(context: Context, attrs: AttributeSet?) {}

    override fun initView() {}

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
        layoutProgress.background = progressDrawable
        val ratio = max / progress
        val progressWidth = ((totalWidth - (padding * 2)) / ratio).toInt()
        val progressParams = layoutProgress.layoutParams as MarginLayoutParams
        progressParams.width = progressWidth
        if (padding + (progressWidth / 2) < radius) {
            val margin = (radius - padding).coerceAtLeast(0) - (progressWidth / 2)
            progressParams.topMargin = margin
            progressParams.bottomMargin = margin
        } else {
            progressParams.topMargin = 0
            progressParams.bottomMargin = 0
        }
        layoutProgress.layoutParams = progressParams
    }

    override fun onViewDraw() {}
}
