package com.agilie.agtimepicker.animation

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF

class TrianglePath(val paint: Paint) {

    private companion object CacheForTriangleCanvas {
        private val trianglePath = Path()
    }

    var center = PointF()
    var radius = 0f
    var lockMove: Boolean = true

    fun onDraw(canvas: Canvas) {
        canvas.drawPath(trianglePath, paint)
    }

    fun onActionDown(angle: Float, pullUp: Float) {
        //Draw agg
    }

    fun onActionMove(angle: Float, pullUp: Float) {
        if (lockMove)
            return

    }

    fun createTrianglePath() {
        updateTrianglePath(0f)
    }

    private fun updateTrianglePath(pullUp: Float) {
        trianglePath.reset()

        val length = radius / 10f
        //Top of triangle
        val point1 = PointF(center.x, center.y - 0.9f * radius - pullUp)
        trianglePath.moveTo(point1.x, point1.y)
        //Left corn
        val point2 = PointF(point1.x - length, point1.y + length)
        trianglePath.lineTo(point2.x, point2.y)
        //Right corn
        val point3 = PointF(point1.x + length, point1.y + length)
        trianglePath.lineTo(point3.x, point3.y)

        trianglePath.close()
    }

}