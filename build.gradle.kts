plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.graalvm)
}

group = "de.axelrindle"
version = libs.versions.application

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.clikt)
}

kotlin {
    jvmToolchain(Integer.parseInt(libs.versions.jvm.get()))
}

val appMainClass = "de.axelrindle.ksg.KeystoreGeneratorKt"

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "Main-Class" to appMainClass,
            "Implementation-Version" to version
        ))
    }
}

graalvmNative {
    toolchainDetection = true

    binaries.named("main") {
        imageName.set(rootProject.name)
        this.mainClass.set(appMainClass)
        useFatJar.set(true)
    }
}
