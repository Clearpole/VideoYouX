package com.clearpole.videoyoux._assembly

import android.content.Context
import android.graphics.*
import android.graphics.Color.*
import android.util.AttributeSet
import android.view.View

class CurvedCircleView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val circlePaint: Paint = Paint()
    private val curvePaint: Paint = Paint()
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    init {
        circlePaint.color = BLUE
        circlePaint.isAntiAlias = true

        curvePaint.color = WHITE
        curvePaint.isAntiAlias = true
        curvePaint.style = Paint.Style.STROKE
        curvePaint.strokeWidth = 6f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (Math.min(w, h) / 2).toFloat()
        centerX = w.toFloat() / 2
        centerY = h.toFloat() / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制圆形
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // 绘制弧线
        val curvePath = Path()
        val startPointX = centerX - radius
        val startPointY = centerY
        curvePath.moveTo(startPointX, startPointY)

        val control1X = centerX - radius / 2
        val control1Y = centerY - radius / 2
        val control2X = centerX + radius / 2
        val control2Y = centerY - radius / 2
        val curveEndPointX = centerX + radius
        val curveEndPointY = centerY

        curvePath.cubicTo(
            control1X, control1Y,
            control2X, control2Y,
            curveEndPointX, curveEndPointY
        )

        canvas.drawPath(curvePath, curvePaint)
    }
}


