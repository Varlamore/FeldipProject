import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "com.cryptic.UpdateCacheKt")
    }
}

allprojects {
    apply(plugin = "kotlin")

    group = "com.cryptic"
    version = "1.0"

    repositories {
        mavenCentral()
        maven(url = "https://repo.runelite.net/")
        maven(url = "https://repo.openrs2.org/repository/openrs2-snapshots/")
        maven(url = "https://raw.githubusercontent.com/OpenRune/hosting/main")
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("io.github.microutils:kotlin-logging:3.0.5")
        val slf4jVersion = "2.0.9"
        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
        implementation("me.tongfei:progressbar:0.9.2")
        implementation("org.apache.ant:ant-xz:1.10.7")
        compileOnly("org.projectlombok:lombok:1.18.30")
        implementation("dev.or2:all:2.3.5")
    }

    kotlin {
        jvmToolchain(17)
    }

}
