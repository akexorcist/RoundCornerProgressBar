package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.example.R
import kotlinx.android.synthetic.main.fragment_simple.*

class SimpleFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment = SimpleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_simple, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewSimple1.text = getSimple1Description()
        textViewSimple2.text = getSimple2Description()
        textViewSimple3.text = getSimple3Description()
        textViewSimple4.text = getSimple4Description()
        textViewSimple5.text = getSimple5Description()
        buttonSimpleCustomIncrease.setOnClickListener { increaseCustomProgress() }
        buttonSimpleCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
        buttonSimpleCustomDecrease.setOnClickListener { decreaseCustomProgress() }
        buttonSimpleCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
        checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked -> onAnimationEnableCheckedChange(isChecked) }
        checkBoxGradientProgressColor.setOnCheckedChangeListener { _, isChecked -> onApplyGradientProgressColorCheckedChange(isChecked) }
        progressBarSimpleCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
            if (isPrimaryProgress) {
                updateCustomProgressText()
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckedChange(isChecked: Boolean) {
        if (isChecked) {
            progressBarSimpleCustom.enableAnimation()
        } else {
            progressBarSimpleCustom.disableAnimation()
        }
    }

    private fun onApplyGradientProgressColorCheckedChange(isChecked: Boolean) {
        if (isChecked) {
            progressBarSimpleCustom.progressColors = resources.getIntArray(R.array.sample_progress_gradient)
        } else {
            @Suppress("DEPRECATION")
            progressBarSimpleCustom.progressColor = resources.getColor(R.color.sample_progress_primary)
        }
    }

    private fun increaseCustomProgress() {
        progressBarSimpleCustom.progress = progressBarSimpleCustom.progress + 2
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        progressBarSimpleCustom.progress = progressBarSimpleCustom.progress + 20
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        progressBarSimpleCustom.progress = progressBarSimpleCustom.progress - 2
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        progressBarSimpleCustom.progress = progressBarSimpleCustom.progress - 20
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        progressBarSimpleCustom.secondaryProgress = progressBarSimpleCustom.progress + 10
    }

    private fun updateCustomProgressText() {
        textViewSimpleCustom.text = String.format("%.0f", progressBarSimpleCustom.progress)
    }

    private fun getSimple1Description() = """
    |Max : 100
    |Progress : 50
    |Radius : 0dp
    |Padding : 4dp
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getSimple2Description() = """
    |Max : 100
    |Progress : 40
    |SecondaryProgress : 60
    |Radius : 10dp
    |Padding : 2dp
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getSimple3Description() = """
    |Max : 100
    |Progress : 20
    |SecondaryProgress : 75
    |Radius : 80dp
    |Padding : 2dp
    |Reverse : True
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getSimple4Description() = """
    |Max : 100
    |Progress : 80
    |Radius : 20dp
    |Padding : 2dp
    |Width : 260dp
    |Height : 20dp
    """.trimMargin()

    private fun getSimple5Description() = """
    |Max : 200
    |Progress : 20
    |Radius : 20dp
    |Padding : 10dp
    |Width : 260dp
    |Height : 50dp
    """.trimMargin()
}