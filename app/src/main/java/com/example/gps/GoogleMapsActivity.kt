package com.example.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.gps.Coordenadas.stadium
import com.example.gps.Coordenadas.univalle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gps.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //Es el objeto que va a contener a su mapa de Google
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        //El mapa de Google se carga asincronamente
        //sin congelar la pantalla o el hilo principal
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /*
    Aquí ocurre toda la magia
    cuando el mapa esta listo...
    cuando el mapa esta listo, les devuelve
    en un parámetro llamda googleMap, el mapa
    y todas sus caracteristicas en ese objeto
    */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Las posiciones se manejan en un objeto
        //que conjunciona latitud y longitud
        //se llama LatLng
        val salarUyuni = LatLng(-20.152120, -67.611300)

        //Marcador
        //Tachuela roja que se posiciona en el mapa donde quieren ubicarse
        mMap.addMarker(MarkerOptions()
            .title("Salar de Uyuni")
            .snippet("${salarUyuni.latitude},${salarUyuni.longitude}") //Contenido extra
            .position(salarUyuni)
        )

        //Colocar la cámara virtual en la posición requerida
        //en el mapa
        //La cámara se centra o coloca tus coordenadas
        //en el centro de la pantalla

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salarUyuni,10f))
        /*
        El zoom en el mapa va de un rango [0,21)
        se le puede asignar desde 2 a 20
        a partir de 5: continentes
        a partir de 10: ciudades o paises
        a partir de 15: se usa para calles avenidas
        20: sirve para edificios, casas, parques, domicilios
        */

        /*
        * Configuración personalizada de cámara
        */

        val camaraPersonalizada = CameraPosition.Builder()
            .target(univalle)
            .zoom(17f)
            .tilt(35f) //ángulo de inclinación de la cámara
            .bearing(235f) //ángulo para cambio de orientación del norte
            .build()
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPersonalizada))

        /*
         * Movimiento de la cámara
         * usando corrutinas: similares a hilos o procesos en background
        */

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle,17f))

        //Uso de corrutinas....

        lifecycleScope.launch {
            delay(5000)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium, 17f))
        }

        //Los mapas tienen eventos, como los botones.
        //Se configura listeners que escuchen esos eventos
        //y resuelvan una acción ante ese evento
        //El evento más sencillo e importante en los mapas
        //es el click a cualquier parte del mapa de google

        mMap.setOnMapClickListener {
            //it es la posición donde haces click con tu dedo
            mMap.addMarker(MarkerOptions()
                .title("Nueva ubicación Random")
                .snippet("${it.latitude},${it.longitude}")
                .position(it)
                .draggable(true)
            )
        }
    }
}