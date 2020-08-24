package com.fanjavaid.github_explorer_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    var accountId: Long,

    @SerializedName("avatar_url")
    var avatarUrl: String,

    @SerializedName("login")
    var name: String,

    var location: String?
)
