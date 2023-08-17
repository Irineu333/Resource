plugins {
    kotlin("jvm") version "1.9.0"
}

group = "com.neo.resource"
version = "1.0-DEV"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}