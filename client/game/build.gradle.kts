group = "net.cryptic"
version = "0.0.1"

plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "net.runelite.client.Application")
    }
}

dependencies {
    implementation(project(":runelite"))
    implementation(group = "com.github.akman", name = "jpackage-maven-plugin", version = "0.1.5")

    val slf4j = "2.0.7"
    implementation("org.slf4j:slf4j-api:$slf4j")

    implementation("one.util:streamex:0.8.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.guava:guava:31.1-jre")

    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("io.netty:netty-all:4.1.92.Final")
    implementation(group = "com.google.inject", name = "guice", version = "5.0.1")
    implementation("javax.vecmath:vecmath:1.5.2")
    implementation(group = "java3d", name = "j3d-core", version = "1.3.1")


    val lombok = module("org.projectlombok", "lombok", "1.18.26")
    compileOnly(lombok)
    annotationProcessor(lombok)
    testCompileOnly(lombok)
    testAnnotationProcessor(lombok)
}

tasks {
    register<JavaExec>("Run-Normal") {
        group = "Runelite"
        description = "Run Runelite in Normal Mode"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("Application")
    }

    register<JavaExec>("Run-Development") {
        group = "Runelite"
        description = "Run Runelite in Development Mode"
        enableAssertions = true
        args = listOf("--developer-mode")
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("Application")
    }

    jar {
        destinationDirectory.set(file("${rootProject.buildDir}\\libs\\"))
        archiveBaseName.set(project.name)
        from(sourceSets.main.get().resources) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }

}

application {
    mainClass.set("Application")
}