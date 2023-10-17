package com.example.rickandmorty

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration

import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.rickandmorty.dao.AppDatabase
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.network.isOnline
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.main_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.characterFragment -> {
                // Перейдите к CharacterFragment
                navController.navigate(R.id.characterFragment)
                return true
            }
            R.id.locationFragment -> {
                // Перейдите к LocationFragment
                navController.navigate(R.id.locationFragment)
                return true
            }
            R.id.episodeFragment -> {
                // Перейдите к EpisodeFragment
                navController.navigate(R.id.episodeFragment)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}