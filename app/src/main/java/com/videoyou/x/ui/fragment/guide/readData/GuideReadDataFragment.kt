package com.videoyou.x.ui.fragment.guide.readData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.videoyou.x.R

class GuideReadDataFragment : Fragment() {

    companion object {
        fun newInstance() = GuideReadDataFragment()
    }

    private lateinit var viewModel: GuideReadDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guide_read_data, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GuideReadDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

}