package com.example.rickandmorty.character.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmorty.network.response.Character


class CharacterDetails : Fragment() {

    private lateinit var binding: FragmentCharacterDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val character = arguments?.getSerializable("character") as Character
        binding.characterImg.load(character.image) {
            crossfade(true)
            crossfade(1000)
        }
        binding.name.text = "Имя: " + character.name

        // Inflate the layout for this fragment
        return view
    }

}