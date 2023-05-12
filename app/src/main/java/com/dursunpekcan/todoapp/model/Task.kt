package com.dursunpekcan.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @ColumnInfo("task")
    var task: String,

    @ColumnInfo("priority")
    var priority: String,

    @ColumnInfo("done")
    var done:Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}