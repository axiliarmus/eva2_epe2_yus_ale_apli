package com.example.loginapp

import android.content.ContentValues
import android.content.Context
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
    }

    // Create table SQL query
    private val CREATE_USERS_TABLE = (
            "CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT," +
                    "$COLUMN_PASSWORD TEXT)")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Insert a new user into the database
    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return result != -1L // -1 indicates insertion failure
    }


    // Update a user's information in the database
    fun updateUser(oldUsername: String, newUsername: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, newUsername)
        contentValues.put(COLUMN_PASSWORD, newPassword)

        // Update the user record
        val result = db.update(TABLE_USERS, contentValues, "$COLUMN_USERNAME = ?", arrayOf(oldUsername))
        db.close()
        return result > 0 // Retorna true si se actualizó al menos una fila
    }


    // Validate the user by checking username and password
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))

        val isLoggedIn = cursor.count > 0
        cursor.close()
        db.close()

        return isLoggedIn
    }

    // Retrieve all users from the database
    fun getAllUsers(): List<Pair<String, String>> {
        val userList = mutableListOf<Pair<String, String>>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $COLUMN_USERNAME, $COLUMN_PASSWORD FROM $TABLE_USERS", null)

        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(0)
                val password = cursor.getString(1)
                userList.add(Pair(username, password))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return userList
    }

    // Delete a user from the database
    fun deleteUser(username: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_USERS, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return result > 0 // Retorna true si se eliminó al menos una fila
    }

}
