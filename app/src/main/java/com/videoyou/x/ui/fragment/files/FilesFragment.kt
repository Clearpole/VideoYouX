package com.videoyou.x.ui.fragment.files

import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainFilesBinding
import com.videoyou.x.ui.fragment.files.model.FilesViewModel

class FilesFragment:BaseFragment<FilesViewModel,FragmentMainFilesBinding>() {
    override fun onViewCreate() {

    }

    override fun getViewBinding(): FragmentMainFilesBinding {
        return FragmentMainFilesBinding.inflate(layoutInflater)
    }
}