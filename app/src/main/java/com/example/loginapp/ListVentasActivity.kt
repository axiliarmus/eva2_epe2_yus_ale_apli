package com.example.loginapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ListVentasActivity : AppCompatActivity() {

    private lateinit var etFecha: EditText
    private lateinit var btnFiltrarVentas: Button
    private lateinit var listViewVentas: ListView
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ventas)

        // Inicializar las vistas
        etFecha = findViewById(R.id.etFecha)
        btnFiltrarVentas = findViewById(R.id.btnFiltrarVentas)
        listViewVentas = findViewById(R.id.listViewVentas)

        // Inicializar la base de datos
        db = DatabaseHelper(this)

        // Configurar el campo de fecha para mostrar un DatePickerDialog
        etFecha.setOnClickListener {
            mostrarDatePickerDialog()
        }

        // Configurar el botón de filtro
        btnFiltrarVentas.setOnClickListener {
            val fechaInput = etFecha.text.toString()

            // Verificar si la fecha es válida
            if (isValidDate(fechaInput)) {
                // Filtrar las ventas por fecha
                val ventasFiltradas = db.getVentasPorFecha(fechaInput)

                // Verificar si se encontraron ventas
                if (ventasFiltradas.isNotEmpty()) {
                    // Calcular el total de las ventas
                    val totalVentas = ventasFiltradas.sumOf { it.precio }

                    // Crear un listado con solo la información relevante (nombre y precio)
                    val ventasMostrar = ventasFiltradas.map {
                        VentaMostrar(
                            it.nombre,
                            it.precio,
                            it.fecha,
                            it.medioPago
                        )
                    }

                    // Crear un listado con el total
                    val totalItem = VentaMostrar("Total", totalVentas, "", "")
                    val ventasConTotal = ventasMostrar + totalItem

                    // Crear un adaptador personalizado
                    val adapter = VentaAdapter(this, R.layout.venta_item, ventasConTotal)
                    listViewVentas.adapter = adapter
                } else {
                    Toast.makeText(this, "No se encontraron ventas para la fecha ingresada.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa una fecha válida (yyyy-MM-dd)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para mostrar el DatePickerDialog
    private fun mostrarDatePickerDialog() {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        // Crear el DatePickerDialog con el tema personalizado
        val datePickerDialog = DatePickerDialog(
            this, R.style.DatePickerDialogTheme, { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear la fecha seleccionada
                val fechaSeleccionada = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                etFecha.setText(fechaSeleccionada) // Establecer la fecha en el EditText
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    // Función para validar el formato de la fecha
    private fun isValidDate(date: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}

data class VentaMostrar(
    val nombre: String,
    val precio: Double,
    val fecha: String,
    val medioPago: String
)
