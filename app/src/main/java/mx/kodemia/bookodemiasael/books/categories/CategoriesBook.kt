package mx.kodemia.bookodemiasael.books.categories

import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.modelBooks.Links

@Serializable
data class CategoriesBook(
    val links: Links
): java.io.Serializable
