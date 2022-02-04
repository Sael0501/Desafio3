package mx.kodemia.bookodemiasael.books.categories
import kotlinx.serialization.Serializable

@Serializable
data class AllCategories(
    val data: MutableList<CategoriesData>
): java.io.Serializable
