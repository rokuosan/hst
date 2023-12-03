package io.github.rokuosan.hst.models


/***
 * Record is an expression of DNS record.
 */
sealed class Record {
    // Comment
    data class Comment(
        val content:  String
    ): Record()

    // DNS Record
    data class Entry(
        val address: String,
        val hostname: String,
        val aliases: List<String?>
    ): Record()
}
