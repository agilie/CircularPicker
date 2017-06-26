package com.agilie.circularpicker.ui.animation

import android.graphics.*


class CircularPickerPath(val pickerPaint: Paint,
                         val pointerPaint: Paint) {

    private val path = Path()
    private val pointerPath = Path()
    private val pointerRadius = 10f
    var lockMove: Boolean = true
    var center = PointF()
    var radius = 0f

    fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, pickerPaint)
        canvas.drawPath(pointerPath, pointerPaint)
    }

    fun onActionDown(angle: Int, pullUp: Float) {
        // Draw egg
        updatePickerPath(pullUp)
        rotatePicker(angle)
    }

    fun onActionMove(angle: Int, pullUp: Float) {
        if (lockMove)
            return

        updatePickerPath(pullUp)
        updatePointerPath(pullUp)
        rotatePicker(angle)
    }

    fun onActionUp() {
        updatePickerPath(0f)
        updatePointerPath(0f)
    }

    fun createPickerPath() {
        updatePickerPath(0f)
        updatePointerPath(0f)
    }

    private fun rotatePicker(angle: Int) {
        // Rotate egg
        val matrix = Matrix()
        matrix.setRotate(angle.toFloat(), center.x, center.y)
        path.transform(matrix)
        pointerPath.transform(matrix)
    }

    private fun updatePickerPath(pullUp: Float) {
        path.reset()

        val controlDelta = radius * 0.552f
        // Draw egg or circle
        val offset = pullUp // radius + pullUp

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

    private fun updatePointerPath(pullUp: Float) {
        pointerPath.reset()

        val x = center.x
        val y = center.y - radius - pullUp + 30f

        pointerPath.addCircle(x, y, pointerRadius, Path.Direction.CW)
        pointerPath.close()

    }
}

