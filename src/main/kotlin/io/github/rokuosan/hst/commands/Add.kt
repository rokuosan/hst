package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import io.github.rokuosan.hst.models.Record
import io.github.rokuosan.hst.utils.Writer


class Add: CliktCommand() {
    private val address by argument()
    private val hostname by argument()
    private val aliases: List<String?> by argument().multiple()

    override fun run() {
        val terminal = Terminal()

        // Confirm
        echo(table {
            header {
                row("Address", "Hostname", "Aliases")
            }
            body {
                row(address, hostname, aliases.joinToString("\n"))
            }
        })

        if (YesNoPrompt("Continue?", terminal).ask() == false) {
            echo("Aborted.")
            return
        }

        // Export
        val e = Record.Entry(address, hostname, aliases)
        Writer.append(e)
        echo("Success")
    }
}