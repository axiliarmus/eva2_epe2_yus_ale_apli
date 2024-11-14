package com.example.loginapp  // Usa el paquete en el que estás trabajando

data class Venta(
    val id: Int,               // ID de la venta
    val nombre: String,        // Nombre del producto vendido
    val precio: Double,        // Precio de la venta
    val fecha: String,         // Fecha de la venta en formato yyyy-MM-dd
    val medioPago: String      // Método de pago
) {
    // Método para mostrar la fecha en formato legible dd/MM/yyyy
    fun getFormattedFecha(): String {
        val parts = fecha.split("-")
        return "${parts[2]}/${parts[1]}/${parts[0]}"
    }
}
