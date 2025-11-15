package de.axelrindle.ksg.cmd

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.CoreCliktCommand
import kotlin.system.exitProcess

class ExitCommand : CoreCliktCommand() {

    override fun help(context: Context): String =
        """
            Exit the program.
        """.trimIndent()

    override fun run() {
        exitProcess(0)
    }
}
