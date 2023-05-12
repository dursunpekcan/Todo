package com.dursunpekcan.todoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dursunpekcan.todoapp.dao.TaskDao
import com.dursunpekcan.todoapp.model.Task

@Database(entities = [Task::class], version = 3)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}