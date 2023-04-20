package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.example.R
import com.akexorcist.roundcornerprogressbar.example.databinding.FragmentIconDemoBinding

class IconDemoFragment : Fragment() {
    private lateinit var binding: FragmentIconDemoBinding

    companion object {
        fun newInstance(): Fragment = IconDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIconDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textViewIcon1.text = getIcon1Description()
            textViewIcon2.text = getIcon2Description()
            textViewIcon3.text = getIcon3Description()
            textViewIcon4.text = getIcon4Description()
            textViewIcon5.text = getIcon5Description()
            textViewIcon6.text = getIcon6Description()
            textViewIcon7.text = getIcon7Description()
            buttonIconCustomIncrease.setOnClickListener { increaseCustomProgress() }
            buttonIconCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
            buttonIconCustomDecrease.setOnClickListener { decreaseCustomProgress() }
            buttonIconCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
            checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked ->
                onAnimationEnableCheckdChange(
                    isChecked
                )
            }
            checkBoxGradientProgressColor.setOnCheckedChangeListener { _, isChecked ->
                onApplyGradientProgressColorCheckedChange(
                    isChecked
                )
            }
            progressBarIconCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
                if (isPrimaryProgress) {
                    updateCustomProgressText()
                }
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckdChange(isChecked: Boolean) {
        with(binding) {
            if (isChecked) {
                progressBarIconCustom.enableAnimation()
            } else {
                progressBarIconCustom.disableAnimation()
            }
        }
    }

    private fun onApplyGradientProgressColorCheckedChange(isChecked: Boolean) {
        with(binding) {
            if (isChecked) {
                progressBarIconCustom.setProgressColors(resources.getIntArray(R.array.sample_progress_gradient))
            } else {
                @Suppress("DEPRECATION")
                progressBarIconCustom.setProgressColor(resources.getColor(R.color.sample_progress_primary))
            }
        }
    }

    private fun increaseCustomProgress() {
        with(binding) {
            progressBarIconCustom.setProgress(progressBarIconCustom.getProgress() + 2)
        }
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        with(binding) {
            progressBarIconCustom.setProgress(progressBarIconCustom.getProgress() + 20)
        }
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        with(binding) {
            progressBarIconCustom.setProgress(progressBarIconCustom.getProgress() - 2)
        }
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        with(binding) {
            progressBarIconCustom.setProgress(progressBarIconCustom.getProgress() - 20)
        }
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        with(binding) {
            progressBarIconCustom.setSecondaryProgress(progressBarIconCustom.getProgress() + 30)
        }
    }

    private fun updateCustomProgressText() {
        with(binding) {
            textViewIconCustom.text = "${progressBarIconCustom.getProgress()}"
        }
    }

    private fun getIcon1Description() = """
    |Max : 150
    |Progress : 90
    |Icon Size : 40dp
    |Icon Padding : 5dp
    |Radius : 5dp
    |Background Padding : 2dp
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()

    private fun getIcon2Description() = """
    |Max : 150
    |Progress : 90
    |Icon Size : 40dp
    |Icon Padding : 5dp
    |Radius : 5dp
    |Background Padding : 2dp
    |Reverse : True
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()

    private fun getIcon3Description() = """
    |Max : 150
    |Progress : 50
    |Secondary Progress : 80
    |Icon Size : 25dp
    |Icon Padding : 5dp
    |Radius : 5dp
    |Background Padding : 5dp
    |Reverse : True
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()

    private fun getIcon4Description() = """
    |Max : 150
    |Progress : 40
    |Icon Size : 70dp
    |Icon Padding : 5dp
    |Radius : 5dp
    |Background Padding : 10dp
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()

    private fun getIcon5Description() = """
    |Max : 150
    |Progress : 150
    |Icon Size : 30dp
    |Icon Padding : 3dp
    |Radius : 5dp
    |Background Padding : 5dp
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()

    private fun getIcon6Description() = """
    |Max : 150
    |Progress : 5
    |Icon Size : 50dp
    |Icon Padding : 3dp
    |Radius : 20dp
    |Background Padding : 10dp
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()

    private fun getIcon7Description() = """
    |No Icon Image
    |Max : 150
    |Progress : 100
    |Icon Size : 20dp
    |Icon Padding : 10dp
    |Radius : 30dp
    |Background Padding : 5dp
    |Width : 260dp
    |Height : Wrap Content
    """.trimMargin()
}
