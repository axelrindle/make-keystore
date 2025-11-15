package de.axelrindle.ksg

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import de.axelrindle.ksg.cmd.CreateCommand

fun main(argv: Array<String>) {
    printBanner()

    RootCommand().main(argv)
}

class RootCommand : CoreCliktCommand(Config.APPLICATION_NAME) {
    override fun run() = Unit

    init {
        versionOption(appVersion())

        subcommands(
            CreateCommand(),
        )
    }
}
