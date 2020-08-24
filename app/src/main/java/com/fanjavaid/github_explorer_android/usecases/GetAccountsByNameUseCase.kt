package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
interface GetAccountsByNameUseCase {
    val limit: Int

    val repository: AccountRepository

    suspend fun getAccounts(name: String, page: Int): List<Account>?
}

/**
 * Fetch from Network
 */
class GetAccountsByNameUseCaseImpl(override val repository: AccountRepository) :
    GetAccountsByNameUseCase {

    override val limit: Int
        get() = 10

    override suspend fun getAccounts(name: String, page: Int): List<Account>? {
        return repository.getAccountByName(name, page, limit)
    }
}
