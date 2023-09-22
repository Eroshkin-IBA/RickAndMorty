package com.example.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController: NavController = Navigation.findNavController(this,R.id.main_container)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}