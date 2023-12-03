package io.github.rokuosan.hst

import com.github.ajalt.clikt.core.subcommands
import io.github.rokuosan.hst.commands.Add
import io.github.rokuosan.hst.commands.Remove
import io.github.rokuosan.hst.commands.Show

fun main(args: Array<String>) = runCommand(args)

fun runCommand(args: Array<String>) = Hst()
    .subcommands(Add(), Remove(), Show())
    .main(args)
