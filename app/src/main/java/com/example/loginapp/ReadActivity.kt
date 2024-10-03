package com.example.loginapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReadActivity : AppCompatActivity() {

    private lateinit var userListView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userList: List<Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read) // Asegúrate de que este nombre coincida con el archivo XML

        userListView = findViewById(R.id.user_list_view) // Debe coincidir con el ID en el XML
        databaseHelper = DatabaseHelper(this)

        // Obtener todos los usuarios de la base de datos
        userList = databaseHelper.getAllUsers()

        // Verificar si la lista no está vacía
        if (userList.isNotEmpty()) {
            val displayList = userList.map { "${it.first}: ${it.second}" } // Formatear para mostrar
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
            userListView.adapter = adapter
        } else {
            Toast.makeText(this, "No hay usuarios en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}
