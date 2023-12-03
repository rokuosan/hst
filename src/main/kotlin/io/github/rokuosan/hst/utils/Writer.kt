package io.github.rokuosan.hst.utils

import io.github.rokuosan.hst.models.Record
import java.io.File
import kotlin.io.path.Path

object Writer {
    private fun getTargetFile(): File {
        val conf = Reader.getConfig()?:run { 
            error("No config file provided.")
        }
        return Path(conf.path).toFile()
    }
    
    fun append(record: Record, target: File? = null) {
        val text = if (record is Record.Entry) {
            "${record.address} ${record.hostname} ${record.aliases.joinToString(" ")}".trimEnd()
        }else{
            val c = record as Record.Comment
            "# ${c.content}"
        }

        append(text, target)
    }
    
    private fun append(text: String, target: File? = null) {
        val file = target ?: getTargetFile()

        if (!file.exists()) file.createNewFile()
        file.appendText(StringBuilder().appendLine(text).toString())
    }

    fun writeAll(texts: List<String>, target: File? = null) {
        val file = target ?: getTargetFile()
        if (!file.exists()) file.createNewFile()
        val sb = StringBuilder().apply {
            texts.forEach {
                this.appendLine(it)
            }
        }

        file.bufferedWriter().use {
            it.write(sb.toString())
        }
    }
}