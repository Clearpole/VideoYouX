package com.videoyou.x.ui.fragment.main

import android.net.Uri
import android.os.Environment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.carousel.CarouselLayoutManager
import com.tencent.mmkv.MMKV
import com.videoyou.x.R
import com.videoyou.x._storage.AndroidMediaStore
import com.videoyou.x._utils.MediaUtils
import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainHomeBinding
import com.videoyou.x.ui.fragment.main.model.CarouselModel
import com.videoyou.x.ui.fragment.main.model.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : BaseFragment<MainViewModel, FragmentMainHomeBinding>() {

    override fun onViewCreate() {
        logicList(binding.homeRv)
        binding.refresh.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AndroidMediaStore.writeData(requireContext())
                withContext(Dispatchers.Main){
                    logicList(binding.homeRv)
                }
            }
        }
    }

    override fun getViewBinding(): FragmentMainHomeBinding {
        return FragmentMainHomeBinding.inflate(layoutInflater)
    }

    private fun refreshMediaData() {
        MediaUtils.updateMedia(
            requireContext(),
            Environment.getExternalStorageDirectory().toString()
        )
    }


    private fun logicList(rv: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            refreshMediaData().apply {
                val data = AndroidMediaStore.readVideosData()
                val model = model(data, AndroidMediaStore.readFoldersData())
                withContext(Dispatchers.Main) {
                    rv.linear().setup {
                        it.layoutManager = CarouselLayoutManager()
                        addType<CarouselModel> { R.layout.item_main_carousel }
                    }.models = model
                    rv.setHasFixedSize(true)
                }
            }
        }
    }

    private fun model(dataList: MMKV, folderList: MMKV): MutableList<Any> {
        return mutableListOf<Any>().apply {
            val folders = folderList.allKeys()!!.sortedBy { folderList.decodeString(it)!!.toLong() }
                .reversed()
            folders.forEachIndexed { _, s ->
                val items = dataList.decodeString(s)!!
                items.split("\u001A\u001A").forEachIndexed { _, video ->
                    val data = video.split("\u001A")
                    val load = Glide.with(requireContext()).setDefaultRequestOptions(RequestOptions().frame(1000000)).load(data[1])
                        .transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).centerCrop().override(500)
                    add(CarouselModel(load))
                }
            }
        }
    }
}