package com.trungkien.qlychitieu2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [Account::class, ChiTietThuChi::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDAO(): AccountDAO
    abstract fun chiTietThuChiDAO(): ChiTietThuChiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quan_ly_chi_tieu_db"
                ).fallbackToDestructiveMigration() // Cho phép xóa và tạo lại CSDL khi schema thay đổi
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}