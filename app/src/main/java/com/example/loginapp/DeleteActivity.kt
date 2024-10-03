package com.example.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DeleteActivity : AppCompatActivity() {

    lateinit var usernameInput: EditText
    lateinit var deleteBtn: Button
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        databaseHelper = DatabaseHelper(this)

        usernameInput = findViewById(R.id.username_input)
        deleteBtn = findViewById(R.id.delete_btn)

        deleteBtn.setOnClickListener {
            val username = usernameInput.text.toString().trim()

            if (username.isNotEmpty()) {
                val result = databaseHelper.deleteUser(username)
                if (result) {
                    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    finish() // Volver a la actividad anterior
                } else {
                    Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa el nombre de usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
