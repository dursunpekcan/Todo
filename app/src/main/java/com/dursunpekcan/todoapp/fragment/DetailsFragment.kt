package com.dursunpekcan.todoapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.media.tv.TsRequest
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dursunpekcan.todoapp.R
import com.dursunpekcan.todoapp.SwipeCallback
import com.dursunpekcan.todoapp.adapter.TaskAdapter
import com.dursunpekcan.todoapp.dao.TaskDao
import com.dursunpekcan.todoapp.databinding.FragmentDetailsBinding
import com.dursunpekcan.todoapp.db.TaskDatabase
import com.dursunpekcan.todoapp.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: TaskAdapter
    private lateinit var taskList: ArrayList<Task>
    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDao
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        db = Room.databaseBuilder(requireContext(), TaskDatabase::class.java, "Tasks")
            .allowMainThreadQueries().build()
        dao = db.taskDao()
        taskList = arrayListOf()
        binding.fabAdd.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(DetailsFragmentDirections.actionDetailsFragmentToAddTaskFragment())
        }


        arguments?.let {
            var oldTask = DetailsFragmentArgs.fromBundle(it).taskName
            val dataName = DetailsFragmentArgs.fromBundle(it).dataName
            val dataName2 = DetailsFragmentArgs.fromBundle(it).dataName2
            val priority = DetailsFragmentArgs.fromBundle(it).priority
            val updatedPriority = DetailsFragmentArgs.fromBundle(it).updatedPriority
            println(priority)
            println(oldTask)
            if (oldTask != "notask") {
                if (priority != "non") {
                    println("ex")
                    val task = Task(oldTask, priority, false)
                    dao.insert(task)
                }

            } else if (oldTask == "notask" && dataName != "dataName" && dataName2 != "dataName2") {
                dao.update(dataName2, dataName, updatedPriority)

            }
            taskList.addAll(dao.getAllTaskDesc())

            it.putAll(it)
            it.clear()
        }

        adapter = TaskAdapter(taskList, requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        setHasOptionsMenu(true)

        /*       binding.searchView.clearFocus()
        binding.searchView.queryHint="Search..."
        binding.searchView.setOnQueryTextListener(object :android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return true
            }

        })*/
        toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itemHome -> binding.drawerLayout.closeDrawers()
            }
            true
        }

        val swipeToDeleteCallback = object : SwipeCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                dao.delete(taskList[position])
                taskList.removeAt(position)
                adapter.notifyItemRemoved(position)

            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


    }


    private fun filterList(text: String) {
        val filteredList = ArrayList<Task>()
        for (item in taskList) {
            if (item.task.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()) {

        } else {
            adapter.setFilterList(filteredList)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        inflater.inflate(R.menu.menu_sort_options, menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search"

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return false
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortHighPrior -> {
                taskList.sortWith(Comparator<Task> { task1, task2 ->
                    when {
                        task1.priority.length > task2.priority.length -> 2
                        task1.priority.length < task2.priority.length -> -1
                        else -> 0
                    }
                })


                adapter.notifyDataSetChanged()

            }
            R.id.sortLowPrior -> {
                taskList.sortWith(Comparator<Task> { task1, task2 ->
                    when {
                        task1.priority.length > task2.priority.length -> -1
                        task1.priority.length < task2.priority.length -> 1
                        else -> 0
                    }
                })
                adapter.notifyDataSetChanged()
            }

            R.id.itemDeleteAll -> {

                AlertDialog.Builder(requireContext()).setTitle("Delete All")
                    .setMessage("Are you sure to delete all tasks?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        taskList.clear()
                        dao.deleteAll()
                        adapter.notifyDataSetChanged()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    }).show()
            }

            R.id.sortByDateEarly->{
                taskList.sortWith(Comparator<Task> { task1, task2 ->
                    when {
                        task1.id > task2.id -> 1
                        task1.id< task2.id -> -1
                        else -> 0
                    }
                })
                adapter.notifyDataSetChanged()
            }

            R.id.sortByDateLate->{
                taskList.sortWith(Comparator<Task> { task1, task2 ->
                    when {
                        task1.id > task2.id -> -1
                        task1.id< task2.id -> 1
                        else -> 0
                    }
                })
                adapter.notifyDataSetChanged()
            }

        }
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}