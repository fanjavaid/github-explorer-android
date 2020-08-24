package com.fanjavaid.github_explorer_android.viewmodels

import androidx.lifecycle.*
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByName
import com.fanjavaid.github_explorer_android.usecases.GetCacheAccounts
import kotlinx.coroutines.launch

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class GetAccountViewModel(
    getCacheAccounts: GetCacheAccounts,
    private val getAccountsByName: GetAccountsByName
) : ViewModel() {
    private val searchQuery = MutableLiveData<SearchQuery>()

    val cachedAccounts: LiveData<List<Account>?> = getCacheAccounts.getAccounts()

    val accounts: LiveData<List<Account>> = searchQuery.switchMap { query ->
        val results = MutableLiveData<List<Account>>()
        viewModelScope.launch {
            val result = getAccountsByName.getAccounts(query.name, query.page)
            results.value = result
        }
        results
    }

    fun searchGithubAccount(searchQuery: SearchQuery) {
        this.searchQuery.postValue(searchQuery)
    }
}

@Suppress("UNCHECKED_CAST")
class GetAccountViewModelFactory(
    private val getCacheAccounts: GetCacheAccounts,
    private val getAccountsByName: GetAccountsByName
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GetAccountViewModel(getCacheAccounts, getAccountsByName) as T
    }
}
