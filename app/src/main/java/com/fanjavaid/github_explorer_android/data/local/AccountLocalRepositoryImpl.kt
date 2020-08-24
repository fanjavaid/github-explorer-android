package com.fanjavaid.github_explorer_android.data.local

import androidx.lifecycle.LiveData
import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.model.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Fandi Akhmad (fanjavaid) on 24/08/20.
 */
class AccountLocalRepositoryImpl(private val accountDao: AccountDao) :
    AccountRepository {

    override suspend fun saveAccounts(accounts: List<Account>) {
        withContext(Dispatchers.IO) {
            accountDao.insertAccounts(accounts)
        }
    }

    override fun getAccounts(): LiveData<List<Account>?> = accountDao.getAccounts()

    override suspend fun deletedAccounts() {
        withContext(Dispatchers.IO) {
            accountDao.deleteAccounts()
        }
    }

    override suspend fun getAccountByName(name: String, page: Int, limit: Int): List<Account>? {
        throw UnsupportedOperationException("Not supported")
    }
}
