package com.trungkien.qlychitieu2

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ChiTietThuChi",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["userID"],
        childColumns = ["userID"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class ChiTietThuChi(
    @PrimaryKey(autoGenerate = true) val thuChiID: Int = 0,
    val userID: Int,
    val tenThuChi: String,
    val lyDoThuChi: String,
    val date: String,
    val soTien: Double
): Parcelable


