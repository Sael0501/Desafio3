package mx.kodemia.bookodemiasael.authors2


import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.modelBooks.Links
import mx.kodemia.bookodemiasael.books.categories.CategoriesRelationships

@Serializable
data class AuthorsData(
    val type: String,
    val id: String,
    val attributes: AuthorsAttributes,
    val relationships: CategoriesRelationships,
    val links: Links
): java.io.Serializable
