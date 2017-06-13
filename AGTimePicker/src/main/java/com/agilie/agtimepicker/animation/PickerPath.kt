package com.agilie.agtimepicker.animation

import android.graphics.*


class PickerPath(val paint: Paint) {

    private companion object CacheForPickerCanvas {
        private val pickerPath = Path()
    }

    var center = PointF()
    var radius = 0f

    fun onDraw(canvas: Canvas) {
        val matrix = Matrix()
        matrix.setRotate(90f, center.x, center.y)
        pickerPath.reset()
        updatePickerPath()
        pickerPath.transform(matrix)
        canvas.drawPath(pickerPath, paint)

    }

    fun onActionDown(angle: Int) {
        //movablePoint = getPointOnBorderLineOfCircle(center, radius * 1.5f, angle)
    }


    fun updatePickerPath() {

        val controlDelta = radius * 0.552f
        val offset = 100f

        val startPoint = PointF(center.x + radius + offset, center.y)
        pickerPath.moveTo(startPoint.x, startPoint.y)

        var controlPoint1 = PointF(startPoint.x, center.y + controlDelta)
        var controlPoint2 = PointF(center.x + controlDelta, center.y + radius)

        val point2 = PointF(center.x, center.y + radius)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point2.x, point2.y)

        controlPoint1 = PointF(center.x - controlDelta, point2.y)
        controlPoint2 = PointF(center.x - radius, center.y + controlDelta)

        val point3 = PointF(center.x - radius, center.y)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point3.x, point3.y)

        controlPoint1 = PointF(point3.x, center.y - controlDelta)
        controlPoint2 = PointF(center.x - controlDelta, center.y - radius)

        val point4 = PointF(center.x, center.y - radius)
        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point4.x, point4.y)

        controlPoint1 = PointF(center.x + controlDelta, center.y - radius)
        controlPoint2 = PointF(center.x + radius + offset, center.y - controlDelta)

        pickerPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, startPoint.x, startPoint.y)

        pickerPath.close()
    }
}

