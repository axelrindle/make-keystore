package de.axelrindle.ksg

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parsers.CommandLineParser
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

fun main() {
    printBanner()

    val shouldContinue = AtomicBoolean(true)
    Runtime.getRuntime().addShutdownHook(Thread {
        shouldContinue.set(false)
    })

    val cmd = RootCommand()

    val scanner = Scanner(System.`in`)
    while(shouldContinue.get()) {
        print("$ ")
        val input = scanner.nextLine()
        val args = CommandLineParser.tokenize(input)

        cmd.main(args)
    }
}
