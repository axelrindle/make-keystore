package de.axelrindle.ksg

fun appVersion() = RootCommand::class.java.`package`.implementationVersion ?: "dev"

fun printBanner() {
    RootCommand::class.java.getResourceAsStream("/banner.txt")?.copyTo(System.out)
}
