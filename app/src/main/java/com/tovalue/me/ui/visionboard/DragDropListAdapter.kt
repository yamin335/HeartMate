package com.tovalue.me.ui.visionboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.R
import com.tovalue.me.helper.DragDropItemHelper.ItemTouchHelperContract
import java.util.*
import kotlin.collections.ArrayList


class DragDropListAdapter(data: ArrayList<String>, startDragListener: StartDragListener) :
    RecyclerView.Adapter<DragDropListAdapter.MyViewHolder?>(), ItemTouchHelperContract {
    private val data: ArrayList<String>
    private val mStartDragListener: StartDragListener
    
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTitle: TextView
        var rowView: View
        var containerLayout: LinearLayout
        
        init {
            rowView = itemView
            mTitle = itemView.findViewById(R.id.date_tv)
            containerLayout = itemView.findViewById(R.id.container)
        }
    }
    
    init {
        mStartDragListener = startDragListener
        this.data = data
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_date_tab, parent, false)
        return MyViewHolder(itemView)
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = data[position]
        holder.containerLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) mStartDragListener.requestDrag(holder)
            false
        }
    }
    
    override fun getItemCount(): Int {
        return data.size
    }
    
    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }
    
    fun getUpdatedOrderListed(): List<String> = data
    
    override fun onRowSelected(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundColor(Color.TRANSPARENT)
    }
    
    override fun onRowClear(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundColor(Color.TRANSPARENT)
    }
    
    interface StartDragListener {
        fun requestDrag(viewHolder: RecyclerView.ViewHolder?)
    }
}