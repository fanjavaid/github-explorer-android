package com.fanjavaid.github_explorer_android.usecases

import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.repository.AccountRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by Fandi Akhmad (fanjavaid) on 20/08/20.
 */
class GetAccountsByNameUseCaseImplTest {
    private lateinit var mockRepository: AccountRepository
    private lateinit var mockEmptyRepository: AccountRepository

    @Before
    fun setUp() {
        mockRepository = object : AccountRepository {
            override suspend fun getAccountByName(name: String): List<Account> {
                return List(5){ mockk<Account>()}
            }
        }
        mockEmptyRepository = object : AccountRepository {
            override suspend fun getAccountByName(name: String): List<Account> {
                return emptyList()
            }
        }
    }

    @Test
    fun `Given an empty keyword When search account Then return empty results`() {
        val getAccountsByNameUseCaseImpl = GetAccountsByNameUseCaseImpl(mockEmptyRepository)

        runBlocking {
            val result = getAccountsByNameUseCaseImpl.getAccounts("")
            assert(result.isEmpty())
        }
    }

    @Test
    fun `Given a keyword When search account Then return the results`() {
        val getAccountsByNameUseCaseImpl = GetAccountsByNameUseCaseImpl(mockRepository)

        runBlocking {
            val result = getAccountsByNameUseCaseImpl.getAccounts("john")
            assert(result.isNotEmpty())
            assert(result.size == 5)
        }
    }
}
