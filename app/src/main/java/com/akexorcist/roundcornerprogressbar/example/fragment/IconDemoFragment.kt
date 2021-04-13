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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIconDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewIcon1.text = getIcon1Description()
        binding.textViewIcon2.text = getIcon2Description()
        binding.textViewIcon3.text = getIcon3Description()
        binding.textViewIcon4.text = getIcon4Description()
        binding.textViewIcon5.text = getIcon5Description()
        binding.textViewIcon6.text = getIcon6Description()
        binding.textViewIcon7.text = getIcon7Description()
        binding.buttonIconCustomIncrease.setOnClickListener { increaseCustomProgress() }
        binding.buttonIconCustomExtraIncrease.setOnClickListener { extraIncreaseCustomProgress() }
        binding.buttonIconCustomDecrease.setOnClickListener { decreaseCustomProgress() }
        binding.buttonIconCustomExtraDecrease.setOnClickListener { extraDecreaseCustomProgress() }
        binding.checkBoxAnimationEnable.setOnCheckedChangeListener { _, isChecked -> onAnimationEnableCheckdChange(isChecked) }
        binding.checkBoxGradientProgressColor.setOnCheckedChangeListener { _, isChecked -> onApplyGradientProgressColorCheckedChange(isChecked) }
        binding.progressBarIconCustom.setOnProgressChangedListener { _, _, isPrimaryProgress, _ ->
            if (isPrimaryProgress) {
                updateCustomProgressText()
            }
        }
        updateCustomProgressText()
    }

    private fun onAnimationEnableCheckdChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarIconCustom.enableAnimation()
        } else {
            binding.progressBarIconCustom.disableAnimation()
        }
    }

    private fun onApplyGradientProgressColorCheckedChange(isChecked: Boolean) {
        if (isChecked) {
            binding.progressBarIconCustom.progressColors = resources.getIntArray(R.array.sample_progress_gradient)
        } else {
            @Suppress("DEPRECATION")
            binding.progressBarIconCustom.progressColor = resources.getColor(R.color.sample_progress_primary)
        }
    }

    private fun increaseCustomProgress() {
        binding.progressBarIconCustom.progress = binding.progressBarIconCustom.progress + 2
        updateCustomSecondaryProgress()
    }

    private fun extraIncreaseCustomProgress() {
        binding.progressBarIconCustom.progress = binding.progressBarIconCustom.progress + 20
        updateCustomSecondaryProgress()
    }

    private fun decreaseCustomProgress() {
        binding.progressBarIconCustom.progress = binding.progressBarIconCustom.progress - 2
        updateCustomSecondaryProgress()
    }

    private fun extraDecreaseCustomProgress() {
        binding.progressBarIconCustom.progress = binding.progressBarIconCustom.progress - 20
        updateCustomSecondaryProgress()
    }

    private fun updateCustomSecondaryProgress() {
        binding.progressBarIconCustom.secondaryProgress = binding.progressBarIconCustom.progress + 30
    }

    private fun updateCustomProgressText() {
        binding.textViewIconCustom.text = String.format("%.0f", binding.progressBarIconCustom.progress)
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
