package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.example.R
import com.akexorcist.roundcornerprogressbar.example.databinding.FragmentCenteredDemoBinding

class CenteredDemoFragment : Fragment() {
    private lateinit var binding: FragmentCenteredDemoBinding

    companion object {
        fun newInstance(): Fragment = CenteredDemoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCenteredDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewCentered1.text = getCentered1Description()
        binding.textViewCentered2.text = getCentered2Description()
        binding.textViewCentered3.text = getCentered3Description()
        binding.textViewCentered4.text = getCentered4Description()
        binding.textViewCentered5.text = getCentered5Description()
        binding.buttonCenteredCustomIncrease.setOnClickListener { increaseCustomProgress() }
        binding.buttonCenteredCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
        binding.buttonCenteredCustomDecrease.setOnClickListener { decreaseCustomProgress() }
        binding.buttonCenteredCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
        binding.checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked -> onAnimationEnableCheckdChange(isChecked) }
        binding.checkBoxGradientProgressColor.setOnCheckedChangeListener { _, isChecked -> onApplyGradientProgressColorCheckedChange(isChecked) }
        binding.progressBarCenteredCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
            if (isPrimaryProgress) {
                updateCustomProgressText()
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckdChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarCenteredCustom.enableAnimation()
        } else {
            binding.progressBarCenteredCustom.disableAnimation()
        }
    }

    private fun onApplyGradientProgressColorCheckedChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarCenteredCustom.progressColors = resources.getIntArray(R.array.sample_progress_gradient)
        } else {
            @Suppress("DEPRECATION")
            binding.progressBarCenteredCustom.progressColor = resources.getColor(R.color.sample_progress_primary)
        }
    }

    private fun increaseCustomProgress() {
        binding.progressBarCenteredCustom.progress = binding.progressBarCenteredCustom.progress + 2
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        binding.progressBarCenteredCustom.progress = binding.progressBarCenteredCustom.progress + 20
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        binding.progressBarCenteredCustom.progress = binding.progressBarCenteredCustom.progress - 2
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        binding.progressBarCenteredCustom.progress = binding.progressBarCenteredCustom.progress - 20
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        binding.progressBarCenteredCustom.secondaryProgress = binding.progressBarCenteredCustom.progress + 10
    }

    private fun updateCustomProgressText() {
        binding.textViewCenteredCustom.text = String.format("%.0f", binding.progressBarCenteredCustom.progress)
    }

    private fun getCentered1Description() = """
    |Max : 100
    |Progress : 50
    |Radius : 0dp
    |Padding : 4dp
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getCentered2Description() = """
    |Max : 100
    |Progress : 40
    |SecondaryProgress : 60
    |Radius : 10dp
    |Radius : 10dp
    |Padding : 2dp
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getCentered3Description() = """
    |Max : 100
    |Progress : 20
    |SecondaryProgress : 75
    |Radius : 80dp
    |Padding : 2dp
    |Reverse : True
    |Width : 260dp
    |Height : 30dp
    """.trimMargin()

    private fun getCentered4Description() = """
    |Max : 100
    |Progress : 80
    |Radius : 20dp
    |Padding : 2dp
    |Width : 260dp
    |Height : 20dp
    """.trimMargin()

    private fun getCentered5Description() = """
    |Max : 200
    |Progress : 20
    |Radius : 20dp
    |Padding : 10dp
    |Width : 260dp
    |Height : 50dp
    """.trimMargin()
}
