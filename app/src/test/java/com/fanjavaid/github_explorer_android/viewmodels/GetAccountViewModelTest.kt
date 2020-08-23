package com.fanjavaid.github_explorer_android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByNameUseCase
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
    private lateinit var getAccountsByNameUseCase: GetAccountsByNameUseCase
    private lateinit var viewModel: GetAccountViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = GetAccountViewModel(getAccountsByNameUseCase)
    }

    @Test
    fun `Given a keyword When fetch data and success Then should return the results`() {
        runBlockingTest {
            // Given
            val mockResults = listOf<Account>(mockk())
            val mockObserver: Observer<List<Account>> = spyk(Observer {  })
            viewModel.accounts.observeForever(mockObserver)

            coEvery { getAccountsByNameUseCase.getAccounts(any()) } returns mockResults

            // When
            viewModel.searchGithubAccount("john")

            // Then
            verify {
                mockObserver.onChanged(mockResults)
            }
            coVerify {
                getAccountsByNameUseCase.getAccounts(any())
            }
            assert(viewModel.accounts.value?.size == mockResults.size)
        }
    }
}
