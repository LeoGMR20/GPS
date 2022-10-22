package com.example.gps

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gps.databinding.ActivityPersistenciaBinding

class PersistenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersistenciaBinding

    //Atributos para trabajar con persistencia al estilo Android

    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}