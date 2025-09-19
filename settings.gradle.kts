plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = System.getenv("G_IMAGE_NAME") ?: "make-keystore"
