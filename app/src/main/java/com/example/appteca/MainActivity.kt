package com.example.appteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: AppAdapter
    private val vm: AppTecaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = AppAdapter(emptyList(),
            onAppClick = { app ->
                val intent = Intent(this, DetalleActivity::class.java)
                intent.putExtra("appId", app.id)
                startActivity(intent)
            },
            onFavoritoClick = { app -> vm.alternarFavorita(app) })

        val rv = findViewById<RecyclerView>(R.id.rvApps)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<EditText>(R.id.etBuscar).addTextChangedListener { texto ->
            vm.buscar(texto.toString())
        }

        findViewById<Button>(R.id.btnSoloFav).setOnClickListener {
            vm.alternarModo()
        }

        // ── LAS suscripciones: el único lugar donde el estado toca la pantalla

        vm.listaVisible.observe(this) { lista ->
            adapter.actualizarLista(lista)
        }

        vm.modoSoloFavoritas.observe(this) { activo ->
            findViewById<Button>(R.id.btnSoloFav).text =
                if (activo) " Solo favoritas" else "☆ Todas"
        }
    }
}