plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("com.mark.bootstrap.bootstrap")
}


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.0")
    }
}

/*
configure<com.mark.bootstrap.BootstrapPluginExtension> {
    uploadType.set(com.mark.bootstrap.UploadType.FTP)
    buildType.set("normal")
    customRepo.set("https://zaryte.io/client/normal/repo/")
    passiveMode.set(true)
    externalLibs.set(listOf(File("${rootProject.buildDir}\\libs\\")))
}
*/


allprojects {
    apply(plugin = "java")
    apply(plugin = "application")
    apply(plugin = "com.github.johnrengelman.shadow")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
}

dependencies {
    implementation(project("game"))
}

tasks.withType<JavaCompile>().configureEach {
    options.isWarnings = false
    options.isDeprecation = false
    options.isIncremental = true
}

tasks {
    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\"))
    }
}

application {
    mainClass.set("Application")
}

tasks {
    val gameProjectPath = "game"
    val runeliteProjectPath = "runelite"

    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\tmp\\"))
    }

}