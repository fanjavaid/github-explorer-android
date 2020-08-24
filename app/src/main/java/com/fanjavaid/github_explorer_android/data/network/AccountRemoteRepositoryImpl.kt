package com.fanjavaid.github_explorer_android.data.network

import androidx.lifecycle.LiveData
import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.network.config.RetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Fandi Akhmad (fanjavaid) on 24/08/20.
 */
class AccountRemoteRepositoryImpl : AccountRepository {
    override suspend fun getAccountByName(name: String, page: Int, limit: Int): List<Account>? {
        return withContext(Dispatchers.IO) {
            try {
                RetrofitApi
                    .service(GithubExplorerService::class.java)
                    .listUsers(name, page, limit)
                    .items
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun getAccounts(): LiveData<List<Account>?> {
        throw UnsupportedOperationException("Not supported")
    }

    override suspend fun saveAccounts(accounts: List<Account>) {
        throw UnsupportedOperationException("Not supported")
    }

    override suspend fun deletedAccounts() {
        throw UnsupportedOperationException("Not supported")
    }
}
