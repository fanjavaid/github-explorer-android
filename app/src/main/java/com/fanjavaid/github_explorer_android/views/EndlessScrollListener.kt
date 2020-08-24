package com.fanjavaid.github_explorer_android.views

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
        if (!isLoading && dy > 0) {
            recyclerView.layoutManager?.let {
                val total = it.itemCount
                val lastVisiblePosition = (it as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastVisiblePosition == (total - 1)) {
                    action?.invoke()
                }
            }
        }
    }
}
