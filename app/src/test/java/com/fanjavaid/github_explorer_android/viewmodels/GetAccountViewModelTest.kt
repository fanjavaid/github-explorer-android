package com.fanjavaid.github_explorer_android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByNameUseCase
import com.fanjavaid.github_explorer_android.usecases.GetAccountsUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Fandi Akhmad (fanjavaid) on 20/08/20.
 */
@ExperimentalCoroutinesApi
class GetAccountViewModelTest {
    @MockK
    private lateinit var getAccountsUseCase: GetAccountsUseCase
    @MockK
    private lateinit var getAccountsByNameUseCase: GetAccountsByNameUseCase
    private lateinit var viewModel: GetAccountViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = GetAccountViewModel(getAccountsUseCase, getAccountsByNameUseCase)
        coEvery { viewModel.cachedAccounts } returns MutableLiveData<List<Account>>()
    }

    // FIXME: 24/08/20 io.mockk.MockKException: no answer found for: GetAccountsUseCase(getAccountsUseCase#2).getAccounts()
    @Test
    fun `Given a keyword When fetch data and success Then should return the results`() {
        runBlockingTest {
            // Given
            val mockResults = listOf<Account>(mockk())
            val mockObserver: Observer<List<Account>> = spyk(Observer {  })
            viewModel.accounts.observeForever(mockObserver)

            coEvery { getAccountsByNameUseCase.getAccounts(any(), any()) } returns mockResults

            // When
            viewModel.searchGithubAccount(SearchQuery("john", 1))

            // Then
            verify {
                mockObserver.onChanged(mockResults)
            }
            coVerify {
                getAccountsByNameUseCase.getAccounts(any(), any())
            }
            assert(viewModel.accounts.value?.size == mockResults.size)
        }
    }
}
