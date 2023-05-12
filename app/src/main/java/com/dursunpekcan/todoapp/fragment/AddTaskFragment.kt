package com.dursunpekcan.todoapp.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.room.Room

import com.dursunpekcan.todoapp.dao.TaskDao
import com.dursunpekcan.todoapp.databinding.FragmentAddTaskBinding

import com.dursunpekcan.todoapp.db.TaskDatabase
import com.dursunpekcan.todoapp.util.showKeyboard


class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDao


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etTask.requestFocus()
        activity?.showKeyboard()


        db = Room.databaseBuilder(requireContext(), TaskDatabase::class.java, "Tasks")
            .allowMainThreadQueries().build()
        dao = db.taskDao()

        binding.btnAddTask.setOnClickListener {
            val priority = binding.spinner.selectedItem.toString()
            var addedTask = binding.etTask.text.toString()
            if (addedTask == "") {
                Toast.makeText(requireContext(), "Task cannnot be empty", Toast.LENGTH_SHORT).show()
            } else {
                if (priority == "Select") {
                    Toast.makeText(requireContext(), "Select priority", Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().navigate(
                        AddTaskFragmentDirections.actionAddTaskFragmentToDetailsFragment(
                            taskName = addedTask,
                            priority = priority
                        )
                    )
                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}