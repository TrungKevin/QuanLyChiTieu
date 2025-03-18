package com.trungkien.qlychitieu2

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Dao
interface ChiTietThuChiDao {
    @Insert
    suspend fun insert(chiTietThuChi: ChiTietThuChi)

    @Query("SELECT * FROM ChiTietThuChi WHERE userID = :userID")
    suspend fun getChiTietThuChiByUserID(userID: Int): List<ChiTietThuChi>


    @Query("SELECT * FROM ChiTietThuChi")
    fun getAll(): LiveData<List<ChiTietThuChi>>

    @Update
    suspend fun update(chiTietThuChi: ChiTietThuChi)

    @Delete
    suspend fun delete(chiTietThuChi: ChiTietThuChi)
}
