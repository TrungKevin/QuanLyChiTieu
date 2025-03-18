package com.trungkien.qlychitieu2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val userID: Int = 0,
    val username: String,
    val password: String
)