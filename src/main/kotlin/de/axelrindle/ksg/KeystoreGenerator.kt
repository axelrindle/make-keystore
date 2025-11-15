package de.axelrindle.ksg

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parsers.CommandLineParser
import de.axelrindle.ksg.cmd.CreateCommand
import de.axelrindle.ksg.cmd.ExitCommand
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

fun main() {
    val shouldContinue = AtomicBoolean(true)
    Runtime.getRuntime().addShutdownHook(Thread {
        shouldContinue.set(false)
    })

    val cmd = RootCommand().subcommands(ExitCommand())

    val scanner = Scanner(System.`in`)
    while(shouldContinue.get()) {
        print("$ ")
        val input = scanner.nextLine()
        val args = CommandLineParser.tokenize(input)

        cmd.main(args)
    }
}

class RootCommand : CoreCliktCommand(Config.APPLICATION_NAME) {
    override fun run() = Unit

    init {
        versionOption(javaClass.`package`.implementationVersion ?: "dev")

        subcommands(
            CreateCommand()
        )
    }
}
