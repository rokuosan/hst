package io.github.rokuosan.hst.utils

import com.charleskorn.kaml.Yaml
import io.github.rokuosan.hst.models.Record
import io.github.rokuosan.hst.models.YamlConfig
import kotlinx.serialization.decodeFromString
import java.io.File
import java.lang.Exception
import kotlin.io.path.Path

object Reader {
    fun getRecords(): List<Record> {
        return mutableListOf<Record>().apply {
            getRawRecords().forEach{ line ->
                rowToRecord(line)?.let { this.add(it) }
            }
        }
    }

    /**
     * getRawRecords reads entries from hosts file defined by config
     */
    fun getRawRecords(): List<String> {
        val conf = getConfig()?:return emptyList()
        val file = Path(conf.path).toFile()
        if (!file.exists()) return emptyList()

        file.useLines {
            return it.toList()
        }
    }


    fun rowToRecord(row: String): Record?{
        return if (row.startsWith("#")) {
            Record.Comment(row.removePrefix("#"))
        }else {
            val split = row.replace(Regex("""\s+"""), " ")
                .split(" ", limit = 3)
            if (split.isEmpty() || split[0].isBlank()) return null

            val aliases = mutableListOf<String>()
            if (split.size > 2) {
                aliases.addAll(split[2].split(" "))
            }
            Record.Entry(
                address = split[0],
                hostname = split[1],
                aliases = aliases
            )
        }
    }

    fun getConfig(): YamlConfig? {
        val name = "hst.config"
        val extensions = arrayOf("yaml", "yml")

        // Search config file.
        // Returns an empty string if it does not match any filenames.
        val configFile = fun(): File?{
            for (ext in extensions) {
                val fn = "${name}.$ext"
                try {
                    val file = Path(fn).toFile()
                    if (file.exists()) {
                        return file
                    }
                } catch (_: Exception) {
                }
            }
            return null
        }()

        if (configFile == null) {
            System.getProperty("os.name")?.let {
                if (it.contains("Windows", ignoreCase = true)) {
                    return YamlConfig(path = "C:\\Windows\\System32\\drivers\\etc\\hosts")
                }else {
                    return YamlConfig(path = "/etc/hosts")
                }
            }

            return YamlConfig("/etc/hosts")
        }

        // Load Config
        val raw = configFile.bufferedReader().readText()
        return try {
            val config: YamlConfig = Yaml.default.decodeFromString(raw)
            config
        }catch (_: Exception){
            null
        }
    }
}