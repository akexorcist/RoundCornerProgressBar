package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.example.R
import com.akexorcist.roundcornerprogressbar.example.databinding.FragmentSimpleDemoBinding

class SimpleDemoFragment : Fragment() {
    private lateinit var binding: FragmentSimpleDemoBinding

    companion object {
        fun newInstance(): Fragment = SimpleDemoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSimpleDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewSimple1.text = getSimple1Description()
        binding.textViewSimple2.text = getSimple2Description()
        binding.textViewSimple3.text = getSimple3Description()
        binding.textViewSimple4.text = getSimple4Description()
        binding.textViewSimple5.text = getSimple5Description()
        binding.buttonSimpleCustomIncrease.setOnClickListener { increaseCustomProgress() }
        binding.buttonSimpleCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
        binding.buttonSimpleCustomDecrease.setOnClickListener { decreaseCustomProgress() }
        binding.buttonSimpleCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
        binding.checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked -> onAnimationEnableCheckedChange(isChecked) }
        binding.checkBoxGradientProgressColor.setOnCheckedChangeListener { _, isChecked -> onApplyGradientProgressColorCheckedChange(isChecked) }
        binding.progressBarSimpleCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
            if (isPrimaryProgress) {
                updateCustomProgressText()
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckedChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarSimpleCustom.enableAnimation()
        } else {
            binding.progressBarSimpleCustom.disableAnimation()
        }
    }

    private fun onApplyGradientProgressColorCheckedChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarSimpleCustom.progressColors = resources.getIntArray(R.array.sample_progress_gradient)
        } else {
            @Suppress("DEPRECATION")
            binding.progressBarSimpleCustom.progressColor = resources.getColor(R.color.sample_progress_primary)
        }
    }

    private fun increaseCustomProgress() {
        binding.progressBarSimpleCustom.progress = binding.progressBarSimpleCustom.progress + 2
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        binding.progressBarSimpleCustom.progress = binding.progressBarSimpleCustom.progress + 20
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        binding.progressBarSimpleCustom.progress = binding.progressBarSimpleCustom.progress - 2
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        binding.progressBarSimpleCustom.progress = binding.progressBarSimpleCustom.progress - 20
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        binding.progressBarSimpleCustom.secondaryProgress = binding.progressBarSimpleCustom.progress + 10
    }

    private fun updateCustomProgressText() {
        binding.textViewSimpleCustom.text = String.format("%.0f", binding.progressBarSimpleCustom.progress)
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
