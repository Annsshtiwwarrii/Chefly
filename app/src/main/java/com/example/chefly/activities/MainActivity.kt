package com.example.chefly.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.chefly.R
import com.example.chefly.db.MealDatabase
import com.example.chefly.viewModel.HomeViewModel
import com.example.chefly.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
     val viewModel:HomeViewModel by lazy {
         val mealDatabase = MealDatabase.getInstance(this)
         val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
         ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)
       NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
}