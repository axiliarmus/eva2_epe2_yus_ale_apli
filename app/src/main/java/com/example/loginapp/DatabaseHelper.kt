package com.example.loginapp

import android.content.Context
import android.content.ContentValues  // Importar ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "User.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Tabla ventas
        private const val TABLE_VENTAS = "ventas"
        private const val COLUMN_VENTA_ID = "id"
        private const val COLUMN_VENTA_NOMBRE = "nombre"
        private const val COLUMN_VENTA_PRECIO = "precio"
        private const val COLUMN_VENTA_FECHA = "fecha"
        private const val COLUMN_VENTA_MEDIO_PAGO = "medio_pago"

        // Tabla productos
        private const val TABLE_PRODUCTOS = "productos"
        private const val COLUMN_PRODUCTO_ID = "id"
        private const val COLUMN_PRODUCTO_NOMBRE = "nombre"
        private const val COLUMN_PRODUCTO_PRECIO = "precio"
    }

    // Crear las tablas con el estilo camelCase para las constantes
    private val createUsersTable = (
            "CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT," +
                    "$COLUMN_PASSWORD TEXT)"
            )

    private val createVentasTable = (
            "CREATE TABLE $TABLE_VENTAS (" +
                    "$COLUMN_VENTA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_VENTA_NOMBRE TEXT," +
                    "$COLUMN_VENTA_PRECIO REAL," +
                    "$COLUMN_VENTA_FECHA TEXT," +
                    "$COLUMN_VENTA_MEDIO_PAGO TEXT)"
            )
    private val createProductosTable = (
            "CREATE TABLE $TABLE_PRODUCTOS (" +
                    "$COLUMN_PRODUCTO_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_PRODUCTO_NOMBRE TEXT," +
                    "$COLUMN_PRODUCTO_PRECIO REAL)"
            )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createUsersTable)
        db.execSQL(createVentasTable)
        db.execSQL(createProductosTable)

    }


    private fun insertarUsuarioAdmin(db: SQLiteDatabase) {
        // Consultar si el usuario admin ya existe
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?", arrayOf("admin"))

        // Si no hay resultados, insertar el usuario admin
        if (cursor.count == 0) {
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, "admin")
                put(COLUMN_PASSWORD, "12345")
            }
            db.insert(TABLE_USERS, null, values) // Insertar el usuario admin
        }

        cursor.close() // Cerrar el cursor
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VENTAS")
        onCreate(db)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase

        // Consulta con cláusula WHERE y parámetros de selección para evitar inyecciones SQL
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))

        // Validar si se encontró al menos un registro
        val isValid = cursor.moveToFirst() // Mueve el cursor al primer resultado, si existe

        cursor.close() // Asegurarse de cerrar el cursor
        db.close()     // Cerrar la base de datos

        return isValid // Retorna true si encontró al usuario, false de lo contrario
    }
    fun obtenerProductos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val db = readableDatabase

        // Usamos try-catch para manejar posibles excepciones
        try {
            val cursor = db.query(
                TABLE_PRODUCTOS,
                arrayOf(COLUMN_PRODUCTO_ID, COLUMN_PRODUCTO_NOMBRE, COLUMN_PRODUCTO_PRECIO),
                null,
                null,
                null,
                null,
                null
            )

            // Verificamos si el cursor no es null y si hay registros
            cursor?.let {
                if (it.moveToFirst()) {
                    do {
                        val id = it.getInt(it.getColumnIndex(COLUMN_PRODUCTO_ID))
                        val nombre = it.getString(it.getColumnIndex(COLUMN_PRODUCTO_NOMBRE))
                        val precio = it.getDouble(it.getColumnIndex(COLUMN_PRODUCTO_PRECIO))

                        // Creamos un objeto Producto y lo añadimos a la lista
                        val producto = Producto(id, nombre, precio)
                        productos.add(producto)
                    } while (it.moveToNext())
                }
            }
        } catch (e: Exception) {
            // Manejo de excepciones
            e.printStackTrace()
        } finally {
            // Nos aseguramos de cerrar la base de datos
            db.close()
        }

        return productos
    }





    // Método para insertar una venta
        fun insertarVenta(nombre: String, precio: Double, fecha: String, medioPago: String): Long {
            val db = writableDatabase  // Obtiene la base de datos en modo escritura
            val values = ContentValues().apply {
                put(COLUMN_VENTA_NOMBRE, nombre)
                put(COLUMN_VENTA_PRECIO, precio)
                put(COLUMN_VENTA_FECHA, fecha)
                put(COLUMN_VENTA_MEDIO_PAGO, medioPago)
            }

            // Realiza la inserción
            val result = db.insert(TABLE_VENTAS, null, values)  // Inserta los datos y retorna el ID insertado
            db.close()

            return result  // Retorna el resultado de la inserción
        }


        // Método para modificar una venta existente
        fun modificarVenta(id: Int, nombre: String, precio: Double) {
            val db = writableDatabase  // Obtiene la base de datos en modo escritura
            val values = ContentValues().apply {
                put(COLUMN_VENTA_NOMBRE, nombre)
                put(COLUMN_VENTA_PRECIO, precio)
            }

            // Realiza la actualización de la venta con el id específico
            db.update(TABLE_VENTAS, values, "$COLUMN_VENTA_ID = ?", arrayOf(id.toString()))
            db.close()  // Cierra la base de datos
        }


        // Método para obtener las ventas filtradas por fecha
        fun getVentasPorFecha(fecha: String): List<Venta> {
            val ventas = mutableListOf<Venta>()
            val db = readableDatabase  // Obtiene la base de datos en modo lectura

            // Filtrar las ventas por fecha usando la cláusula WHERE
            val cursor = db.query(
                TABLE_VENTAS,
                null,
                "$COLUMN_VENTA_FECHA = ?",
                arrayOf(fecha),
                null,
                null,
                null
            )

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COLUMN_VENTA_ID))
                    val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_VENTA_NOMBRE))
                    val precio = cursor.getDouble(cursor.getColumnIndex(COLUMN_VENTA_PRECIO))
                    val fechaVenta = cursor.getString(cursor.getColumnIndex(COLUMN_VENTA_FECHA))
                    val medioPago = cursor.getString(cursor.getColumnIndex(COLUMN_VENTA_MEDIO_PAGO))

                    val venta = Venta(id, nombre, precio, fechaVenta, medioPago)
                    ventas.add(venta)
                } while (cursor.moveToNext())
            }
            cursor.close()  // Cierra el cursor
            db.close()  // Cierra la base de datos
            return ventas
        }

    }

