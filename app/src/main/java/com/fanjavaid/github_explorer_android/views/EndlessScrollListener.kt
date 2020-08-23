package com.fanjavaid.github_explorer_android.views

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Fandi Akhmad (fanjavaid) on 21/08/20.
 */
class EndlessScrollListener : RecyclerView.OnScrollListener() {
    var isLoading = false
    var action: (() -> Unit)? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
//        Log.d("EndlessScrollListener", "dy = $dy")
        if (!isLoading && dy >= 0) {
            recyclerView.layoutManager?.let {
                val total = it.itemCount
                Log.d("EndlessScrollListener", "Item = ${it.itemCount}")
                Log.d("EndlessScrollListener", "Child = ${it.childCount}")
                val lastVisiblePosition = (it as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastVisiblePosition == (total - 1)) {
                    Log.d("EndlessScrollListener", "Load More...")
                    action?.invoke()
                }
            }
        }
    }
}
