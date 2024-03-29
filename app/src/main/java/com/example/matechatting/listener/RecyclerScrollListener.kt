package com.example.matechatting.listener

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

//    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
//        if (v?.getChildAt(v.childCount - 1) != null) {
//            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
//                scrollY > oldScrollY
//            ) {
//                val visibleItemCount = layoutManager.childCount;
//                val totalItemCount = layoutManager.itemCount;
//                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
//                if (!isLastPage()) {
//                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount
//                        && pastVisibleItems >= 0
//                        && totalItemCount >= PAGE_SIZE
//                    ) {
//                        loadMoreItems()
//                    }
//                }
//            }
//        }
//    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 7
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
            ) {
                loadMoreItems()
            }
        }
    }

    abstract fun isLastPage(): Boolean

    abstract fun loadMoreItems()

    companion object {
        private const val PAGE_SIZE = 20
    }
}