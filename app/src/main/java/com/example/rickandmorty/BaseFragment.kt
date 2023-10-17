package com.example.rickandmorty

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.rickandmorty.network.isOnline

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(activity.findViewById(R.id.toolbar))


        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                val navController = NavHostFragment.findNavController(this)

                navController.navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun noInternetMessage(){
        if (!isOnline(requireContext())) {
            Toast.makeText(
                requireContext(),
                "there is no connection",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}