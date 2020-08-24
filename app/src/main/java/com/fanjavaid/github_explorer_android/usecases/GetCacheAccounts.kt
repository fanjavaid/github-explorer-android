package com.fanjavaid.github_explorer_android.usecases

import androidx.lifecycle.LiveData
import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 24/08/20.
 */
class GetCacheAccounts(private val repository: AccountRepository) {
    fun getAccounts(): LiveData<List<Account>?> = repository.getAccounts()
}
