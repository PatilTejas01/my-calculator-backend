plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.tejas.securechatserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Core Ktor Server Framework & Netty Engine
    implementation("io.ktor:ktor-server-core-jvm:2.3.11")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.11")

    // WebSockets Protocol Engine
    implementation("io.ktor:ktor-server-websockets-jvm:2.3.11")

    // JSON parsing processing library
    implementation("org.json:json:20240303")

    // Logging deployment tracking
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

// Forces the compiler to look inside your java/ folder for Kotlin files
sourceSets {
    main {
        kotlin.setSrcDirs(listOf("src/main/java"))
    }
}

application {
    mainClass.set("com.tejas.securechatserver.ApplicationKt")
}