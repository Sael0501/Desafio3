package mx.kodemia.bookodemiasael

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.extra.eliminarSesion
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.extra.obtenerkDeSesion
import mx.kodemia.bookodemia.model.Errors
import mx.kodemia.bookodemia.modelBooks.Data
import mx.kodemia.bookodemia.modelBooks.datosLibro
import mx.kodemia.bookodemiasael.adaptadores.AdaptadorBooks
import mx.kodemia.bookodemiasael.adaptadores.RecyclerViewHome
import mx.kodemia.bookodemiasael.model.DataClassHome
import org.json.JSONObject

class Home : AppCompatActivity() {
    private val TAG = Home::class.qualifiedName


    val librosLista: MutableList<datosLibro> = mutableListOf()
    var adapterAgregados = RecyclerViewHome(librosLista)
    private lateinit var rv_books: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()



        tv_regresar_home.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    fun initRecyclerLibros(listBooks: MutableList<datosLibro>) {
        var adapterLibro = AdaptadorBooks(this, librosLista)
        recyclerView_Home.layoutManager = LinearLayoutManager(this)
        recyclerView_Home.adapter = adapterLibro
        recyclerView_Home.setHasFixedSize(true)
    }


        fun init() {
        rv_books = findViewById(R.id.recyclerView_Home)
    }

    override fun onResume() {
        super.onResume()
        realizarPeticion()
    }

    fun realizarPeticion() {
        if (estaEnLinea(applicationContext)) {
            val cola = Volley.newRequestQueue(applicationContext)
            val peticion = object : JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.url_servidor) + getString(R.string.api_libros),
                null,
                { response ->
                    Log.d(TAG, response.toString())
                    val books = Json.decodeFromString<Data>(response.toString())
                    val adaptador = AdaptadorBooks(this, books.data)
                    rv_books.layoutManager = LinearLayoutManager(this)
                    rv_books.adapter = adaptador
                    adaptador.notifyDataSetChanged()
                    rv_books.visibility = View.VISIBLE
                },
                { error ->
                    if (error.networkResponse.statusCode == 401) {
                        eliminarSesion(applicationContext)
                        val intent = Intent(this, Login::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                        val errors = Json.decodeFromString<Errors>(json.toString())
                        for (error in errors.errors) {
                            mensajeEmergente(this, error.detail)
                        }
                        rv_books.visibility = View.GONE
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] =
                        "Bearer ${obtenerkDeSesion(applicationContext, "token")}"
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

    /* fun initRecyclerAgregados() {
        val myLinearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recyclerView_Home.layoutManager = myLinearLayoutManager
        recyclerView_Home.adapter = adapterAgregados
        recyclerView_Home.setHasFixedSize(true)
        }
     */


   /* private fun addLibros() {

        librosLista.add(DataClassHome(R.drawable.libro_1, "Las Cronicas de Narnia", "Sabe", "Fantasia"))
        librosLista.add(DataClassHome(R.drawable.libro_2, "La Sombra", "Sabe", "Suspenso"))
        librosLista.add(DataClassHome(R.drawable.libro_1, "El Evangelio del mal","Sabe", "Suspenso"))
        librosLista.add(DataClassHome(R.drawable.libro_2,"La musica del silencio", "Mark", "Fantasia"))



        recyclerView_Home.layoutManager = LinearLayoutManager(this)
        recyclerView_Home.setHasFixedSize(true)
        adapterAgregados = RecyclerViewHome(librosLista)
        recyclerView_Home.adapter = adapterAgregados
    }*/

    fun item_card(view: View) {
        startActivity(Intent(this, Detalles::class.java))

    }
}



