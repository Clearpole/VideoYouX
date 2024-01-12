package com.videoyou.x.ui.fragment.files

import android.widget.Toast
import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainFilesBinding
import com.videoyou.x.ui.fragment.files.model.FilesViewModel

class FilesFragment:BaseFragment<FilesViewModel,FragmentMainFilesBinding>() {
    override fun onViewCreate() {
        Toast.makeText(requireContext(), "没闪退？", Toast.LENGTH_SHORT).show()
    }

    override fun getViewBinding(): FragmentMainFilesBinding {
        return FragmentMainFilesBinding.inflate(layoutInflater)
    }
}