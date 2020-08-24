package com.fanjavaid.github_explorer_android.data

import androidx.lifecycle.LiveData
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
interface AccountRepository {
    fun getAccounts(): LiveData<List<Account>?>
    suspend fun getAccountByName(name: String, page: Int, limit: Int): List<Account>?
    suspend fun saveAccounts(accounts: List<Account>)
    suspend fun deletedAccounts()
}
