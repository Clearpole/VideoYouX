package com.videoyou.x.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.videoyou.x.R
import com.videoyou.x.databinding.FoldersSheetBinding
import com.videoyou.x.storage.AndroidMediaStore
import com.videoyou.x.ui.fragment.home.model.VideosModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModalBottomSheet(private val folderPath: String) : BottomSheetDialogFragment() {
    private lateinit var binding: FoldersSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FoldersSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            val model = models()
            withContext(Dispatchers.Main) {
                binding.videosRv.linear().setup {
                    addType<VideosModel> { R.layout.item_videos_item }
                }.models = model
            }
        }
    }

    private fun models(): MutableList<Any> =
        mutableListOf<Any>().apply {
            val kv = AndroidMediaStore.readFoldersVideosData()
            kv.allKeys()!!.forEach {
                if (it.contains(folderPath)) {
                    val data = kv.decodeString(it)!!.split("\u001A")
                    add(VideosModel(data[0], data[3], data[4]))
                }
            }
        }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}
