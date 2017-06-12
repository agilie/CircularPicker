package com.agilie.volumecontrol

import android.graphics.PointF

fun getPointOnBorderLineOfCircle(point: PointF?, radius: Float, alfa: Int = 0) =
        PointF().apply {
            if (point != null) {
                x = (radius * Math.cos(Math.toRadians(alfa - 90.0)) + point.x).toFloat()
                y = (radius * Math.sin(Math.toRadians(alfa - 90.0)) + point.y).toFloat()
            }
        }

fun calculateAngleWithTwoVectors(touch: PointF?, center: PointF?): Double {
    var angle = 0.0
    if (touch != null && center != null) {
        val x2 = touch.x - center.x
        val y2 = touch.y - center.y
        val d1 = Math.sqrt((center.y * center.y).toDouble())
        val d2 = Math.sqrt((x2 * x2 + y2 * y2).toDouble())
        if (touch.x >= center.x) {
            angle = Math.toDegrees(Math.acos((-center.y * y2) / (d1 * d2)))
        } else
            angle = 360 - Math.toDegrees(Math.acos((-center.y * y2) / (d1 * d2)))
    }
    return angle
}

fun pointInCircle(point: PointF, pointCenter: PointF, radius: Float) =
        Math.pow((point.x - pointCenter.x).toDouble(), 2.0) +
                Math.pow((point.y - pointCenter.y).toDouble(), 2.0) <= radius * radius


fun getPointOnBorderLineOfCircle(innerX: Float, innerY: Float, innerRadius: Float, alfa: Double = 0.0) =
        PointF().apply {
            x = (innerRadius * Math.cos(Math.toRadians(alfa - 90.0)) + innerX).toFloat()
            y = (innerRadius * Math.sin(Math.toRadians(alfa - 90.0)) + innerY).toFloat()
        }

fun calculateAngleWithTwoVectors(touchX: Float, touchY: Float, centerX: Float, centerY: Float): Double {
    val angle: Double
    val x2 = touchX - centerX
    val y2 = touchY - centerY
    val d1 = Math.sqrt((centerY * centerY).toDouble())
    val d2 = Math.sqrt((x2 * x2 + y2 * y2).toDouble())
    if (touchX >= centerX) {
        angle = Math.toDegrees(Math.acos((-centerY * y2) / (d1 * d2)))
    } else
        angle = 360 - Math.toDegrees(Math.acos((-centerY * y2) / (d1 * d2)))
    return angle
}

fun closestValue(value: Double, closestValue: Int): Int {
    var j = (Math.round(value)).toInt()
    while (true) {
        if (j > 0 && closestValue > 0) {
            if (j % closestValue == 0)
                return j
            else
                ++j
        } else
            return j
    }
}

