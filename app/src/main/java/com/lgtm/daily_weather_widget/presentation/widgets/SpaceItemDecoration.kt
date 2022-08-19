package com.lgtm.daily_weather_widget.presentation.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lgtm.daily_weather_widget.utils.toPx

class SpaceItemDecoration(
    private val space: Int, // DP
    private val beforeFirst: Boolean = false,
    private val afterLast: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val spacePx = space.toPx(parent.context)
        val adapterPos = parent.getChildAdapterPosition(view)
        val adapterSize = parent.adapter?.itemCount ?: 0

        val isFirst = adapterPos == 0
        val isLast = adapterPos == adapterSize - 1
        val isVertical = (parent.layoutManager as? LinearLayoutManager)?.orientation == LinearLayoutManager.VERTICAL

        outRect.top = if (isFirst && beforeFirst && isVertical) spacePx else outRect.top
        outRect.right = if ((!isLast || afterLast) && !isVertical) spacePx else outRect.right
        outRect.bottom = if ((!isLast || afterLast) && isVertical) spacePx else outRect.bottom
        outRect.left = if (isFirst && beforeFirst && !isVertical) spacePx else outRect.left
    }
}