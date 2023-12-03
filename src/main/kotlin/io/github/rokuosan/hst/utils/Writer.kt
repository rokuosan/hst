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
    
    fun write(record: Record, target: File? = null) {
        val text = if (record is Record.Entry) {
            "${record.address} ${record.hostname} ${record.aliases.joinToString(" ")}".trimEnd()
        }else{
            val c = record as Record.Comment
            "# ${c.content}"
        }
        
        write(text, target)
    }
    
    private fun write(text: String, target: File? = null) {
        val file = target ?: getTargetFile()

        if (!file.exists()) file.createNewFile()
        file.appendText(StringBuilder().appendLine(text).toString())
    }
}