package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.varargValues
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import io.github.rokuosan.hst.models.Record
import io.github.rokuosan.hst.utils.Writer


class Add: CliktCommand() {
    private val address: String by option("-a", "--address", help = "Address").prompt()
    private val hostname: String by option("-n", "--hostname", help = "Hostname").prompt()
    private val aliases: List<String>? by option("--aliases", help = "Aliases").varargValues()

    override fun run() {
        // Prepare a decorated terminal
        val terminal = Terminal()

        // Confirm
        echo(table {
            header {
                row("Address", "Hostname", "Aliases")
            }
            body {
                row(address, hostname, aliases?.joinToString("\n") ?:"None")
            }
            footer {
                style(italic = true)
                row{
                    cellBorders = Borders.NONE
                    cell("This entry will be created.")
                }
            }
        })

        if (YesNoPrompt("Continue?", terminal).ask() == false) {
            echo("Aborted.")
            return
        }

        // Export
        val e = Record.Entry(address, hostname, aliases?: emptyList())
        try {
            Writer.append(e)
            echo("Success")
        }catch (e: Exception) {
            echo(e.message?.let { TextColors.red(it) })
        }
    }
}