package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.example.R
import kotlinx.android.synthetic.main.fragment_text.*

class TextFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment = TextFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_text, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewText1.text = getText1Description()
        textViewText2.text = getText2Description()
        textViewText3.text = getText3Description()
        textViewText4.text = getText4Description()
        textViewText5.text = getText5Description()
        buttonTextCustomIncrease.setOnClickListener { increaseCustomProgress() }
        buttonTextCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
        buttonTextCustomDecrease.setOnClickListener { decreaseCustomProgress() }
        buttonTextCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
        checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked -> onAnimationEnableCheckdChange(isChecked) }
        progressBarTextCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
            if (isPrimaryProgress) {
                updateCustomProgressText()
            }
        }
        radioButtonInsideGravityStart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateInsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_START)
            }
        }
        radioButtonInsideGravityEnd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateInsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_END)
            }
        }
        radioButtonOutsideGravityStart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateOutsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_START)
            }
        }
        radioButtonOutsideGravityEnd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateOutsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_END)
            }
        }
        radioButtonTextPositionPriorityInside.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateTextPositionPriorityCustomProgress(TextRoundCornerProgressBar.PRIORITY_INSIDE)
            }
        }
        radioButtonTextPositionPriorityOutside.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateTextPositionPriorityCustomProgress(TextRoundCornerProgressBar.PRIORITY_OUTSIDE)
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckdChange(isChecked: Boolean) {
        if (isChecked) {
            progressBarTextCustom.enableAnimation()
        } else {
            progressBarTextCustom.disableAnimation()
        }
    }

    private fun increaseCustomProgress() {
        progressBarTextCustom.progress = progressBarTextCustom.progress + 2
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        progressBarTextCustom.progress = progressBarTextCustom.progress + 20
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        progressBarTextCustom.progress = progressBarTextCustom.progress - 2
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        progressBarTextCustom.progress = progressBarTextCustom.progress - 20
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        progressBarTextCustom.secondaryProgress = progressBarTextCustom.progress + 10
    }

    private fun updateInsideTextGravityCustomProgress(gravity: Int) {
        progressBarTextCustom.textInsideGravity = gravity
    }

    private fun updateOutsideTextGravityCustomProgress(gravity: Int) {
        progressBarTextCustom.textOutsideGravity = gravity
    }

    private fun updateTextPositionPriorityCustomProgress(priority: Int) {
        progressBarTextCustom.textPositionPriority = priority
    }

    private fun updateCustomProgressText() {
        progressBarTextCustom.progressText = String.format("Point : %.0f", progressBarTextCustom.progress)
    }

    private fun getText1Description() = """
    |Max : 100
    |Progress : 50
    |Radius : 0dp
    |Padding : 4dp
    |Width : 250dp
    |Height : 30dp
    """.trimMargin()

    private fun getText2Description() = """
    |Max : 100
    |Progress : 40
    |SecondaryProgress : 60
    |Radius : 10dp
    |Padding : 2dp
    |Text Inside Gravity : End
    |Width : 250dp
    |Height : 30dp
    """.trimMargin()

    private fun getText3Description() = """
    |Max : 100
    |Progress : 20
    |SecondaryProgress : 75
    |Radius : 80dp
    |Padding : 2dp
    |Reverse : True
    |Text Position Priority : Outside
    |Width : 250dp
    |Height : 30dp
    """.trimMargin()

    private fun getText4Description() = """
    |Max : 100
    |Progress : 80
    |Radius : 20dp
    |Padding : 2dp
    |Text Size : 12sp
    |Width : 250dp
    |Height : 20dp
    """.trimMargin()

    private fun getText5Description() = """
    |Max : 200
    |Progress : 20
    |Radius : 20dp
    |Padding : 10dp
    |Width : 250dp
    |Height : 50dp
    """.trimMargin()
}