package mx.kodemia.bookodemiasael.books.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesRelationships(
    val books: CategoriesBook
): java.io.Serializable
