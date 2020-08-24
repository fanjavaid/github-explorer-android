package com.fanjavaid.github_explorer_android.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.AccountRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by Fandi Akhmad (fanjavaid) on 20/08/20.
 */
class GetCacheAccountsTest {
    private lateinit var mockRepository: AccountRepository
    private lateinit var mockEmptyRepository: AccountRepository

    @Before
    fun setUp() {
        mockRepository = object : AccountRepository {

            override fun getAccounts(): LiveData<List<Account>?> = MutableLiveData()

            override suspend fun getAccountByName(
                name: String,
                page: Int,
                limit: Int
            ): List<Account>? = List(5) { mockk<Account>() }

            override suspend fun saveAccounts(accounts: List<Account>) {
            }

            override suspend fun deletedAccounts() {
            }
        }
        mockEmptyRepository = object : AccountRepository {
            override fun getAccounts(): LiveData<List<Account>?> = MutableLiveData()

            override suspend fun getAccountByName(
                name: String,
                page: Int,
                limit: Int
            ): List<Account>? = null

            override suspend fun saveAccounts(accounts: List<Account>) {
            }

            override suspend fun deletedAccounts() {
            }
        }
    }

    @Test
    fun `Given an empty keyword When search account Then return empty results`() {
        val getAccountsByName = GetAccountsByName(mockEmptyRepository)

        runBlocking {
            val result = getAccountsByName.getAccounts("", 1)
            assert(result == null)
        }
    }

    @Test
    fun `Given a keyword When search account Then return the results`() {
        val getAccountsByName = GetAccountsByName(mockRepository)

        runBlocking {
            val result = getAccountsByName.getAccounts("john", 1)
            assert(result?.isNotEmpty() == true)
            assert(result?.size == 5)
        }
    }
}
