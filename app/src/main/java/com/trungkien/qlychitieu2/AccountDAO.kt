package com.trungkien.qlychitieu2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDAO {
    @Insert
    suspend fun insert(account: Account)

    @Query("SELECT * FROM Account WHERE userID = :userID")
    suspend fun getAccountByID(userID: Int): Account?

    @Query("SELECT * FROM Account WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): Account?

    @Query("SELECT * FROM Account WHERE username = :username LIMIT 1")
    suspend fun getAccountByUsername(username: String): Account?

}