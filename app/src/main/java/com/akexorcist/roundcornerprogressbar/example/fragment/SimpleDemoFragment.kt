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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSimpleDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textViewSimple1.text = getSimple1Description()
            textViewSimple2.text = getSimple2Description()
            textViewSimple3.text = getSimple3Description()
            textViewSimple4.text = getSimple4Description()
            textViewSimple5.text = getSimple5Description()
            buttonSimpleCustomIncrease.setOnClickListener { increaseCustomProgress() }
            buttonSimpleCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
            buttonSimpleCustomDecrease.setOnClickListener { decreaseCustomProgress() }
            buttonSimpleCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
            checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked ->
                onAnimationEnableCheckedChange(
                    isChecked
                )
            }
            checkBoxGradientProgressColor.setOnCheckedChangeListener { _, isChecked ->
                onApplyGradientProgressColorCheckedChange(
                    isChecked
                )
            }
            progressBarSimpleCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
                if (isPrimaryProgress) {
                    updateCustomProgressText()
                }
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckedChange(isChecked: Boolean) {
        with(binding) {
            if (isChecked) {
                progressBarSimpleCustom.enableAnimation()
            } else {
                progressBarSimpleCustom.disableAnimation()
            }
        }
    }

    private fun onApplyGradientProgressColorCheckedChange(isChecked: Boolean) {
        with(binding) {
            if (isChecked) {
                progressBarSimpleCustom.setProgressColors(resources.getIntArray(R.array.sample_progress_gradient))
            } else {
                @Suppress("DEPRECATION")
                progressBarSimpleCustom.setProgressColor(resources.getColor(R.color.sample_progress_primary))
            }
        }
    }

    private fun increaseCustomProgress() {
        with(binding) {
            progressBarSimpleCustom.setProgress(progressBarSimpleCustom.getProgress() + 2)
        }
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        with(binding) {
            progressBarSimpleCustom.setProgress(progressBarSimpleCustom.getProgress() + 20)
        }
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        with(binding) {
            progressBarSimpleCustom.setProgress(progressBarSimpleCustom.getProgress() - 2)
        }
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        with(binding) {
            progressBarSimpleCustom.setProgress(progressBarSimpleCustom.getProgress() - 20)
        }
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        with(binding) {
            progressBarSimpleCustom.setSecondaryProgress(progressBarSimpleCustom.getProgress() + 10)
        }
    }

    private fun updateCustomProgressText() {
        with(binding) {
            textViewSimpleCustom.text = "${progressBarSimpleCustom.getProgress()}"
        }
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
