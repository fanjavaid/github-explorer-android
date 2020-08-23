package com.fanjavaid.github_explorer_android.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fanjavaid.github_explorer_android.R

/**
 * Created by Fandi Akhmad (fanjavaid) on 21/08/20.
 */
class AdapterViewType {
    var type: Int = TYPE_LOADING

    fun getViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return if (type == TYPE_LOADING) {
            AccountAdapter.ProgressViewHolder(
                inflater.inflate(R.layout.item_loading, viewGroup, false)
            )
        } else {
            AccountAdapter.ViewHolder(
                inflater.inflate(R.layout.item_account, viewGroup, false)
            )
        }
    }

    companion object {
        const val TYPE_LOADING = 0
        const val TYPE_ITEM = 1
    }
}
