package de.axelrindle.ksg

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.parsers.CommandLineParser
import com.github.ajalt.clikt.testing.CliktCommandTestResult
import com.github.ajalt.clikt.testing.test
import java.util.logging.Logger

val logger = Logger.getLogger("AppTest")

inline fun <reified C : CoreCliktCommand> test(
    cmdline: String = ""
): CliktCommandTestResult {
    val argv = CommandLineParser.tokenize(cmdline)
    val instance = C::class.java.getConstructor().newInstance()

    logger.info("calling ${instance.javaClass.name} with args: ${argv.joinToString(" ")}")

    return instance.test(
        argv = argv,
        parse = instance::parse
    )
}
