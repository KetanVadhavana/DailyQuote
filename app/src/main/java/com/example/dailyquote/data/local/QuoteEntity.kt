package com.example.dailyquote.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Quotes")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val quote: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val modified: String?
)