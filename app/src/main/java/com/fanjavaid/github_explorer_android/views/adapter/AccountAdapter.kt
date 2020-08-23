package com.fanjavaid.github_explorer_android.views.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fanjavaid.github_explorer_android.R
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.views.adapter.AdapterViewType.Companion.TYPE_ITEM
import com.fanjavaid.github_explorer_android.views.adapter.AdapterViewType.Companion.TYPE_LOADING
import kotlinx.android.synthetic.main.item_account.view.*

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class AccountAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var accounts = mutableListOf<Account?>()
    private val adapterViewType = AdapterViewType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        adapterViewType.type = viewType
        return adapterViewType.getViewHolder(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return if (accounts[position] == null) TYPE_LOADING else TYPE_ITEM
    }

    override fun getItemCount(): Int = accounts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val account = accounts[position]
        if (holder is DefaultViewHolder) holder.setData(account)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), DefaultViewHolder {
        override fun setData(account: Account?) {
            if (account != null) {
                view.ivAvatar.setImageResourceUrl(account.avatarUrl)
                view.tvName.text = account.name
                view.tvLocation.text = account.location
            }
        }
    }

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view), DefaultViewHolder {
        override fun setData(account: Account?) {
        }
    }
}

fun ImageView.setImageResourceUrl(url: String) {
    Glide.with(this)
        .load(url.isBlank())
        .error(R.drawable.shape_circle_gray)
        .circleCrop()
        .into(this)
}

interface DefaultViewHolder {
    fun setData(account: Account?)
}
