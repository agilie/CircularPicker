package com.agilie.agtimepicker.ui.animation

import android.graphics.*


class PickerPath(val pickerPaint: Paint,
                 val trianglePaint: Paint) {

    private val path = Path()
    private val trianglePath = Path()
    var lockMove: Boolean = true
    var center = PointF()
    var radius = 0f

    fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, pickerPaint)
        canvas.drawPath(trianglePath, trianglePaint)
    }

    fun onActionDown(angle: Float, pullUp: Float) {
        //Draw agg
        updatePickerPath(pullUp)
        rotatePicker(angle)
    }

    fun onActionMove(angle: Float, pullUp: Float) {
        if (lockMove)
            return

        updatePickerPath(pullUp)
        updateTrianglePath(pullUp)
        rotatePicker(angle)
    }

    fun onActionUp() {
        updatePickerPath(0f)
        updateTrianglePath(0f)
    }

    fun createPickerPath() {
        updatePickerPath(0f)
        updateTrianglePath(0f)
    }

    private fun rotatePicker(angle: Float) {
        //Rotate agg
        val matrix = Matrix()
        matrix.setRotate(angle, center.x, center.y)
        path.transform(matrix)
        trianglePath.transform(matrix)
    }

    private fun updatePickerPath(pullUp: Float) {
        path.reset()

        val controlDelta = radius * 0.552f
        //Draw agg or circle
        val offset = pullUp//radius + pullUp

        val startPoint = PointF(center.x, center.y - radius - offset)
        path.moveTo(startPoint.x, startPoint.y)

        var controlPoint1 = PointF(center.x + controlDelta, center.y - radius - offset)
        var controlPoint2 = PointF(center.x + radius, center.y - controlDelta)

        val point2 = PointF(center.x + radius, center.y)
        path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point2.x, point2.y)

        controlPoint1 = PointF(center.x + radius, center.y + controlDelta)
        controlPoint2 = PointF(center.x + controlDelta, center.y + radius)

        val point3 = PointF(center.x, center.y + radius)
        path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point3.x, point3.y)

        controlPoint1 = PointF(center.x - controlDelta, center.y + radius)
        controlPoint2 = PointF(center.x - radius, center.y + controlDelta)
        val point4 = PointF(center.x - radius, center.y)
        path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point4.x, point4.y)

        controlPoint1 = PointF(center.x - radius, center.y - controlDelta)
        controlPoint2 = PointF(center.x - controlDelta, center.y - radius - offset)
        path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, startPoint.x, startPoint.y)

        path.close()
    }

    private fun updateTrianglePath(pullUp: Float) {
        trianglePath.reset()

        val length = radius / 12f
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

