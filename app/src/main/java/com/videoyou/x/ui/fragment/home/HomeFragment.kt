package com.videoyou.x.ui.fragment.home

import android.os.Environment
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.carousel.CarouselLayoutManager
import com.tencent.mmkv.MMKV
import com.videoyou.x.R
import com.videoyou.x._player.Play
import com.videoyou.x._storage.AndroidMediaStore
import com.videoyou.x._utils.MediaUtils
import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainHomeBinding
import com.videoyou.x.ui.fragment.home.model.CarouselModel
import com.videoyou.x.ui.fragment.home.model.FoldersModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : BaseFragment<FragmentMainHomeBinding>() {

    override fun onViewCreate() {
        logicList(binding.homeRv)
        logicListForFolders(binding.homeFoldersRv)
        binding.refresh.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AndroidMediaStore.writeData(requireContext())
                withContext(Dispatchers.Main) {
                    logicList(binding.homeRv)
                    logicListForFolders(binding.homeFoldersRv)
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
                val model = model(data)
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

    private fun model(dataList: MMKV): MutableList<Any> {
        return mutableListOf<Any>().apply {
            val sortedList = dataList.allKeys()!!.sorted()
            val arrayList = arrayListOf<MediaItem>()
            sortedList.reversed().take(10).forEachIndexed { _, s ->
                val items = dataList.decodeString(s)!!
                val list = items.split("\u001A")
                arrayList.add(MediaItem.fromUri(list[1].toUri()))
                Play.list = arrayList
                val load = Glide.with(requireContext())
                    .setDefaultRequestOptions(RequestOptions().frame(1000000)).load(list[0])
                    .transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).centerCrop().override(500)
                add(CarouselModel(load, list))
            }
        }
    }

    private fun logicListForFolders(rv: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            refreshMediaData().apply {
                val data = AndroidMediaStore.readFoldersData()
                val model = modelForFolders(data)
                withContext(Dispatchers.Main) {
                    rv.linear().setup {
                        addType<FoldersModel> { R.layout.item_main_folders }
                    }.models = model
                    rv.setHasFixedSize(true)
                }
            }
        }
    }

    private fun modelForFolders(dataList: MMKV): MutableList<Any> {
        return mutableListOf<Any>().apply {
            dataList.allKeys()!!.forEachIndexed { _, s ->
                val titleList = s.split("/")
                val title = titleList[titleList.lastIndex - 1]
                val timeStamp = dataList.decodeString(s)
                val updateTime = millis2String(timeStamp!!.toLong() * 1000)
                add(FoldersModel(title, updateTime))
            }
        }
    }
}