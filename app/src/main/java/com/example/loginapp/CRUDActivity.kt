package com.example.loginapp

import com.example.loginapp.ReadActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CRUDActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud)

        // Referencias a los botones
        val btnCreate: Button = findViewById(R.id.btn_create)
        val btnRead: Button = findViewById(R.id.btn_read)
        val btnUpdate: Button = findViewById(R.id.btn_update)
        val btnDelete: Button = findViewById(R.id.btn_delete)

        // Configurar botones con acciones específicas para cada operación CRUD
        btnCreate.setOnClickListener {
            // Lógica para crear un nuevo registro
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        btnRead.setOnClickListener {
            // Lógica para leer registros
            val intent = Intent(this, ReadActivity::class.java)
            startActivity(intent)
        }

        btnUpdate.setOnClickListener {
            // Lógica para actualizar un registro
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            // Lógica para eliminar un registro
            val intent = Intent(this, DeleteActivity::class.java)
            startActivity(intent)
        }
    }
}
