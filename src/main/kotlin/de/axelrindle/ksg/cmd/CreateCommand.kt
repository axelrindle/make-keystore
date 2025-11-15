package de.axelrindle.ksg.cmd

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.boolean
import de.axelrindle.ksg.KEYSTORE_DEFAULT_PASSWORD
import de.axelrindle.ksg.makeFactory
import de.axelrindle.ksg.makeStore
import kotlin.io.path.*

const val GLOB_CERTIFICATE = "*.{crt,pem}"

class CreateCommand : CoreCliktCommand() {

    override fun help(context: Context): String =
        """
            Create a new Keystore file.

            Specify multiple input files and directories for importing certificates. Only .pem files are accepted.
        """.trimIndent()

    override fun helpEpilog(context: Context): String =
        """
            Example:
              $ make-keystore create --input=/etc/ssl/certs --input=my_root_ca.pem --password=another --output=trust.p12
        """.trimIndent()

    val input by option()
        .multiple()
        .help("input files and directories")
        .validate { it.isNotEmpty() }

    val output by option()
        .default("cacerts.p12")
        .help("output file")
        .validate { it.isNotEmpty() }

    val password by option()
        .default(KEYSTORE_DEFAULT_PASSWORD)
        .help("keystore password")

    val dryRun by option()
        .boolean()
        .default(false)
        .help("simulate keystore creation without writing to disk")

    override fun run() {
        val keystorePassword = password.toCharArray()

        val path = Path(output)
        val factory = makeFactory()
        val store = makeStore()
        store.load(null, keystorePassword)

        input.map { Path(it) }
            .flatMap {
                if (it.isRegularFile()) {
                    return@flatMap listOf(it)
                }
                else if (it.isDirectory()) {
                    return@flatMap it.listDirectoryEntries(GLOB_CERTIFICATE)
                }

                throw IllegalArgumentException("$it is neither a file nor a directory")
            }
            .forEach {
                echo("adding certificate to keystore: ${it.absolutePathString()}")
                val cert = it.inputStream().use { pem -> factory.generateCertificate(pem) }
                store.setCertificateEntry(it.name, cert)
            }

        echo("created keystore with ${store.size()} entries")

        if (dryRun.not()) {
            path.outputStream().use { ous ->
                store.store(ous, keystorePassword)
            }
        }
    }
}
