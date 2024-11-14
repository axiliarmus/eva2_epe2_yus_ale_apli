package com.example.loginapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.graphics.Color

class VentaAdapter(context: Context, resource: Int, objects: List<VentaMostrar>) :
    ArrayAdapter<VentaMostrar>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.venta_item, parent, false)

        val venta = getItem(position)

        val nombreTextView: TextView = view.findViewById(R.id.textViewNombre)
        val precioTextView: TextView = view.findViewById(R.id.textViewPrecio)
        val fechaTextView: TextView = view.findViewById(R.id.textViewFecha)
        val medioPagoTextView: TextView = view.findViewById(R.id.textViewMedioPago)

        // Asignar los valores al View
        nombreTextView.text = venta?.nombre
        precioTextView.text = "$${"%.2f".format(venta?.precio)}" // Formato del precio con 2 decimales
        fechaTextView.text = venta?.fecha
        medioPagoTextView.text = venta?.medioPago

        // Si es el total, cambiamos el estilo para diferenciarlo
        if (venta?.nombre == "Total") {
            nombreTextView.setTextColor(Color.WHITE)
            precioTextView.setTextColor(Color.WHITE)
            fechaTextView.text = ""
            medioPagoTextView.text = ""
        }

        return view
    }
}

