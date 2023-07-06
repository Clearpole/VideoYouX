package com.clearpole.videoyoux._assembly

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.clearpole.videoyoux.R
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class EmptyControlVideo : StandardGSYVideoPlayer {
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId(): Int {
        return R.layout.empty_control_video
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        //不给触摸快进
        mChangePosition = false

        //不给触摸音量
        mChangeVolume = false

        //不给触摸亮度
        mBrightness = false
    }

    override fun touchDoubleUp(e: MotionEvent) {
        //super.touchDoubleUp(e)
        //不需要双击暂停
    }

}