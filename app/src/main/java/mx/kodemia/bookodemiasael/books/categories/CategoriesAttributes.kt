package mx.kodemia.bookodemiasael.books.categories
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesAttributes(
    val name: String,
    val slug: String
): java.io.Serializable