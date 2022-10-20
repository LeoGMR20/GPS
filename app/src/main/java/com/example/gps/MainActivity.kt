package com.example.gps

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gps.databinding.ActivityMainBinding
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    //OJO: esto no es extremamente necesario para nuestra variable
    //solo estoy enseñando que es compañion object
    //Compañion object = para definir constantes que sean a un nivel global en su clase
    // constantes que ustedes no requieran inicializar en cada instancia de su clase
    // Sirve para deifnir constantes globales de acceso genral que quieres tener en tu clase
    companion object {
        val REQUIERED_PERMISSION_GPS = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
    }

    private lateinit var binding: ActivityMainBinding
    private var isGPSEnabled = false
    private val PERMISSION_ID = 42
    //Variable que vamos a usar para gestionar el GPS con google play services
    //FusedLocation: fusionar los datos respectivos a GPS en un objeto
    private lateinit var fusedLocation : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabGPS.setOnClickListener{
            enableGPSServices()
        }
        binding.fabCoords.setOnClickListener {
            manageLocation()
        }
    }

    /*
        * Seccion: Tratamioento de localizacion
        * obtencion de coordenadas
    */
    @SuppressLint("MissingPermission")
    private fun manageLocation() {
        if (hasGPSEnabed()){
            if (allPermissionsGrantedGPS()) {
                //solo puede ser tratado si el usuario dio permisos
                fusedLocation = LocationServices.getFusedLocationProviderClient(this)
                //Estan configurando un evento que escuche cuando
                // del sensor GPS se captura datos correctamente
                fusedLocation.lastLocation.addOnSuccessListener {
                        location -> requestNewLocationData()
                }
            }else{
                requestPermissionsLocation()
            }
        }else{
            goToEnableGPS()
        }
    }

    //OJO: solo usar si estas completamente seguro de que en este punto
    //garantizas al 100% que el usuario ha dado permisos
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        //configurar las caracteristicas de nuestra peticion de localizacion
        //TODO revisar la actualización a versión 21 con método create()
        var myLocationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval  = 0
            numUpdates = 1
        }
        fusedLocation.requestLocationUpdates(myLocationRequest, myLocationCallback, Looper.myLooper())
    }

    private val myLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var myLastLocation: Location? = locationResult.lastLocation
            if(myLastLocation != null) {
                binding.apply {
                    tvLat.text = myLastLocation.latitude.toString()
                    tvLong.text = myLastLocation.longitude.toString()
                }
            }
        }
    }

    //Evaluar y gestionar si el GPS en el celular está Activo

    private fun enableGPSServices() {
        if(!hasGPSEnabed()){
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
        } else
            Toast.makeText(this,"Ya tienes el GPS habilitado", Toast.LENGTH_SHORT).show()
    }

    private fun hasGPSEnabed(): Boolean {
        //Castear el tipo de dato
        //Managers en Android = director de la orquesta (hombre de la batuta)
        //LocationManager: orquesta o gestiona o cotrola tod0 lo requerido a localizacion
        //desde el ambito del uso de las librerias de Android para localizacion
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun goToEnableGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    //Seccion: tratamiento de permisos para el uso del GPS
    private fun allPermissionsGrantedGPS(): Boolean {
        //checkSelfPermissions: revisa en tu APP que valor tienen los permisos que estas
        //Consultando... ejemplo para GPS ACCESS_FINE_LOCATION que valor tiene en tu App ese permiso
        //Package manager.PERMISON_GRANDED = contiene el valor que android considera
        //como permiso otorgado .... 0
        //si tu permiso en la app tiene el valor de lo que android considera permiso otorgado
        //tu si podras usar el GPS en esta APP :)
        return REQUIERED_PERMISSION_GPS.all {
            ContextCompat.checkSelfPermission( baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    //Este metodo hace lo mismo que el anterior pero es mas explicito en su forma de desarollo
    private fun checkPermissionsGPS(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    //ESTAN HACIENDO QUE LA APP SOLICITE PERMISOS PARA QUE EL USUARIO DECIDA ACEPTAR O DENEGAR
    private fun requestPermissionsLocation() {
        ActivityCompat.requestPermissions(this, REQUIERED_PERMISSION_GPS, PERMISSION_ID)
    }
}