package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.AccountRepository

/**
 * Created by Fandi Akhmad (fanjavaid) on 24/08/20.
 */
interface DeleteAccountsUseCase {
    val repository: AccountRepository
    suspend fun deleteAccounts()
}

class DeleteLocalAccountsUseCaseImpl(override val repository: AccountRepository) :
    DeleteAccountsUseCase {

    override suspend fun deleteAccounts() = repository.deletedAccounts()
}
