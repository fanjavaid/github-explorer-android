package com.fanjavaid.github_explorer_android.viewmodels

import androidx.lifecycle.*
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByNameUseCase
import com.fanjavaid.github_explorer_android.usecases.GetAccountsUseCase
import kotlinx.coroutines.launch

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class GetAccountViewModel(
    getAccountsUseCase: GetAccountsUseCase,
    private val getAccountsByNameUseCase: GetAccountsByNameUseCase
) : ViewModel() {
    private val searchQuery = MutableLiveData<SearchQuery>()

    val cachedAccounts: LiveData<List<Account>?> = getAccountsUseCase.getAccounts()

    val accounts: LiveData<List<Account>> = searchQuery.switchMap { query ->
        val results = MutableLiveData<List<Account>>()
        viewModelScope.launch {
            val result = getAccountsByNameUseCase.getAccounts(query.name, query.page)
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
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getAccountsByNameUseCase: GetAccountsByNameUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GetAccountViewModel(getAccountsUseCase, getAccountsByNameUseCase) as T
    }
}
