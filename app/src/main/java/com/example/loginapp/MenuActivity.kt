package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    lateinit var ingresarVentaBtn: Button
    lateinit var listarVentasBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)  // Asegúrate de que este sea el nombre correcto del layout

        ingresarVentaBtn = findViewById(R.id.ingresar_venta_btn)
        listarVentasBtn = findViewById(R.id.listar_ventas_btn)

        // Redirigir a la actividad de ingresar venta
        ingresarVentaBtn.setOnClickListener {
            val intent = Intent(this, IngresarVentaActivity::class.java)
            startActivity(intent)
        }

        // Redirigir a la actividad de listar ventas
        listarVentasBtn.setOnClickListener {
            val intent = Intent(this, ListVentasActivity::class.java)
            startActivity(intent)
        }
    }
}
