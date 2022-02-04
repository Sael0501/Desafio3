package mx.kodemia.bookodemiasael.adaptadores

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.*
import mx.kodemia.bookodemia.Connections.Connect
import mx.kodemia.bookodemia.extra.eliminarSesion
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.extra.obtenerkDeSesion
import mx.kodemia.bookodemia.model.Errors
import mx.kodemia.bookodemia.modelAuthors.DataCategory
import mx.kodemia.bookodemia.modelAuthors.DataFirstAuthor
import mx.kodemia.bookodemia.modelAuthors.DataSecAuthor
import mx.kodemia.bookodemia.modelBooks.Data
import mx.kodemia.bookodemia.modelBooks.datosLibro
import mx.kodemia.bookodemiasael.Detalles
import mx.kodemia.bookodemiasael.Home
import mx.kodemia.bookodemiasael.R
import mx.kodemia.bookodemiasael.authors2.AuthorsAll
import mx.kodemia.bookodemiasael.books.categories.CategoriesAll
import org.json.JSONObject

class AdaptadorBooks(val activity: Activity, val books: MutableList<datosLibro>): RecyclerView.Adapter<AdaptadorBooks.BookHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view,parent,false)
        return BookHolder(view)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book = books.get(position)
        with(holder){
            tv_title.setOnClickListener{
                val bundle = Bundle()
                bundle.putSerializable("book",book)
                val intent = Intent(activity,Detalles::class.java)
                intent.putExtras(bundle)
                activity.startActivity(intent)
            }
            tv_title.text = book.attributes.title
//            tv_author.text = book.relationships.toString()
//            tv_cat.text = book.relationships.categories.toString()
        }
    }


    override fun getItemCount(): Int = books.size


    class BookHolder(val view: View): RecyclerView.ViewHolder(view){
        val tv_title: TextView = view.findViewById(R.id.tv_nombre_autor)
        val tv_author: TextView = view.findViewById(R.id.tv_autor_detalles)
        val tv_cat: TextView = view.findViewById(R.id.tv_categoria_detalles)
        private val AUTOR = "author"
        private val CATEGORY = "category"

//        val cv: MaterialCardView = view.findViewById(R.id.cardView_recien_agregados)
//        val img: ImageView = view.findViewById(R.id.image_recycler_agregados_portada)
//        val titulo: TextView = view.findViewById(R.id.text_recycler_agregados_titulo)
//        val autor: TextView = view.findViewById(R.id.text_recycler_agregados_autor)
//        val categoria: TextView = view.findViewById(R.id.text_recycler_agregados_categoria)
//        val btn_detalles: Button = view.findViewById(R.id.button_recycler_home_detalles)

        fun getRequests(libro: datosLibro){
            getCategoriesOrAuthorsByRequest(libro.relationships.authors.links.related, tv_author, AUTOR)
            getCategoriesOrAuthorsByRequest(libro.relationships.categories.links.related, tv_cat, CATEGORY)
        }

        fun setInfo(libro: datosLibro) {
            //Glide.with(view).load(libro.img).diskCacheStrategy(DiskCacheStrategy.NONE).into(img)

            getRequests(libro)
//            img.setImageResource(R.drawable.libro_1)
//            titulo.text = libro.attributes.title

        }

        private fun getCategoriesOrAuthorsByRequest(url: String, textView: TextView, type: String) {
            val queue = Volley.newRequestQueue(view.context)

            val request = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    if(type == CATEGORY) {
                        val r = Json.decodeFromString<CategoriesAll>(response.toString())
                        textView.text = r.data.attributes.name
                    }
                    else{
                        val r = Json.decodeFromString<AuthorsAll>(response.toString())
                        textView.text = view.context.getString(R.string.Autorchange, r.data.attributes.name)
                    }
                },
                { error ->
                    Log.e("Recycler", error.toString())
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }
            queue.add(request)
        }
    }
}