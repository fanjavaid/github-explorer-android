package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 23/08/20.
 */
class SaveCacheAccounts(private val repository: AccountRepository) {
    suspend fun saveAccounts(accounts: List<Account>) = repository.saveAccounts(accounts)
}
