package com.agilie.agtimepicker.ui

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import java.util.*


class PickerPagerAdapter : PagerAdapter() {

    private val views = ArrayList<View>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, ob: Any) {
        container.removeView(views[position])
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, ob: Any): Boolean {
        return view === ob
    }

    override fun getItemPosition(ob: Any): Int {
        val index = views.indexOf(ob)
        if (index == -1)
            return PagerAdapter.POSITION_NONE
        else
            return index
    }

    fun addView(v: View): Int {
        return addView(v, views.size)
    }

    fun addView(v: View, position: Int): Int {
        views.add(position, v)
        notifyDataSetChanged()
        return position
    }

    fun removeView(pager: ViewPager, v: View): Int {
        return removeView(pager, views.indexOf(v))
    }

    fun removeView(pager: ViewPager, position: Int): Int {
        pager.adapter = null
        views.removeAt(position)
        pager.adapter = this
        return position
    }

    fun getView(position: Int): View {
        return views[position]
    }

}