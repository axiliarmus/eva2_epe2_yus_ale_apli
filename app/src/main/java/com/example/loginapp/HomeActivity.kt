package com.example.loginapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    // Declaración de los elementos de la interfaz
    lateinit var firstNameInput: EditText
    lateinit var lastNameInput: EditText
    lateinit var comunaInput: EditText
    lateinit var observationInput: EditText
    lateinit var sendEmailBtn: Button
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Obtener el nombre de usuario pasado desde MainActivity
        val username = intent.getStringExtra("USERNAME")

        // Configurar el mensaje de bienvenida
        val welcomeMessage = findViewById<TextView>(R.id.welcome_message)
        welcomeMessage.text = "Bienvenido, $username"

        // Referencias a los campos de entrada
        firstNameInput = findViewById(R.id.first_name_input)
        lastNameInput = findViewById(R.id.last_name_input)
        comunaInput = findViewById(R.id.comuna_input)
        observationInput = findViewById(R.id.observation_input)
        sendEmailBtn = findViewById(R.id.send_email_btn)

        // Configurar el botón para enviar el correo electrónico
        sendEmailBtn.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val comuna = comunaInput.text.toString().trim()
            val observation = observationInput.text.toString().trim()

            // Validar que todos los campos estén completos
            if (firstName.isEmpty() || lastName.isEmpty() || comuna.isEmpty() || observation.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show()
            } else {
                // Guardar los datos en la base de datos si es necesario (opcional)
                val isInserted = databaseHelper.addUser(firstName, lastName) // Puedes cambiar a otra tabla si es necesario
                if (isInserted) {
                    // Si los datos fueron insertados correctamente, enviar el correo
                    sendEmail(username, firstName, lastName, comuna, observation)
                } else {
                    Toast.makeText(this, "Error al guardar los datos.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Función para enviar el correo electrónico
    private fun sendEmail(username: String?, firstName: String, lastName: String, comuna: String, observation: String) {
        val subject = "Datos de $username"
        val message = """
            Nombre: $firstName
            Apellido: $lastName
            Comuna: $comuna
            Observación: $observation
        """.trimIndent()

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("francisco.yanez@edu.ipchile.cl")) // Puedes cambiar el correo aquí
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo con"))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "No se encontró ninguna aplicación de correo.", Toast.LENGTH_SHORT).show()
        }
    }
}