package com.example.gps

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.gps.Coordenadas.hospitalHobrero
import com.example.gps.Coordenadas.lapaz
import com.example.gps.Coordenadas.plazaEstudiante
import com.example.gps.Coordenadas.plazaMurillo
import com.example.gps.Coordenadas.plazaSanPedro
import com.example.gps.Coordenadas.plazaSpain
import com.example.gps.Coordenadas.sanFrancisco
import com.example.gps.Coordenadas.stadium
import com.example.gps.Coordenadas.torresMall
import com.example.gps.Coordenadas.univalle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gps.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds
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
            .tilt(45f) //ángulo de inclinación de la cámara
            .bearing(245f) //ángulo para cambio de orientación del norte
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPersonalizada))

        /*
         * Movimiento de la cámara
         * usando corrutinas: similares a hilos o procesos en background
        */

        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle,17f))

        //Uso de corrutinas....

        lifecycleScope.launch {
            delay(4000)
            //transición de movimiento entre dos coordenadas
            //similar movimiento en el mapa
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stadium, 17f))
            delay(3000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaMurillo, 17f))
            delay(3000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sanFrancisco, 17f))
            delay(3000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaSanPedro, 17f))
            delay(3000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaEstudiante, 17f))
            delay(3000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaSpain, 17f))
            delay(3000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(torresMall, 17f))
            mMap.addMarker(MarkerOptions()
                .title("Destino final")
                .snippet("${torresMall.latitude},${torresMall.longitude}") //Contenido extra
                .position(torresMall)
            )
        }*/

        /*
        * Movimiento de la cámara por pixeles en pantalla
        */

        /*lifecycleScope.launch {
            delay(5000)
            for (i in 0..50) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(150f,120f))
                delay(500)
            }
        }*/

        /**
        * Limitación de área de acción del mapa
        * usando sesgos de coordenadas de acción
         * esta caracteristica de mapear un área de acción
         * se conoce como bounds
        */

        //Bounds necesita dos posiciones: una surOeste y otra noreste que delimintan tu área de acción
        val lapazBounds = LatLngBounds(torresMall,hospitalHobrero)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz,12f))
        lifecycleScope.launch{
            delay(3_500)
            //De la área que has delimintado tu puedes acceder al punto central del rectángulo imaginario
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lapazBounds.center,18f))
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lapazBounds, 32))
        }
        mMap.setLatLngBoundsForCameraTarget(lapazBounds)

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