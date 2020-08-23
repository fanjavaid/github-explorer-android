package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
interface GetAccountsByNameUseCase {
    suspend fun getAccounts(name: String): List<Account>
}

class GetAccountsByNameUseCaseImpl(private val repository: AccountRepository) :
    GetAccountsByNameUseCase {
    override suspend fun getAccounts(name: String): List<Account> {
        return withContext(Dispatchers.Default) {
            repository.getAccountByName(name)
        }
    }
}
