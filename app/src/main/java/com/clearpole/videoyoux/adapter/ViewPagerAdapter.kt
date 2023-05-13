package com.clearpole.videoyoux.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter : PagerAdapter {
    // 定义视图列表
    private var viewList: List<View> = ArrayList()

    // 构造方法传入数据
    constructor(viewList: List<View>) {
        this.viewList = viewList
    }

    constructor()

    //初始化指定位置的页面
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //将页面加入容器
        container.addView(viewList[position])
        //返回加载的页面
        return viewList[position]
    }

    //销毁指定位置的页面
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList[position]) //删除页面
    }

    //显示多少个页面
    override fun getCount(): Int {
        return viewList.size
    }

    //判断view 是否是object
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}