package com.example.gps

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.example.gps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabGPS.setOnClickListener{
            enableGPSServices()
        }
    }

    //Evaluar y gestionar si el GPS en el celular está Activo

    private fun enableGPSServices() {
        AlertDialog.Builder(this)
            .setTitle(R.string.alert_dialog_title)
            .setMessage(R.string.alert_dialog_description)
            .setPositiveButton(R.string.alert_dialog_button_accept,
                DialogInterface.OnClickListener{
                    dialog, wich -> goToEnableGPS()
                })
            .setNegativeButton(R.string.alert_dialog_button_denny) {
                dialog, wich -> isGPSEnabled = false
            }
            .setCancelable(true)
            .show()
    }

    private fun goToEnableGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
}