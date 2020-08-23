package com.fanjavaid.github_explorer_android.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
data class Account(
    var id: Long,

    @SerializedName("avatar_url")
    var avatarUrl: String,

    @SerializedName("login")
    var name: String,

    var location: String
)
