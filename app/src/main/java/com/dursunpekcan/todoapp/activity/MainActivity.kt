package com.dursunpekcan.todoapp.activity
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dursunpekcan.todoapp.databinding.ActivityMainBinding
import com.dursunpekcan.todoapp.fragment.AddTaskFragment
import com.dursunpekcan.todoapp.fragment.DetailsFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


    }










}