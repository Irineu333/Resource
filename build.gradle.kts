plugins {
    kotlin("jvm") version "1.9.0"
    id("java-library")
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.neo.resource"
            artifactId = "resource"
            version = "1.0-DEV"

            from(components["java"])
        }
    }
}