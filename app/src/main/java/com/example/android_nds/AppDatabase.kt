package com.example.android_nds

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_nds.dao.HistoryDao
import com.example.android_nds.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}