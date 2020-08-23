package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.repository.AccountRepository

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
interface GetAccountsByNameUseCase {
    val limit: Int
    suspend fun getAccounts(name: String, page: Int): List<Account>?
}

class GetAccountsByNameUseCaseImpl(private val repository: AccountRepository) :
    GetAccountsByNameUseCase {

    override val limit: Int
        get() = 10

    override suspend fun getAccounts(name: String, page: Int): List<Account>? {
        return repository.getAccountByName(name, page, limit)
    }
}
