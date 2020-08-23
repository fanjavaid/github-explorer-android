package com.fanjavaid.github_explorer_android.data.repository

import com.fanjavaid.github_explorer_android.data.model.Account
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
interface AccountRepository {
    suspend fun getAccountByName(name: String): List<Account>
}

class AccountLocalRepositoryImpl : AccountRepository {
    private val random = Random(Int.MAX_VALUE)
    override suspend fun getAccountByName(name: String): List<Account> {
        delay(3000)
        val randomNumber = random.nextInt()
        return if (randomNumber % 2 == 0) {
            listOf(
                Account(1L, "", "Jonathan", "California"),
                Account(1L, "", "Josh", "Australia"),
                Account(1L, "", "Jorgie", "London, UK"),
                Account(1L, "", "Jonathan", "California"),
                Account(1L, "", "Josephine", "Canada"),
                Account(1L, "", "Jonathan", "California"),
                Account(1L, "", "Josephine", "Canada"),
                Account(1L, "", "Anjovie", "Jakarta, Indonesia"),
                Account(1L, "", "Josh", "Australia"),
                Account(1L, "", "Jorgie", "London, UK"),
                Account(1L, "", "Anjovie", "Jakarta, Indonesia"),
                Account(1L, "", "Josephine", "Canada"),
                Account(1L, "", "Jorgie", "London, UK"),
                Account(1L, "", "Josh", "Australia"),
                Account(1L, "", "Anjovie", "Jakarta, Indonesia")
            )
        } else listOf()
    }
}

class AccountRemoteRepositoryImpl : AccountRepository {
    override suspend fun getAccountByName(name: String): List<Account> {
        TODO("Not yet implemented")
    }
}
