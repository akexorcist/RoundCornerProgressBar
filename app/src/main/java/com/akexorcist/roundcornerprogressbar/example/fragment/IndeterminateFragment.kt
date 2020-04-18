package com.akexorcist.roundcornerprogressbar.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.example.R
import kotlinx.android.synthetic.main.fragment_indeterminate.*

class IndeterminateFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment = IndeterminateFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_indeterminate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewIndeterminate1.text = getIndeterminate1Description()
        textViewIndeterminate2.text = getIndeterminate2Description()
        textViewIndeterminate3.text = getIndeterminate3Description()
        textViewIndeterminate4.text = getIndeterminate4Description()
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