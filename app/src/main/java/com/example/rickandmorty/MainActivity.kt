package com.example.rickandmorty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentFragmentId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        navController = Navigation.findNavController(this, R.id.main_container)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.characterFragment,
                R.id.episodeFragment,
                R.id.locationFragment,
                R.id.characterDetails,
                R.id.episodeDetails,
                R.id.locationDetailsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val id = item.itemId
            if (id == currentFragmentId) {
                return@setOnItemSelectedListener false
            }
            currentFragmentId = id
            when (id) {
                R.id.character_nav_graph -> navController.navigate(R.id.character_nav_graph)
                R.id.episode_nav_graph -> navController.navigate(R.id.episode_nav_graph)
                R.id.location_nav_graph -> navController.navigate(R.id.location_nav_graph)
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.main_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}