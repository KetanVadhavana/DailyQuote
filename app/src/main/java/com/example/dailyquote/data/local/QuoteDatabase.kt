package com.example.dailyquote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QuoteEntity::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

}