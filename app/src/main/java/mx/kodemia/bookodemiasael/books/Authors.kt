package mx.kodemia.bookodemia.modelBooks

import kotlinx.serialization.Serializable

@Serializable
data class Authors(
    val links: Links
): java.io.Serializable
