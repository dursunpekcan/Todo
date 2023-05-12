package com.dursunpekcan.todoapp.adapter

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dursunpekcan.todoapp.dao.TaskDao
import com.dursunpekcan.todoapp.databinding.TaskRowBinding
import com.dursunpekcan.todoapp.db.TaskDatabase
import com.dursunpekcan.todoapp.fragment.DetailsFragmentDirections
import com.dursunpekcan.todoapp.model.Task
import java.text.DateFormat
import java.util.*

class TaskAdapter(var taskList: List<Task>, var context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskHolder>() {
    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDao


    fun setFilterList(filteredList: List<Task>) {
        this.taskList = filteredList
        notifyDataSetChanged()
    }


    class TaskHolder(val binding: TaskRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = TaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {

        db = Room.databaseBuilder(holder.itemView.context, TaskDatabase::class.java, "Tasks")
            .allowMainThreadQueries().build()
        dao = db.taskDao()


        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar)
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)

        holder.binding.tvTime.text = "$timeFormat"
        holder.binding.tvDate.text = "$dateFormat"

        holder.binding.cbTask.text = taskList.get(position).task
        holder.binding.tvPriority.text = taskList.get(position).priority

        if (taskList.get(position).priority == "High") {
            holder.binding.tvPriority.setTextColor(Color.RED)
        }
        if (taskList.get(position).priority == "Medium") {
            holder.binding.tvPriority.setTextColor(Color.YELLOW)
        }
        if (taskList.get(position).priority == "Low(default)") {
            holder.binding.tvPriority.text = "Low"
            holder.binding.tvPriority.setTextColor(Color.GREEN)
        }

        /*  holder.itemView.setOnClickListener {
            dao.delete(taskList.get(position))
            holder.binding.cbTask.visibility = View.GONE
            Toast.makeText(holder.itemView.context, "Task Done", Toast.LENGTH_SHORT).show()
            holder.binding.tvDate.visibility = View.GONE
            holder.binding.tvTime.visibility = View.GONE
            holder.binding.btnEdit.visibility = View.GONE
            holder.binding.tvPriority.visibility = View.GONE
        } */
        holder.binding.cbTask.setOnCheckedChangeListener { buttonView, isChecked ->

            val currentNightMode =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (isChecked) {
                dao.updateDone(true, taskList[position].id)
                holder.binding.cbTask.setText((Html.fromHtml("<strike>${taskList.get(position).task}</strike>")))
                holder.binding.cbTask.setTextColor(Color.GRAY)


            } else {
                dao.updateDone(false, taskList.get(position).id)
                holder.binding.cbTask.setText("${taskList.get(position).task}")


                if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                    holder.binding.cbTask.setTextColor(Color.BLACK)
                }
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    holder.binding.cbTask.setTextColor(Color.BLACK)
                }


            }
        }

        //holder.binding.cbTask.isChecked = taskList.get(position).done
        holder.binding.cbTask.isChecked=dao.getDoneFromId(taskList[position].id)
        if (holder.binding.cbTask.isChecked) {
            holder.binding.cbTask.setText((Html.fromHtml("<strike>${taskList.get(position).task}</strike>")))
        } else {
            holder.binding.cbTask.setText("${taskList.get(position).task}")
        }

        holder.binding.btnEdit.setOnClickListener {
            val data = holder.binding.cbTask.text.toString()
            val priority = holder.binding.tvPriority.text.toString()
            Navigation.findNavController(it).navigate(
                DetailsFragmentDirections.actionDetailsFragmentToEditFragment(
                    data0 = data, priority = priority
                )
            )
        }


    }
}


