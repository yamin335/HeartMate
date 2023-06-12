package com.tovalue.me.util

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tovalue.me.R


class HorizontalItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPadding = 8 // use extension function to pass dynamic values
        parent.let {
            if (it.getChildAdapterPosition(view) != 0) {
                outRect.left = itemPadding
            } else {
                outRect.right = itemPadding
            }
        }
    }
}