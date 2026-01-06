plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("dev.or2:all:2.3.5")
}

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "com.cryptic.UpdateCacheKt")
    }
}

tasks {

    register<JavaExec>("Run JS5") {
        group = "Service"
        description = "Runs the Js5 Server"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("com.cryptic.RunJs5Kt")
        jvmArgs = listOf(
            "-XX:-OmitStackTraceInFastThrow",
            "-Xmx8g",
            "-Xms4g",
            "-XX:+UseZGC",
            "-XX:MaxGCPauseMillis=100",
            "-Dio.netty.tryReflectionSetAccessible=true",
        )
    }

    register<JavaExec>("Update Cache") {
        group = "Service"
        description = "Update Cache to the defined revision"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("com.cryptic.UpdateCacheKt")
    }

    register<JavaExec>("Build Cache") {
        group = "Service"
        description = "Pack all custom Files into the cache"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("com.cryptic.BuildCacheKt")
    }

}

