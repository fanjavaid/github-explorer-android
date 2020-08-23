package com.fanjavaid.github_explorer_android.views

/**
 * Created by Fandi Akhmad (fanjavaid) on 20/08/20.
 */
interface LoadableView<T> {
    fun render(data: T)
    fun showLoading()
    fun hideLoading()
    fun onLoadMore()
}

interface EmptyView {
    fun showEmpty()
}
