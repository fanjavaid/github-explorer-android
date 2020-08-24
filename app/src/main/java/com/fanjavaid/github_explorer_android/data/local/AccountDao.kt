package com.fanjavaid.github_explorer_android.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fanjavaid.github_explorer_android.data.model.Account

/**
 * Created by Fandi Akhmad (fanjavaid) on 23/08/20.
 */
@Dao
interface AccountDao {
    @Query("SELECT * FROM Account")
    fun getAccounts(): LiveData<List<Account>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<Account>)

    @Query("DELETE FROM Account")
    suspend fun deleteAccounts()
}
