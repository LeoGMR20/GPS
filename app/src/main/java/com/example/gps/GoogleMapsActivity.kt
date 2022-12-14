package com.example.gps

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.gps.Coordenadas.cossmil
import com.example.gps.Coordenadas.hospitalHobrero
import com.example.gps.Coordenadas.lapaz
import com.example.gps.Coordenadas.plazaMurillo
import com.example.gps.Coordenadas.stadium
import com.example.gps.Coordenadas.torresMall
import com.example.gps.Coordenadas.univalle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.gps.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    //Es el objeto que va a contener a su mapa de Google
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.binding = binding

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        //El mapa de Google se carga asincronamente
        //sin congelar la pantalla o el hilo principal
        mapFragment.getMapAsync(this)
        setupToggleButtons()
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
    Aqu?? ocurre toda la magia
    cuando el mapa esta listo...
    cuando el mapa esta listo, les devuelve
    en un par??metro llamda googleMap, el mapa
    y todas sus caracteristicas en ese objeto
    */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Las posiciones se manejan en un objeto
        //que conjunciona latitud y longitud
        //se llama LatLng
        val salarUyuni = LatLng(-20.152120, -67.611300)

        /**
         *
         * InfoWindow custom
         *
         * */

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

        //Marcador
        //Tachuela roja que se posiciona en el mapa donde quieren ubicarse
        mMap.addMarker(MarkerOptions()
            .title("Salar de Uyuni")
            .snippet("${salarUyuni.latitude},${salarUyuni.longitude}") //Contenido extra
            .position(salarUyuni)
        )
        /**
         * Delimintar el zoom permitido en el mapa
         * */

        mMap.apply {
            setMinZoomPreference(15f)
            setMaxZoomPreference(20f)
        }

        //Colocar la c??mara virtual en la posici??n requerida
        //en el mapa
        //La c??mara se centra o coloca tus coordenadas
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
        * Configuraci??n personalizada de c??mara
        */

        val camaraPersonalizada = CameraPosition.Builder()
            .target(univalle)
            .zoom(17f)
            .tilt(45f) //??ngulo de inclinaci??n de la c??mara
            .bearing(245f) //??ngulo para cambio de orientaci??n del norte
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPersonalizada))

        /*
         * Movimiento de la c??mara
         * usando corrutinas: similares a hilos o procesos en background
        */

        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle,17f))

        //Uso de corrutinas....

        lifecycleScope.launch {
            delay(4000)
            //transici??n de movimiento entre dos coordenadas
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
        * Movimiento de la c??mara por pixeles en pantalla
        */

        /*lifecycleScope.launch {
            delay(5000)
            for (i in 0..50) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(150f,120f))
                delay(500)
            }
        }*/

        /**
        * Limitaci??n de ??rea de acci??n del mapa
        * usando sesgos de coordenadas de acci??n
         * esta caracteristica de mapear un ??rea de acci??n
         * se conoce como bounds
        */

        //Bounds necesita dos posiciones: una surOeste y otra noreste que delimintan tu ??rea de acci??n
        val lapazBounds = LatLngBounds(torresMall,hospitalHobrero)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz,12f))
        lifecycleScope.launch{
            delay(3_500)
            //De la ??rea que has delimintado tu puedes acceder al punto central del rect??ngulo imaginario
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lapazBounds.center,18f))
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lapazBounds, 32))
        }
        mMap.setLatLngBoundsForCameraTarget(lapazBounds)

        /**
         * Establecer los controles de UI del mapa y las Gestures
         */

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true // Botones + - zoom in zoom out
            isCompassEnabled = true // la br??jula de orientaci??n del mapa
            isMapToolbarEnabled = true // habilito para un marcador la opci??n de ir a ver una ruta a verlo en la app Mapa Google
            isRotateGesturesEnabled = false // deshabilitar la opci??n de rotaci??n del mapa
            isTiltGesturesEnabled = false // deshabilitar la opci??n de rotaci??n de la c??mara
            isZoomControlsEnabled = false // deshabilita las opciones de zoom con los dedos del mapa
        }

        //establecer un padding al mapa

        mMap.setPadding(0,0,0,Utils.dp(64))// densidad de pixeles en pantalla

        /**
         * Estilo personalizado de mapa
         */

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.my_map_style))

        /**
         * configuracion y personalizacion de marcadores
         */

        val univalleMarcador = mMap.addMarker(MarkerOptions()
            .title("Mi universidad")
            .position(univalle)
        )

        univalleMarcador?.run {
            //setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) //Cambiar color marcador con opciones pro defecto
            //setIcon(BitmapDescriptorFactory.defaultMarker(86f)) // cambiar color Personalizado
            //setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker)) // Cambiar marcador con dise??o personalizado
            Utils.getBitmapfromVector(this@GoogleMapsActivity, R.drawable.ic_baseline_directions_car_24)?.let {
                setIcon(BitmapDescriptorFactory.fromBitmap(it))
            }
            rotation = 145f
            isFlat = true // el marcador rote o no con el mapa
            setAnchor(0.5f,0.5f)
            isDraggable = true
        }

        //Eventos en marcadores

        mMap.setOnMarkerClickListener(this)
        //Cuando la interfaz a implementar tiene muchos m??todos
        //mejor haganlo de la forma tradicional
        mMap.setOnMarkerDragListener(this)

        /**
         * Trazado de linea areas y circulos en el mapa
         * trazar un l??nea entre dos puntos se llama Polyline
         */

        val misRutas1 = mutableListOf(univalle, stadium, hospitalHobrero)
        val misRutas2 = mutableListOf(univalle, hospitalHobrero, stadium)
        val misRutas3 = mutableListOf(hospitalHobrero, stadium, plazaMurillo)

        //setupPolyline(misRutas1)
        //setupSecondPolyline()

        /**
         * Trazado de areas en el mapa a partir de un forma poligonal
         *
         */

        setupPolygonArea()

        //trazar rutas en tiempo real simulando movimiento
        /*lifecycleScope.launch {
            delay(3_500)
            setupPolylineTime(misRutas1)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalHobrero,13f))
            delay(3_500)
            setupPolylineTime(misRutas2)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium,13f))
            delay(3_500)
            setupPolylineTime(misRutas3)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plazaMurillo,13f))
        }*/

        //Los mapas tienen eventos, como los botones.
        //Se configura listeners que escuchen esos eventos
        //y resuelvan una acci??n ante ese evento
        //El evento m??s sencillo e importante en los mapas
        //es el click a cualquier parte del mapa de google

        mMap.setOnMapClickListener {
            //it es la posici??n donde haces click con tu dedo
            mMap.addMarker(MarkerOptions()
                .title("Nueva ubicaci??n Random")
                .snippet("${it.latitude},\n${it.longitude}")
                .position(it)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            )
        }
    }

    private fun setupPolygonArea() {
        val polygon = mMap.addPolygon(PolygonOptions()
            .geodesic(true)
            .clickable(true)
            .fillColor(Color.WHITE)//Color de relleno en el ??rea
            .strokeColor(Color.RED) //Color de la linea que delimita el area
            .add(univalle, stadium, hospitalHobrero)// Indicas los puntos que van a delimitar el trazo de tu l??nea
        )
    }

    private fun setupPolyline(ruta: MutableList<LatLng>) {
        //las l??neas Polyline dependen de un arreglo o lista de coordenadas
        val polyline = mMap.addPolyline(PolylineOptions()
            .color(Color.BLUE)
            .width(10f) //ancho de la l??nea
            .clickable(true) //la l??nea debe ser clickeada
            .geodesic(true) //curvatura con respecto al radio de la tierra
        )
        polyline.points = ruta

        lifecycleScope.launch{
            val misRutasEnTiempoReal = mutableListOf<LatLng>()
            for (punto in ruta){
                misRutasEnTiempoReal.add(punto)
                polyline.points = misRutasEnTiempoReal
                delay(2_000)
            }
        }
        //Poner informacion o una descripcion en la linea
        polyline.tag = "Ruta Univalle a Hospital Obrero"
        //configuraci??n del estilo de la uniones entre l??neas
        polyline.jointType = JointType.ROUND
        //polyline.width = 100f
        //Como configuran el patr??n de la forma de la l??nea
        // 1) l??nea continua
        // 2) l??nea punteada
        // 3) segmentos de l??nea
        polyline.pattern = listOf(Dot(), Gap(32f), Dash(64f))
    }

    private fun setupSecondPolyline() {
        val miRutaPersonalizada = mutableListOf(cossmil, torresMall)
        val secondPolyline = mMap.addPolyline(PolylineOptions()
            .color(Color.CYAN)
            .width(15f)
            .clickable(true)
            .geodesic(true)
            .jointType(JointType.ROUND)
        )
        secondPolyline.tag = "Mi ruta de la operaci??n a Torres Mall, hogar de Menacho"
        secondPolyline.points = miRutaPersonalizada
        //Configurar un evento de click sobre la l??nea
        mMap.setOnPolylineClickListener {
            Toast.makeText(this, "${it.tag}",Toast.LENGTH_LONG).show()
        }

        //a la l??nea se le puede configurar como quieren el aspecto de la termininaci??n y el inicio de la l??nea
        /*secondPolyline.startCap = RoundCap() //Default = Buttcap()
        secondPolyline.endCap = SquareCap()*/
        //Poner iconos que indiquen el inicio y fin de su linea
        Utils.getBitmapfromVector(this, R.drawable.ic_baseline_directions_car_24)?.let {
            secondPolyline.startCap = CustomCap(BitmapDescriptorFactory.fromBitmap(it))
        }
        Utils.getBitmapfromVector(this, R.drawable.ic_baseline_boy_24)?.let {
            secondPolyline.startCap = CustomCap(BitmapDescriptorFactory.fromBitmap(it))
        }
    }

    /*private fun setupPolyline() {
        //las l??neas Polyline dependen de un arreglo o lista de coordenadas
        val misRutas = mutableListOf(univalle, stadium, hospitalHobrero)
        val polyline = mMap.addPolyline(PolylineOptions()
            .color(Color.BLUE)
            .width(10f) //ancho de la l??nea
            .clickable(true) //la l??nea debe ser clickeada
            .geodesic(true) //curvatura con respecto al radio de la tierra
        )
        lifecycleScope.launch {
            for (ruta in misRutas) {
                delay(3_500)
                polyline.points.add(ruta)
            }
        }
    }*/

    private fun setupToggleButtons() {
        binding.toggleGroup.addOnButtonCheckedListener{
                group, checkedId, isChecked ->
            if (isChecked) {
                mMap.mapType = when(checkedId) {
                    R.id.btnNormal -> GoogleMap.MAP_TYPE_NORMAL
                    R.id.btnHibrido -> GoogleMap.MAP_TYPE_HYBRID
                    R.id.btnSatelital -> GoogleMap.MAP_TYPE_SATELLITE
                    R.id.btnTerreno -> GoogleMap.MAP_TYPE_TERRAIN
                    else -> GoogleMap.MAP_TYPE_NONE
                }
            }
        }
    }

    //Click al marcador
    override fun onMarkerClick(marker: Marker): Boolean {
        //marker es el marcador al que le estas haciendo click
        Toast.makeText(this, "${marker.position.latitude}, ${marker.position.longitude}", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMarkerDrag(marker: Marker) {
        //mientras arrastras el marcador
        //ocultas el menu de botones y haces transparente el marcador
        binding.toggleGroup.visibility = View.INVISIBLE
        marker.alpha = 0.4f
    }

    override fun onMarkerDragEnd(marker: Marker) {
        //Cuando sueltas el marcador
        binding.toggleGroup.visibility = View.VISIBLE
        marker.alpha = 1.0f
        //los marcadores tienen una ventan de informaci??n
        //se le llama infoWindow
        marker.showInfoWindow()
    }

    override fun onMarkerDragStart(marker: Marker) {
        //cuando empiezas a arrastrar el marcador
        marker.hideInfoWindow()//oculta la ventana de informaci??n
    }
}