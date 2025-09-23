package de.axelrindle.ksg.cmd

import de.axelrindle.ksg.test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.Parameter
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.MethodSource
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyStore
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.io.path.isRegularFile

const val KEYSTORE_PASSWORD = "changeme"

@ParameterizedClass
@MethodSource("generateFilename")
class CreateCommandTest {

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun generateFilename() = Path("cacerts-${UUID.randomUUID()}.p12")
    }

    @Parameter
    lateinit var path: Path

    @AfterEach
    fun cleanup() {
        Files.delete(path)
    }

    @Test
    fun test_basic() {
        val result = test<CreateCommand>("--output $path")
        Assertions.assertEquals(0, result.statusCode)

        Assertions.assertTrue { path.isRegularFile() }
    }

    @Test
    fun test_custom_input() {
        val result = test<CreateCommand>("--input test/snapshots/certs --password $KEYSTORE_PASSWORD --output $path")
        Assertions.assertEquals(0, result.statusCode)
        Assertions.assertTrue { path.isRegularFile() }

        val store = KeyStore.getInstance("PKCS12")
        Assertions.assertDoesNotThrow { path.inputStream().use { store.load(it, KEYSTORE_PASSWORD.toCharArray()) } }
        Assertions.assertEquals(1, store.size())
    }

    @Test
    fun test_custom_password() {
        val result = test<CreateCommand>("--password anotherone --output $path")
        Assertions.assertEquals(0, result.statusCode)

        Assertions.assertTrue { path.isRegularFile() }

        val store = KeyStore.getInstance("PKCS12")
        Assertions.assertThrows(IOException::class.java) { path.inputStream().use { store.load(it, KEYSTORE_PASSWORD.toCharArray()) } }
        Assertions.assertDoesNotThrow { path.inputStream().use { store.load(it, "anotherone".toCharArray()) } }
        Assertions.assertEquals(0, store.size())
    }

}
