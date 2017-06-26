package com.agilie.agtimepickerexample

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.View

class PickerPagerTransformer : ViewPager.PageTransformer {

    private var maxTranslateOffsetX = 0
    private var viewPager: ViewPager? = null

    constructor (context: Context, dipValue: Int) {
        this.maxTranslateOffsetX = dp2px(context, dipValue)
    }

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }

        val leftInScreen = view.left - viewPager!!.scrollX

        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate = offsetX.toFloat() * 0.2f / viewPager!!.measuredWidth
        val scaleFactor = 1 - Math.abs(offsetRate)

        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
        }
    }

    private fun dp2px(context: Context, dipValue: Int): Int {
        val m = context.resources.displayMetrics.density
        return (dipValue * m + 0.5f).toInt()
    }
}