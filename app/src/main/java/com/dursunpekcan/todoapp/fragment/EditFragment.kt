package com.dursunpekcan.todoapp.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.dursunpekcan.todoapp.R
import com.dursunpekcan.todoapp.databinding.FragmentEditBinding
import com.dursunpekcan.todoapp.util.showKeyboard


class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etTask.requestFocus()
        activity?.showKeyboard() 
        arguments?.let {
            val data = EditFragmentArgs.fromBundle(it).data0
            var priority = EditFragmentArgs.fromBundle(it).priority
            binding.etTask.setText(data)
            when (priority) {
                "High" -> binding.spinner.setSelection(1)
                "Medium" -> binding.spinner.setSelection(2)
                "Low" -> binding.spinner.setSelection(3)
            }
            binding.btnEditButton.setOnClickListener {

                if (binding.etTask.text.isEmpty()) {
                    Toast.makeText(requireContext(), "Task cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (binding.spinner.selectedItemId == 0L) {
                        Toast.makeText(requireContext(), "Select priority", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Navigation.findNavController(it)
                            .navigate(
                                EditFragmentDirections.actionEditFragmentToDetailsFragment(
                                    dataName = binding.etTask.text.toString(),
                                    dataName2 = data,
                                    updatedPriority = binding.spinner.selectedItem.toString()
                                )
                            )
                    }
                }
            }
            it.clear()
        }


    }


}


