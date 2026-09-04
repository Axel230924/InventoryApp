package com.example.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.inventoryapp.data.remote.dto.LoginRequest
import com.example.inventoryapp.data.remote.retrofit.RetrofitClient
import com.example.inventoryapp.ui.dashboard.Dashboard
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.content.Context


//Esta Activity corresponde a la pantalla de inicio de sesión. Su función es capturar los datos introducidos por el usuario y realizar las validaciones necesarias antes de permitir el acceso a la aplicación.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla completa.
        enableEdgeToEdge()

        // Carga el diseño XML correspondiente a la pantalla de Login.
        setContentView(R.layout.activity_login)

        //Ajusta el contenido de la pantalla para evitar que los componentes queden ocultos detrás de las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            // Obtiene las dimensiones de las barras del sistema.
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica el espacio de las barras del sistema al contenedor principal.
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            // Devuelve los Insets para que el sistema continúe procesándolos.
            insets
        }

        //Establece la aplicación permanentemente en modo claro.
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        // Obtiene el campo de usuario definido en activity_login.xml.
        val txtUsuario =
            findViewById<TextInputEditText>(R.id.TIEUsuario)

        // Obtiene el campo de contraseña definido en activity_login.xml.
        val txtContraseña =
            findViewById<TextInputEditText>(R.id.TIEContraseña)

        // Obtiene el contenedor del campo de usuario.
        val layoutUsuario =
            findViewById<TextInputLayout>(R.id.txtILayoutUsuario)

        // Obtiene el contenedor del campo de contraseña.
        val layoutContraseña =
            findViewById<TextInputLayout>(R.id.txtILayoutContraseña)

        // Obtiene el botón "Iniciar Sesión".
        val btnInicioS =
            findViewById<Button>(R.id.btniniciosesion)

        // Detecta cuando el usuario presiona el botón de iniciar sesión
        btnInicioS.setOnClickListener {

            //Obtiene el contenido escrito en el campo de usuario.
            val usuario =
                txtUsuario.text.toString().trim()

            // Obtiene el contenido escrito en el campo de contraseña.
            val contraseña =
                txtContraseña.text.toString()

            // Limpia los mensajes de error que hayan sidos mostrados en intentos anteriores
            layoutUsuario.error = null
            layoutContraseña.error = null

            // Variable utilizada para determinar si todos los datos cumplen las validaciones locales.

            var datosValidos = true

            // Comprueba si el campo de usuario está vacío.
            if (usuario.isEmpty()) {

                // Muestra el mensaje de error debajo del campo.
                layoutUsuario.error = "El usuario es obligatorio"

                // Indica que existen datos incorrectos.
                datosValidos = false
            }

            // Comprueba si el campo de contraseña está vacío.
            if (contraseña.isEmpty()) {

                // Muestra el mensaje de error debajo del campo.
                layoutContraseña.error = "La contraseña es obligatoria"

                // Indica que los datos no son validos
                datosValidos = false
            }

            // Comprueba que la contraseña tenga como mínimo 6 caracteres.
            if (contraseña.isNotEmpty() && contraseña.length < 6) {

                // Muestra un mensaje indicando la longitud mínima.
                layoutContraseña.error =
                    "La contraseña debe tener al menos 6 caracteres"

                // Indica que los datos no son válidos.
                datosValidos = false
            }

            //Si alguno de los campos no cumple las validaciones, no se continúa con el proceso de autenticación
            if (!datosValidos) {
                return@setOnClickListener
            }

            // Login real contra la API con JWT
            lifecycleScope.launch {
                try {
                    val respuesta = RetrofitClient.create(this@MainActivity).login(
                        LoginRequest(usuario = usuario, password = contraseña)
                    )

                    // Login exitoso: el backend devolvió un token válido.
                    // Obtiene el token JWT enviado por la API.
                    val token = respuesta.token

                    // Obtiene el almacenamiento privado de la aplicación.
                    val sharedPreferences = getSharedPreferences(
                        "InventoryPreferences",
                        Context.MODE_PRIVATE
                    )

                    // Guarda el token JWT para utilizarlo en las siguientes peticiones.
                    val editor = sharedPreferences.edit()

                    editor.putString(
                        "token",
                        token
                    )

                    editor.apply()

                    // Comprueba en Logcat que el token fue almacenado.
                    android.util.Log.d(
                        "JWT_TEST",
                        "Token guardado correctamente: ${token.take(10)}..."
                    )

                    // Abre el Dashboard después de guardar el token.
                    val intent = Intent(this@MainActivity, Dashboard::class.java)
                    startActivity(intent)
                    finish()

                } catch (e: Exception) {

                    android.util.Log.e(
                        "LOGIN_ERROR",
                        "Error real durante el login",
                        e
                    )

                    layoutUsuario.error = "Error de conexión"
                    layoutContraseña.error = "Revise Logcat"
                }
            }
        }
    }
}