package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import io.github.rokuosan.hst.models.Record
import io.github.rokuosan.hst.utils.Reader
import io.github.rokuosan.hst.utils.Writer
import java.lang.Exception

class Remove: CliktCommand() {
    private val address by argument()
    private val hostname by argument()

    override fun run() {
        val records = Reader.getRecords()

        // Get entry; If it is not found, return null.
        val entry = fun(addr: String, host: String): Record.Entry?{
            for (rec in records) {
                // Skip comment.
                if (rec is Record.Comment) continue

                val r = rec as Record.Entry
                if (r.address == addr && r.hostname == host) {
                    return r
                }
            }
            return null
        }(address, hostname)?:run {
            echo("Your entry was not found in /etc/hosts.")
            echo("Please check your /etc/hosts or the search hostname.")
            return
        }

        // Remove entry
        val rawRecords = Reader.getRawRecords()
        val filtered = rawRecords.filter {
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
        }

        // Confirm
        val term = Terminal()
        echo(table {
            header { row("Address", "Hostname", "Aliases") }
            body {
                row(entry.address, entry.hostname, entry.aliases.joinToString("\n"))
            }
            footer {
                cellBorders = Borders.NONE
                row("This record will be removed.")
            }
        })
        if (YesNoPrompt("Continue?", term).ask() == false) {
            echo("Aborted.")
            return
        }

        // Write
        try {
            Writer.writeAll(filtered)
        }catch (e: Exception) {
            echo(e.message?.let { TextColors.red(it) })
        }
    }
}