package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
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

        // Create a record
        val record = "$address $hostname ".plus(aliases.joinToString(" "))

        // Confirm
        echo("The record will be set in hosts:")
        echo("  - $record")
        if (YesNoPrompt("Continue?", terminal).ask() == false) {
            echo("Aborted.")
            return
        }

        // Export
//        val cwd = Path("sample.txt").toAbsolutePath()
//        if (!cwd.exists()) cwd.createFile()
//        cwd.toFile().let {
//            val sb = StringBuilder()
//            sb.appendLine(record)
//            it.appendText(sb.toString())
//        }
//        echo("Success.")
        
        val e = Record.Entry(address, hostname, aliases)
        Writer.append(e)
        echo("Success")
    }
}