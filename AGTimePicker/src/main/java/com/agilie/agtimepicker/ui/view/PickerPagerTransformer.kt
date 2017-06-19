package com.agilie.agtimepicker.ui.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.View

class PickerPagerTransformer : ViewPager.PageTransformer {

    private var maxTranslateOffsetX = 0
    private var viewPager: ViewPager? = null

    constructor (context: Context) {
        this.maxTranslateOffsetX = dp2px(context, 120f)
    }

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }

        val leftInScreen = view.left - viewPager!!.scrollX

        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate = offsetX.toFloat() * 0.4f / viewPager!!.measuredWidth
        val scaleFactor = 1 - Math.abs(offsetRate)



        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
        }
    }

    private fun dp2px(context: Context, dipValue: Float): Int {
        val m = context.resources.displayMetrics.density
        return (dipValue * m + 0.5f).toInt()
    }
}