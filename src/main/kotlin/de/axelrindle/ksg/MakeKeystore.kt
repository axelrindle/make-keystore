package de.axelrindle.ksg

import com.github.ajalt.clikt.core.CoreCliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import java.security.KeyStore
import java.security.cert.CertificateFactory
import kotlin.io.path.*
import kotlin.system.exitProcess

class MakeKeystore : CoreCliktCommand() {

    init {
        versionOption(javaClass.`package`.implementationVersion)
    }

    val input by option()
        .default("/etc/ssl/certs")
        .help("input directory")

    val password by option()
        .default("changeme")
        .help("keystore password")

    val additional by option()
        .multiple()
        .help("additional pem certificates to add")

    override fun run() {
        val directorySslCerts = Path(input)
        val keystorePassword = password.toCharArray()

        if (directorySslCerts.isDirectory().not()) {
            exitProcess(1)
        }

        val factory = CertificateFactory.getInstance("X.509")
        val store = KeyStore.getInstance("PKCS12")
        store.load(null, keystorePassword)

        directorySslCerts.listDirectoryEntries("*.pem")
            .plus(additional.map { Path(it) })
            .sorted()
            .forEach {
                val cert = it.inputStream().use { pem -> factory.generateCertificate(pem) }
                store.setCertificateEntry(it.name, cert)
            }

        Path("cacerts").outputStream().use { ous ->
            store.store(ous, keystorePassword)
        }
    }
}