plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    // Apply the plugin here
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

// Configure the Shadow task specifically
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    manifest {
        attributes["Main-Class"] = "com.tejas.securechatserver.ApplicationKt"
    }
    archiveBaseName.set("app")
    archiveClassifier.set("")
    archiveVersion.set("")
}

application {
    mainClass.set("com.tejas.securechatserver.ApplicationKt")
}