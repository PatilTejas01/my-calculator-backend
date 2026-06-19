plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("com.github.johnrengelman.shadow") version "8.1.1" // Required for Fat JAR
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.tejas.securechatserver.ApplicationKt"
    }
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "com.tejas.securechatserver.ApplicationKt"
    }
}