package com.flametech.vaytoday.utils.saved

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedMovie(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "businessId")
    val businessId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "subcategory")
    val subcategory: String?,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "instagram")
    val instagram: String?,
    @ColumnInfo(name = "instagram_uri")
    val instagram_uri: String?,
    @ColumnInfo(name = "site")
    val site: String?,
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "city")
    val city: String?,
    @ColumnInfo(name = "image")
    val image: String
)