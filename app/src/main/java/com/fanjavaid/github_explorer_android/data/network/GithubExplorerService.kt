package com.fanjavaid.github_explorer_android.data.network

import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.network.config.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Fandi Akhmad (fanjavaid) on 23/08/20.
 */
interface GithubExplorerService {
    @GET("search/users")
    suspend fun listUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): Response<List<Account>>
}
