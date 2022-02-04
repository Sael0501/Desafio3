package mx.kodemia.bookodemiasael.authors2

import kotlinx.serialization.Serializable

@Serializable
data class AuthorsAll(
    val data: AuthorsData
): java.io.Serializable
