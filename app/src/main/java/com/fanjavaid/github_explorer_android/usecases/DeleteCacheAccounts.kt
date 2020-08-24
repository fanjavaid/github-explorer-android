package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.AccountRepository

/**
 * Created by Fandi Akhmad (fanjavaid) on 24/08/20.
 */
class DeleteCacheAccounts(private val repository: AccountRepository) {
    suspend fun deleteAccounts() = repository.deletedAccounts()
}
