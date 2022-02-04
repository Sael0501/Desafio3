package mx.kodemia.bookodemiasael.books.categories


import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.modelBooks.Links

@Serializable
data class CategoriesData(
    val type: String,
    val id: String,
    val attributes: CategoriesAttributes,
    val relationships: CategoriesRelationships,
    val links: Links
): java.io.Serializable
