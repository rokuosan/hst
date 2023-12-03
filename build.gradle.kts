plugins {
    application
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.20"

    id("org.graalvm.buildtools.native") version "0.9.27"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.rokuosan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.2.1")
    implementation("io.klogging:klogging-jvm:0.5.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.charleskorn.kaml:kaml:0.55.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("io.github.rokuosan.hst.MainKt")
}

graalvmNative {
    binaries {
        named("main") {
            javaLauncher.set(javaToolchains.launcherFor{
                languageVersion.set(JavaLanguageVersion.of(21))
                vendor.set(JvmVendorSpec.matching("GraalVM"))
            })
        }
    }
}