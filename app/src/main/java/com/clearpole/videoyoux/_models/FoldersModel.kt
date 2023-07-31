package com.clearpole.videoyoux._models

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux.R
import com.clearpole.videoyoux._utils.ReadMediaStore
import com.clearpole.videoyoux.databinding.ItemFoldersBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

open class FoldersModel(
    private val title: String,
    private val timeStamp: String,
    private val foldersRv: RecyclerView,
    private val videosRv: RecyclerView,
    private val toolbar: SubtitleCollapsingToolbarLayout
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        CoroutineScope(Dispatchers.IO).launch {
            val count =
                ReadMediaStore.readVideosData().decodeString(title)!!.split("\u001A\u001A").size
            withContext(Dispatchers.Main) {
                holder.getBinding<ItemFoldersBinding>().apply {
                    val titleClip = title.split("/").let { it[it.size - 2] }
                    foldersTitle.text = titleClip
                    foldersInfo.text = TimeUtils.millis2String(
                        timeStamp.toLong() * 1000,
                        SimpleDateFormat("yy.MM.dd - HH:mm")
                    ) + " • " + count + holder.context.getString(R.string.videos)
                    more.setOnClickListener {
                        showMenu(it, R.menu.folders_menu, holder.context)
                    }
                    root.setOnClickListener {
                        toolbar.title = titleClip
                        toolbar.subtitle = holder.context.getString(R.string.all_videos_count,count)
                        foldersRv.visibility = View.GONE
                        videosRv.visibility = View.VISIBLE
                    }
                }
            }
            cancel()
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, context: Context) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            true
        }
        popup.show()
    }
}