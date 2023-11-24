package com.ceduc.complementariasqlite

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val rut: String?
        get() {
            TODO()
        }
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnListar = findViewById<Button>(R.id.btnListar)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        val editTextRut = findViewById<EditText>(R.id.editTextRut)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextCorreo = findViewById<EditText>(R.id.editTextCorreo)

        btnGuardar.setOnClickListener {
            guardarUsuario(editTextRut.text.toString(), editTextNombre.text.toString(), editTextCorreo.text.toString())
            limpiarCampos()
            showToast()
        }

        btnListar.setOnClickListener {
            listarUsuarios()
            mostrarDialogo()
        }

        btnBuscar.setOnClickListener {
            val rut = editTextRut.text.toString()
            val usuario = buscarUsuario(rut)

            if (usuario != null) {
                editTextNombre.setText(usuario.getString(1))
                editTextCorreo.setText(usuario.getString(2))
            } else {
                showToast()
            }
        }


        btnActualizar.setOnClickListener {
            editTextRut.text.toString()
            editTextNombre.text.toString()
            editTextCorreo.text.toString()

            if (actualizarUsuario()) {
                showToast()
            } else {
                showToast()
            }
        }

        btnEliminar.setOnClickListener {
            editTextRut.text.toString()
            if (eliminarUsuario()) {
                limpiarCampos()
                showToast()
            } else {
                showToast()
            }
        }
    }

    private fun eliminarUsuario(): Boolean {
        val db = dbHelper.writableDatabase
        val eliminados = db.delete("Usuarios", "Rut=?", arrayOf(rut))
        db.close()

        return eliminados > 0
    }


    private fun actualizarUsuario(): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put()
            put()
        }

        val actualizados = db.update("Usuarios", values, "Rut=?", arrayOf(rut))
        db.close()

        return actualizados > 0
    }

    @Suppress("SameParameterValue")
    private fun put() {

    }


    private fun mostrarDialogo() {
        val builder = AlertDialog.Builder(this)
        val mensaje = null
        builder.setMessage(mensaje)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showToast() {
        val message: Nothing? = null
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun limpiarCampos() {
        findViewById<EditText>(R.id.editTextRut).setText("")
        findViewById<EditText>(R.id.editTextNombre).setText("")
        findViewById<EditText>(R.id.editTextCorreo).setText("")
    }


    private fun guardarUsuario(rut: String, nombre: String, correo: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("Rut", rut)
            put("Nombre", nombre)
            put("Correo", correo)
        }

        db.insert("Usuarios", null, values)
        db.close()
    }

    private fun listarUsuarios(): String {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Usuarios", null)

        val stringBuilder = StringBuilder()

        while (cursor.moveToNext()) {
            val rut = cursor.getString(0)
            val nombre = cursor.getString(1)
            val correo = cursor.getString(2)

            stringBuilder.append("Rut: $rut, Nombre: $nombre, Correo: $correo\n")
        }

        cursor.close()
        db.close()

        return stringBuilder.toString()
    }

    private fun buscarUsuario(rut: String): Cursor? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "Usuarios",
            arrayOf("Rut", "Nombre", "Correo"),
            "Rut=?",
            arrayOf(rut),
            null,
            null,
            null
        )

        cursor?.moveToFirst()
        return cursor
    }


}

