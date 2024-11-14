package com.example.loginapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.AdapterView
import android.view.View
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class IngresarVentaActivity : AppCompatActivity() {

    private lateinit var etMonto: EditText
    private lateinit var spinnerProducto: Spinner
    private lateinit var spinnerMedioPago: Spinner
    private lateinit var btnRegistrarVenta: Button

    private lateinit var db: DatabaseHelper
    private var productos: List<Producto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar_venta)

        // Asociar vistas utilizando findViewById
        etMonto = findViewById(R.id.etMonto)
        spinnerProducto = findViewById(R.id.spinnerProducto)
        spinnerMedioPago = findViewById(R.id.spinnerMedioPago)
        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta)

        // Inicializar el DatabaseHelper
        db = DatabaseHelper(this)

        // Crear un ArrayAdapter para el Spinner de medios de pago
        val mediosDePago = arrayOf("Efectivo", "Tarjeta", "Transferencia", "Otro")
        val adapterMedioPago = ArrayAdapter(this, R.layout.spinner_item, mediosDePago)
        adapterMedioPago.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerMedioPago.adapter = adapterMedioPago

        // Cargar los productos de la base de datos de forma asíncrona
        cargarProductos()

        // Configurar el Spinner para actualizar el monto automáticamente
        spinnerProducto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val productoSeleccionado = productos[position]
                etMonto.setText(productoSeleccionado.precio.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                etMonto.setText("")
            }
        }

        // Configurar el botón para registrar la venta
        btnRegistrarVenta.setOnClickListener {
            val producto = spinnerProducto.selectedItem.toString()
            val monto = etMonto.text.toString().toDoubleOrNull()
            val medioPagoSeleccionado = spinnerMedioPago.selectedItem.toString()

            if (producto.isNotEmpty() && monto != null) {
                // Obtener la fecha actual
                val fechaActual = obtenerFechaActual()

                // Registrar la venta en la base de datos
                val result = db.insertarVenta(producto, monto, fechaActual, medioPagoSeleccionado)

                if (result != -1L) {
                    Toast.makeText(this, "Venta registrada correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar la venta.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Cargar los productos de la base de datos de manera asíncrona
    private fun cargarProductos() {
        // Ejecutar la consulta en un hilo en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            productos = db.obtenerProductos()  // Recuperar productos de la base de datos

            // Regresar a la UI para actualizar el Spinner
            withContext(Dispatchers.Main) {
                // Crear el adapter para el Spinner
                val adapterProducto = ArrayAdapter(this@IngresarVentaActivity, R.layout.spinner_item, productos.map { it.nombre })
                adapterProducto.setDropDownViewResource(R.layout.spinner_dropdown_item)
                spinnerProducto.adapter = adapterProducto
            }
        }
    }

    // Método para obtener la fecha actual en formato yyyy-MM-dd
    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())  // Retorna la fecha actual
    }
}
