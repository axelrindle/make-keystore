plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = System.getenv("BUILD_IMAGE_NAME") ?: "make-keystore"
