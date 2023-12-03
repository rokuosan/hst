package io.github.rokuosan.hst.commands

import com.github.ajalt.clikt.core.CliktCommand
import io.github.rokuosan.hst.utils.Reader

class Show: CliktCommand() {
    override fun run() {
        val records = Reader.getRecords()

        records.map(::echo)
    }
}