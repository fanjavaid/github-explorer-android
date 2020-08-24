package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class GetAccountsByName(private val repository: AccountRepository) {
    var limit: Int = 10

    suspend fun getAccounts(name: String, page: Int): List<Account>? {
        if (name.isEmpty()) return null
        return repository.getAccountByName(name, page, limit)
    }
}
