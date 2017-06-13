package com.agilie.agtimepicker.animation

import android.graphics.*


class PickerPath(val paint: Paint) {

    private companion object CacheForPickerCanvas {
        private val pickerPath = Path()
    }

    var lockMove: Boolean = true
    var center = PointF()
    var radius = 0f

    fun onDraw(canvas: Canvas) {
        canvas.drawPath(pickerPath, paint)
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


    fun createPickerPath() {
        updatePickerPath(0f)
    }

    private fun rotatePickerPath(angle: Float) {
        //Rotate agg
        val matrix = Matrix()
        matrix.setRotate(angle, center.x, center.y)
        pickerPath.transform(matrix)
    }

    private fun updatePickerPath(pullUp: Float) {
        pickerPath.reset()

        val controlDelta = radius * 0.552f
        //Draw agg or circle
        val offset = pullUp//radius + pullUp

        val startPoint = PointF(center.x, center.y - radius - offset)
        pickerPath.moveTo(startPoint.x, startPoint.y)

        var controlPoint1 = PointF(center.x + controlDelta, center.y - radius - offset)
        var controlPoint2 = PointF(center.x + radius, center.y - controlDelta)

        val point2 = PointF(center.x + radius, center.y)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point2.x, point2.y)

        controlPoint1 = PointF(center.x + radius, center.y + controlDelta)
        controlPoint2 = PointF(center.x + controlDelta, center.y + radius)

        val point3 = PointF(center.x, center.y + radius)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point3.x, point3.y)

        controlPoint1 = PointF(center.x - controlDelta, center.y + radius)
        controlPoint2 = PointF(center.x - radius, center.y + controlDelta)
        val point4 = PointF(center.x - radius, center.y)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point4.x, point4.y)

        controlPoint1 = PointF(center.x - radius, center.y - controlDelta)
        controlPoint2 = PointF(center.x - controlDelta, center.y - radius - offset)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, startPoint.x, startPoint.y)

        pickerPath.close()
    }

    fun onActionUp() {
        updatePickerPath(0f)
    }


}

