package com.dursunpekcan.todoapp.dao

import androidx.room.*
import com.dursunpekcan.todoapp.model.Task
import org.intellij.lang.annotations.JdkConstants.ListSelectionMode

@Dao
interface TaskDao {


    @Query("SELECT * FROM Task ORDER BY id ASC")
    fun getAllTaskDesc(): List<Task>

    @Insert
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("UPDATE Task SET task = :newTask, priority= :priority WHERE task = :oldTask")
    fun update(oldTask: String, newTask: String, priority: String)

    @Query("DELETE FROM Task")
    fun deleteAll()

    @Query("UPDATE Task SET done = :done WHERE id=  :id")
    fun updateDone(done: Boolean, id: Int)

    @Query("SELECT done FROM Task WHERE id = :id")
    fun getDoneFromId(id:Int):Boolean


}