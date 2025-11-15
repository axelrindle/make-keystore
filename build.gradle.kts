plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.graalvm)

    application
}

group = "de.axelrindle"
version = libs.versions.application.get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.clikt) {
        exclude(group = "com.github.ajalt.mordant", module = "mordant")
    }

    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.bundles.clikt)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<ProcessResources> {
    filesMatching("banner.txt") {
        filter { it.replace("%APP_VERSION%", version.toString() ) }
    }
}

application {
    mainClass = appMainClass
}

graalvmNative {
    toolchainDetection = true

    binaries.named("main") {
        imageName.set(rootProject.name)
        this.mainClass.set(appMainClass)
        useFatJar.set(true)
    }
}
