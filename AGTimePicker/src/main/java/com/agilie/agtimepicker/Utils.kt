package com.agilie.volumecontrol

import android.graphics.PointF
import java.lang.Math.*

fun getPointOnBorderLineOfCircle(center: PointF, radius: Float, alfa: Int = 0) =
        PointF().apply {
            x = (radius * cos(toRadians(alfa - 90.0)) + center.x).toFloat()
            y = (radius * sin(toRadians(alfa - 90.0)) + center.y).toFloat()
        }

fun calculateAngleWithTwoVectors(touch: PointF?, center: PointF?): Float {
    var angle = 0.0
    if (touch != null && center != null) {
        val x2 = touch.x - center.x
        val y2 = touch.y - center.y
        val d1 = sqrt((center.y * center.y).toDouble())
        val d2 = sqrt((x2 * x2 + y2 * y2).toDouble())
        if (touch.x >= center.x) {
            angle = toDegrees(acos((-center.y * y2) / (d1 * d2)))
        } else
            angle = 360 - toDegrees(acos((-center.y * y2) / (d1 * d2)))
    }
    return angle.toFloat()
}

fun distance(point1: PointF, point2: PointF): Float {
    return sqrt(((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y)).toDouble()).toFloat()
}

fun pointInCircle(point: PointF, pointCenter: PointF, radius: Float) =
        pow((point.x - pointCenter.x).toDouble(), 2.0) +
                pow((point.y - pointCenter.y).toDouble(), 2.0) <= radius * radius

