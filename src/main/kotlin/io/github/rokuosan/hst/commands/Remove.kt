package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.github.rokuosan.hst.models.Record
import io.github.rokuosan.hst.utils.Reader

class Remove: CliktCommand() {
    private val hostname by argument()

    override fun run() {
        val records = Reader.getRecords()

        // Get entry; If it is not found, return null.
        val entry = fun(host: String): Record.Entry?{
            for (rec in records) {
                // Skip comment.
                if (rec is Record.Comment) continue

                val r = rec as Record.Entry
                return if (r.hostname == host) {
                    r
                }else {
                    null
                }
            }
            return null
        }(hostname)?:run {
            echo("Your entry was not found in /etc/hosts.")
            echo("Please check your /etc/hosts or the search hostname.")
            return
        }

        // Remove entry
        val rawRecords = Reader.getRawRecords()
        rawRecords.filter {
            // Cast this line to Record
            when (val r = Reader.rowToRecord(it)) {
                is Record.Comment -> {
                    true
                }
                is Record.Entry -> {
                    r != entry
                }
                null -> {
                    false
                }
            }
        }.map(::println)
    }
}