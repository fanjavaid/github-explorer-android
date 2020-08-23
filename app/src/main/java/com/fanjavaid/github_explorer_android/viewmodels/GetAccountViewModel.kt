package com.fanjavaid.github_explorer_android.viewmodels

import androidx.lifecycle.*
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByNameUseCase
import kotlinx.coroutines.launch

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class GetAccountViewModel(
    private val getAccountsByNameUseCase: GetAccountsByNameUseCase
) : ViewModel() {
    private val name = MutableLiveData<String>()

    val accounts: LiveData<List<Account>> = name.switchMap {
        val results = MutableLiveData<List<Account>>()
        viewModelScope.launch {
            results.value = getAccountsByNameUseCase.getAccounts(name.value ?: "")
        }
        results
    }

    fun searchGithubAccount(name: String) = this.name.postValue(name)
}

@Suppress("UNCHECKED_CAST")
class GetAccountViewModelFactory(private val getAccountsByNameUseCase: GetAccountsByNameUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GetAccountViewModel(getAccountsByNameUseCase) as T
    }
}
