package com.agilie.mobileeastergift

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agilie.agtimepicker.ui.view.TimePickerView
import com.agilie.agtimepickerexample.R


class ViewsAdapter(var viewsList: List<TimePickerView>, var addNewViewsListener: AddNewViewsListener,
                   var context: Context) : RecyclerView.Adapter<ViewsAdapter.ViewHolder>() {

    override fun getItemCount() = viewsList.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        //
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.view_item, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface AddNewViewsListener {
        fun addNewView()
    }
}