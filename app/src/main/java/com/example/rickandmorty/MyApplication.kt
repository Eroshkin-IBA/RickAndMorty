package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.dao.AppDatabase


class MyApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDataBase(this) }
}