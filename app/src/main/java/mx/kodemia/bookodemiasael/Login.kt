package mx.kodemia.bookodemiasael

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.iniciarSesion
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.extra.validarSesion
import mx.kodemia.bookodemia.model.Errors
import org.json.JSONObject

class Login : AppCompatActivity() {

    private val TAG = Login::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()

        if(validarSesion(applicationContext)){
            lanzarActivity()
        }

        tiet_correo_login.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editText: Editable?) {
                if (editText.toString().trim().isEmpty()) {
                    til_correo_login.setError("Ingrese un correo")
                } else {
                    til_correo_login.setErrorEnabled(false)
                    til_correo_login.setError("")
                }
            }

        })

        tiet_password_login.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editPass: Editable?) {
                if (editPass.toString().trim().isEmpty()) {
                    til_password_login.setError("Ingresa una contraseña")
                } else {
                    til_password_login.setErrorEnabled(false)
                    til_password_login.setError("")
                }
            }

        })


    }
    fun realizarPeticion() {
        VolleyLog.DEBUG = true
        if (estaEnLinea(applicationContext)) {
            btn_login.visibility = View.GONE
            pb_login.visibility = View.VISIBLE
            val cola = Volley.newRequestQueue(applicationContext)
            val JsonObj: JSONObject = JSONObject()
            JsonObj.put("email", tiet_correo_login.text)
            JsonObj.put("password", tiet_password_login.text)
            JsonObj.put("device_name", "Sael's phone")
            val peticion = object : JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.url_servidor) + getString(R.string.api_login),
                JsonObj,
                Response.Listener { response ->
                    val json = JSONObject(response.toString())
                    iniciarSesion(applicationContext, json)
                    if (validarSesion(applicationContext)) {
                        lanzarActivity()
                    }
                },
                { error ->
                    btn_login.visibility = View.VISIBLE
                    pb_login.visibility = View.GONE
                    /*if(error.networkResponse.statusCode == 422){
                    // Realizamos una accion muy especifica para el error de 422
                }*/
                    val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                    //val errors = Json.decodeFromString<Errors>(json.toString())
                    val errors = Gson().fromJson(json.toString(), Errors::class.java)
                    for (error in errors.errors) {
                        mensajeEmergente(this, error.detail)
                    }
                    Log.e(TAG, error.networkResponse.toString())
                    Log.e(TAG, error.toString())
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            cola.add(peticion)
        } else {
            mensajeEmergente(this, getString(R.string.error_internet))
        }
    }

        fun lanzarActivity(){
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        fun init(){
            btn_login.setOnClickListener {
                val listaBool = listOf<Boolean>(validarCorreo(),validarContrasena())
                var contador = 0
                for(validacion in listaBool){
                    if(validacion == false){
                        contador++
                    }
                }
                if(contador<1){
                    contador = 0
                    realizarPeticion()
                }
            }
            val textSigin = findViewById<TextView>(R.id.tv_registro_login)
            textSigin.setOnClickListener {
                startActivity(Intent(this,Registro::class.java))
            }
        }

        private fun validarCorreo(): Boolean{
            return if(tiet_correo_login.text.toString().isEmpty()){
                til_correo_login.error = getString(R.string.campo_vacio)
                false
            }else{
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(tiet_correo_login.text.toString()).matches()){
                    til_correo_login.isErrorEnabled = false
                    true
                }else{
                    til_correo_login.error = getString(R.string.error_correo)
                    false
                }
            }
        }

        private fun validarContrasena(): Boolean{
            return if(tiet_password_login.text.toString().isEmpty()){
                til_password_login.error = getString(R.string.campo_vacio)
                false
            }else{
                til_password_login.isErrorEnabled = false
                true
            }
        }

}
