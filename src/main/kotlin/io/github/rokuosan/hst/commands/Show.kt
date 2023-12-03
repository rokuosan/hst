package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.BorderType
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import io.github.rokuosan.hst.models.Record
import io.github.rokuosan.hst.utils.Reader

class Show: CliktCommand() {
    override fun run() {
        val records = Reader.getRecords()

        val rows: MutableMap<Record.Entry, String> = mutableMapOf()
        var comments: MutableList<Record.Comment> = mutableListOf()
        for (rec in records) {
            // Add the comment to comments
            when (rec) {
                is Record.Comment -> {
                    comments.add(rec)
                    continue
                }
                is Record.Entry -> {
                    // Add this combination to rows
                    rows[rec] = StringBuilder().apply {
                        comments.map {
                            this.appendLine(it.content.trim())
                        }
                    }.toString().trim()

                    // Reset comments
                    comments = mutableListOf()
                }
            }
        }

        echo(table {
            borderType = BorderType.SQUARE_DOUBLE_SECTION_SEPARATOR
            header {
                cellBorders = Borders.BOTTOM
                row("Address", "Hostname", "Aliases", "Comments")
            }
            body {
                cellBorders = Borders.BOTTOM

                for (r in rows) {
                    val rec = r.key

                    this.row(rec.address,
                        rec.hostname,
                        rec.aliases.joinToString("\n"),
                        r.value
                    )
                }
            }
            footer {
                style(italic = true)
                row{
                    cellBorders = Borders.NONE
                    cell("⚡ Found ${rows.size} records.")
                }
            }
        })
    }
}