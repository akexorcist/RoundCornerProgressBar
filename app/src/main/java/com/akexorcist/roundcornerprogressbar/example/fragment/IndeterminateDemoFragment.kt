package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.example.databinding.FragmentIndeterminateDemoBinding

class IndeterminateDemoFragment : Fragment() {
    private lateinit var binding: FragmentIndeterminateDemoBinding

    companion object {
        fun newInstance(): Fragment = IndeterminateDemoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIndeterminateDemoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewIndeterminate1.text = getIndeterminate1Description()
        binding.textViewIndeterminate2.text = getIndeterminate2Description()
        binding.textViewIndeterminate3.text = getIndeterminate3Description()
        binding.textViewIndeterminate4.text = getIndeterminate4Description()
    }

    private fun getIndeterminate1Description() = """
    |Animation Speed Scale : x1
    """.trimMargin()

    private fun getIndeterminate2Description() = """
    |Animation Speed Scale : x3
    """.trimMargin()

    private fun getIndeterminate3Description() = """
    |Animation Speed Scale : x0.5
    """.trimMargin()

    private fun getIndeterminate4Description() = """
    |Animation Speed Scale : x1
    """.trimMargin()
}
