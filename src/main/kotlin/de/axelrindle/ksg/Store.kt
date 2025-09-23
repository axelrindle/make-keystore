package de.axelrindle.ksg

import java.security.KeyStore
import java.security.cert.CertificateFactory

fun makeFactory(): CertificateFactory = CertificateFactory.getInstance("X.509")

fun makeStore(): KeyStore = KeyStore.getInstance("PKCS12")
