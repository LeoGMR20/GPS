package com.example.gps

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.gps.Constantes.KEY_MAT
import com.example.gps.Constantes.KEY_NAME
import com.example.gps.Constantes.KEY_USER
import com.example.gps.Constantes.KEY_VAL
import com.example.gps.databinding.ActivityPersistenciaBinding

class PersistenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersistenciaBinding

    //Atributos para trabajar con persistencia al estilo Android

    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var materias = arrayOf(
        "Seleccione una materia",
        "Programación Móvil II",
        "Programación Web III",
        "Bases de Datos III")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        popularSpinnerMaterias()
        initializeSharedPreference()
        loadData()
        binding.btnGuardar.setOnClickListener {
            saveData()
            loadData()
        }
    }

    private fun popularSpinnerMaterias() {
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,materias)
        binding.spMateriaFav.adapter = adapter
    }

    //Van a configurar todo lo necesario para usar su archivo persistente.
    //Primero se va a buscar ese archivo, si no existe lo va a crear
    //pero si existe va a traer ese archivo
    private fun initializeSharedPreference() {
        //MODE_PRIVATE = solo tu App puede usar el archivo
        sharedPreference = getSharedPreferences("datos", MODE_PRIVATE)
        //estan abriendo el archivo con permisos de escritura en una variable llamada editor
        editor = sharedPreference.edit()
    }

    private fun saveData() {
        val nombreCompleto = binding.etNombreCompleto.text.toString()
        var materiaFav = binding.spMateriaFav.selectedItem.toString()
        if (materiaFav == "Seleccione una materia"){
            materiaFav = "No seleccionado"
        }
        val valoracion = Integer.parseInt(binding.etVal.text.toString())
        //Cada dato se guarda en un registro
        //en formato CLAVE - VALOR
        //Si el registro con esa llave no existe lo crea
        //si el registro ya existe con esa llave lo reemplaza
        editor.apply{
            putString(KEY_NAME, nombreCompleto)
            putString(KEY_MAT,materiaFav)
            putInt(KEY_VAL,valoracion)
            putBoolean(KEY_USER, true)
        }.apply()
        //commit() ... guardado síncrono
        //apply() ... guardado asíncrono
    }

    private fun loadData() {
        val nombre = sharedPreference.getString(KEY_NAME, "Vacío")
        val matFav = sharedPreference.getString(KEY_MAT, "No seleccionado")
        val valo = sharedPreference.getInt(KEY_VAL,0)
        binding.tvPersistencia.text = """
            Nombre: $nombre
            Materia favorita: $matFav
            Valoración: $valo
        """.trimIndent()
    }
}