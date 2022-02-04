package mx.kodemia.bookodemiasael

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_detalles.*

class Detalles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        val button_favorite: ImageButton = findViewById(R.id.imageButton7)
        val button_share: ImageButton = findViewById(R.id.imageButton6)
        button_favorite.setBackgroundResource(0)
        button_share.setBackgroundResource(0)

        text_regresar_detalle.setOnClickListener{
            startActivity(Intent(this,Home::class.java))
        }

        val text_button_des: TextView = findViewById(R.id.tv_descripcion_detalles)
        val text_button_det: TextView = findViewById(R.id.textView10)
        val alter_text: TextView = findViewById(R.id.textView12)

        text_button_des.setOnClickListener {
            alter_text.setText("Esta es una descripción, is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        }

        text_button_det.setOnClickListener {
            alter_text.setText("Estos son los detalles, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum")
        }
    }
}