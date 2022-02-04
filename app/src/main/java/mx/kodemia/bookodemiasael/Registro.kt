package mx.kodemia.bookodemiasael

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.iniciarSesion
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.model.Errors
import org.json.JSONObject

class Registro : AppCompatActivity() {

    private val TAG = Registro::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        init()


        tv_regresar_registro.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        tiet_registro_usuario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editUser: Editable?) {
                if (editUser.toString().trim().isEmpty()) {
                    til_registro_usuario.setError("Ingrese nombre de usuario")
                } else {
                    til_registro_usuario.setErrorEnabled(false)
                    til_registro_usuario.setError("")
                }
            }

        })

        tiet_registro_correo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editCorreo: Editable?) {
                if (editCorreo.toString().trim().isEmpty()) {
                    til_registro_correo.setError("Ingrese correo")
                } else {
                    til_registro_correo.setErrorEnabled(false)
                    til_registro_correo.setError("")
                }
            }

        })

        tiet_registro_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editPass: Editable?) {
                if (editPass.toString().trim().isEmpty()) {
                    til_registro_password.setError("Ingrese contrasena")
                } else {
                    til_registro_password.setErrorEnabled(false)
                    til_registro_password.setError("")
                }
            }

        })

        tiet_registro_password2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editConfirm: Editable?) {
                if (editConfirm.toString().trim().isEmpty()) {
                    til_registro_password2.setError("Confirme Contrasena")
                } else {
                    til_registro_password2.setErrorEnabled(false)
                    til_registro_password2.setError("")
                }
            }

        })
    }
    fun init(){
        btn_registro.setOnClickListener {
            val listaBool = listOf<Boolean>(validarNombre(),validarCorreo(),validarContrasena(),validarContrasenaDos(),validarSimilutud())
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
    }
    private fun validarCorreo(): Boolean{
        return if(tiet_registro_correo.text.toString().isEmpty()){
            til_registro_correo.error = getString(R.string.campo_vacio)
            false
        }else{
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(tiet_registro_correo.text.toString()).matches()){
                til_registro_correo.isErrorEnabled = false
                true
            }else{
                til_registro_correo.error = getString(R.string.error_correo)
                false
            }
        }
    }

    private fun validarContrasena(): Boolean{
        return if(tiet_registro_password.text.toString().isEmpty()){
            til_registro_password.error = getString(R.string.campo_vacio)
            false
        }else{
            til_registro_password.isErrorEnabled = false
            true
        }
    }

    private fun validarContrasenaDos(): Boolean{
        return if(tiet_registro_password2.text.toString().isEmpty()){
            til_registro_password2.error = getString(R.string.campo_vacio)
            false
        }else{
            til_registro_password2.isErrorEnabled = false
            true
        }
    }

    private fun validarNombre(): Boolean{
        return if(tiet_registro_usuario.text.toString().isEmpty()){
            til_registro_usuario.error = getString(R.string.campo_vacio)
            false
        }else{
            til_registro_usuario.isErrorEnabled = false
            true
        }
    }

    private fun validarSimilutud(): Boolean{
        val textoPassword: String = tiet_registro_password.text?.trim().toString()
        val textoPassConfirm: String = tiet_registro_password2.text?.trim().toString()
        return if (textoPassword != textoPassConfirm){
            Toast.makeText(this,"Contraseñas diferentes", Toast.LENGTH_SHORT).show()
            tiet_registro_password.setText("")
            tiet_registro_password2.setText("")
            false
        }else{
            true
        }
    }

    fun realizarPeticion(){
        if(estaEnLinea(applicationContext)){
            val json = JSONObject()
            json.put("name", tiet_registro_usuario.text)
            json.put("email", tiet_registro_correo.text)
            json.put("password", tiet_password_login.text)
            json.put("password_confirmation", tiet_registro_password2.text)
            json.put("device_name","phone")
            val cola = Volley.newRequestQueue(applicationContext)
            val peticion = object: JsonObjectRequest(Request.Method.POST,getString(R.string.url_servidor)+getString(R.string.api_registro),json, {
                    response ->
                Log.d(TAG,response.toString())
                val jsonObject = JSONObject(response.toString())
                iniciarSesion(applicationContext,jsonObject)
                val intent = Intent(this,Home::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            },{
                    error ->
                if(error.networkResponse.statusCode == 422){
                    mensajeEmergente(this,getString(R.string.error_422))
                }else{
                    val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                    //val errors = Json.decodeFromString<Errors>(json.toString())
                    val errors = Gson().fromJson(json.toString(), Errors::class.java)
                    for (error in errors.errors) {
                        mensajeEmergente(this, error.detail)
                    }
                }
                Log.e(TAG,error.toString())
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            cola.add(peticion)
        }else{
            mensajeEmergente(this,getString(R.string.error_internet))
        }
    }
}

/* btn_registro.setOnClickListener {

     val textoUsuario: String = tiet_registro_usuario.text?.trim().toString()
     val textoCorreo: String = tiet_registro_correo.text?.trim().toString()
     val textoPassword: String = tiet_registro_password.text?.trim().toString()
     val textoPassConfirm: String = tiet_registro_password2.text?.trim().toString()

     if(textoUsuario.isEmpty()){
         til_registro_usuario.setError("Ingrese nombre de usuario")
     }else{
         til_registro_usuario.setError(null)
     }

     if(textoCorreo.isEmpty()){
         til_registro_correo.setError("Ingrese correo")
     }else{
         til_registro_correo.setError(null)
     }

     if(textoPassword.isEmpty()){
         til_registro_password.setError("Ingrese contrasena")
     }else{
         til_registro_password.setError("")
     }

     if(textoPassConfirm.isEmpty()){
         til_registro_password2.setError("Confirme contrasena")
     }else{
         til_registro_password2.setError("")
     }
     if (textoPassword != textoPassConfirm){
         Toast.makeText(this,"Contraseñas diferentes", Toast.LENGTH_SHORT).show()
         til_registro_password.setError("Ingrese contrasena")
         til_registro_password2.setError("Confirme contrasena")
     }else{
         startActivity(Intent(this,Home::class.java))
     }

 }

}*/
