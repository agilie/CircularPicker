package com.agilie.agtimepicker.animation

import android.graphics.*

class MinutesPickerPath(val paint: Paint) {

    private companion object CacheForPickerCanvas {
        private val minutesPath = Path()
    }

    var lockMove: Boolean = true
    var center = PointF()
    var radius = 0f

    fun onDraw(canvas: Canvas) {
        canvas.drawPath(minutesPath, paint)
    }

    fun onActionDown(angle: Float, pullUp: Float) {
        //Draw agg
        updatePickerPath(pullUp)
        rotatePickerPath(angle)
    }

    fun onActionMove(angle: Float, pullUp: Float) {
        if (lockMove)
            return

        updatePickerPath(pullUp)
        rotatePickerPath(angle)
    }

    fun onActionUp() {
        updatePickerPath(0f)
    }

    fun createPickerPath() {
        updatePickerPath(0f)
    }

    private fun rotatePickerPath(angle: Float) {
        //Rotate agg
        val matrix = Matrix()
        matrix.setRotate(angle, center.x, center.y)
        minutesPath.transform(matrix)
    }

    private fun updatePickerPath(pullUp: Float) {
        minutesPath.reset()

        val controlDelta = radius * 0.552f
        //Draw agg or circle
        val offset = pullUp//radius + pullUp

        val startPoint = PointF(center.x, center.y - radius - offset)
        minutesPath.moveTo(startPoint.x, startPoint.y)

        var controlPoint1 = PointF(center.x + controlDelta, center.y - radius - offset)
        var controlPoint2 = PointF(center.x + radius, center.y - controlDelta)

        val point2 = PointF(center.x + radius, center.y)
        minutesPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point2.x, point2.y)

        controlPoint1 = PointF(center.x + radius, center.y + controlDelta)
        controlPoint2 = PointF(center.x + controlDelta, center.y + radius)

        val point3 = PointF(center.x, center.y + radius)
        minutesPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point3.x, point3.y)

        controlPoint1 = PointF(center.x - controlDelta, center.y + radius)
        controlPoint2 = PointF(center.x - radius, center.y + controlDelta)
        val point4 = PointF(center.x - radius, center.y)
        minutesPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point4.x, point4.y)

        controlPoint1 = PointF(center.x - radius, center.y - controlDelta)
        controlPoint2 = PointF(center.x - controlDelta, center.y - radius - offset)
        minutesPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, startPoint.x, startPoint.y)

        minutesPath.close()
    }
}