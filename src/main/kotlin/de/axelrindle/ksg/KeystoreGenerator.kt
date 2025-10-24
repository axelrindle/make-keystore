package de.axelrindle.ksg

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parsers.CommandLineParser
import de.axelrindle.ksg.cmd.CreateCommand

fun main(argv: Array<String>) {
    val argu = System.getenv("MK_ARGS")
    val args = if (argu != null) CommandLineParser.tokenize(argu) else argv.toList()

    RootCommand()
        .subcommands(CreateCommand())
        .main(args)
}

    class RootCommand : CoreCliktCommand(Config.APPLICATION_NAME) {
    override fun run() = Unit

    init {
        versionOption(javaClass.`package`.implementationVersion ?: "dev")
    }
}
