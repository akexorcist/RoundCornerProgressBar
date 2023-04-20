package com.akexorcist.roundcornerprogressbar.indeterminate

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.akexorcist.roundcornerprogressbar.CenteredRoundCornerProgressBar

@Suppress("unused")
@Keep
open class IndeterminateCenteredRoundCornerProgressBar : CenteredRoundCornerProgressBar {
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

    override fun initView() {
        super.initView()
        setMax(100)
    }

    override fun onProgressChangeAnimationUpdate(layout: LinearLayout, current: Float, to: Float) {
        if (!isShown) {
            super.stopProgressAnimationImmediately()
        }
    }

    override fun onProgressChangeAnimationEnd(layout: LinearLayout) {
        if (isShown) {
            startIndeterminateAnimation()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            startIndeterminateAnimation()
        }
    }

    private fun startIndeterminateAnimation() {
        disableAnimation()
        setProgress(0)
        enableAnimation()
        setProgress(100)
    }
}