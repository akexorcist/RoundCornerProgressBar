package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar
import com.akexorcist.roundcornerprogressbar.example.databinding.FragmentTextDemoBinding

class TextDemoFragment : Fragment() {
    private lateinit var binding: FragmentTextDemoBinding

    companion object {
        fun newInstance(): Fragment = TextDemoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTextDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewText1.text = getText1Description()
        binding.textViewText2.text = getText2Description()
        binding.textViewText3.text = getText3Description()
        binding.textViewText4.text = getText4Description()
        binding.textViewText5.text = getText5Description()
        binding.buttonTextCustomIncrease.setOnClickListener { increaseCustomProgress() }
        binding.buttonTextCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
        binding.buttonTextCustomDecrease.setOnClickListener { decreaseCustomProgress() }
        binding.buttonTextCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
        binding.checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked -> onAnimationEnableCheckdChange(isChecked) }
        binding.progressBarTextCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
            if (isPrimaryProgress) {
                updateCustomProgressText()
            }
        }
        binding.radioButtonInsideGravityStart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateInsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_START)
            }
        }
        binding.radioButtonInsideGravityEnd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateInsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_END)
            }
        }
        binding.radioButtonOutsideGravityStart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateOutsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_START)
            }
        }
        binding.radioButtonOutsideGravityEnd.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateOutsideTextGravityCustomProgress(TextRoundCornerProgressBar.GRAVITY_END)
            }
        }
        binding.radioButtonTextPositionPriorityInside.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateTextPositionPriorityCustomProgress(TextRoundCornerProgressBar.PRIORITY_INSIDE)
            }
        }
        binding.radioButtonTextPositionPriorityOutside.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateTextPositionPriorityCustomProgress(TextRoundCornerProgressBar.PRIORITY_OUTSIDE)
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckdChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarTextCustom.enableAnimation()
        } else {
            binding.progressBarTextCustom.disableAnimation()
        }
    }

    private fun increaseCustomProgress() {
        binding.progressBarTextCustom.progress = binding.progressBarTextCustom.progress + 2
    }

    private fun extraIncreaseCustomProgress() {
        binding.progressBarTextCustom.progress = binding.progressBarTextCustom.progress + 20
    }

    private fun decreaseCustomProgress() {
        binding.progressBarTextCustom.progress = binding.progressBarTextCustom.progress - 2
    }

    private fun extraDecreaseCustomProgress() {
        binding.progressBarTextCustom.progress = binding.progressBarTextCustom.progress - 20
    }

    private fun updateInsideTextGravityCustomProgress(gravity: Int) {
        binding.progressBarTextCustom.textInsideGravity = gravity
    }

    private fun updateOutsideTextGravityCustomProgress(gravity: Int) {
        binding.progressBarTextCustom.textOutsideGravity = gravity
    }

    private fun updateTextPositionPriorityCustomProgress(priority: Int) {
        binding.progressBarTextCustom.textPositionPriority = priority
    }

    private fun updateCustomProgressText() {
        binding.progressBarTextCustom.progressText = String.format("%.0f", binding.progressBarTextCustom.progress)
    }

    private fun getText1Description() = """
    |Max : 100
    |Progress : 50
    |Radius : 0dp
    |Padding : 4dp
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getText2Description() = """
    |Max : 100
    |Progress : 40
    |SecondaryProgress : 60
    |Radius : 10dp
    |Padding : 2dp
    |Text Inside Gravity : End
    |Width : 260dp
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
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getText4Description() = """
    |Max : 100
    |Progress : 80
    |Radius : 20dp
    |Padding : 2dp
    |Text Size : 12sp
    |Width : 260dp
    |Height : 20dp
    """.trimMargin()

    private fun getText5Description() = """
    |Max : 200
    |Progress : 20
    |Radius : 20dp
    |Padding : 10dp
    |Width : 260dp
    |Height : 50dp
    """.trimMargin()
}
