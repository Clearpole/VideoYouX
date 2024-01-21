package com.videoyou.x.ui.fragment.functions

import androidx.recyclerview.widget.LinearLayoutManager
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.videoyou.x.R
import com.videoyou.x.databinding.FragmentMainFunctionsBinding
import com.videoyou.x.storage.Statistics
import com.videoyou.x.ui.fragment.functions.model.OverViewModel
import com.videoyou.x.utils.base.BaseFragment

class FunctionsFragment : BaseFragment<FragmentMainFunctionsBinding>() {
    override fun onViewCreate() {
        binding.overViewRv.linear().setup {
            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            it.layoutManager = layoutManager
            addType<OverViewModel> { R.layout.item_functions_overview }
        }.models = mutableListOf<Any?>().apply {
            add(OverViewModel(getString(R.string.videos_count), Statistics.VIDEOS_COUNT))
            add(OverViewModel(getString(R.string.folders_count), Statistics.FOLDERS_COUNT))
            add(OverViewModel(getString(R.string.total_size), Statistics.VIDEOS_SIZE))
        }
    }

    override fun getViewBinding(): FragmentMainFunctionsBinding {
        return FragmentMainFunctionsBinding.inflate(layoutInflater)
    }
}