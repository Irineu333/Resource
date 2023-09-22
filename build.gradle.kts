plugins {
    kotlin("jvm") version "1.9.0"
    id("java-library")
    id("maven-publish")
}

group = "com.neo.resource"
version = "1.1.0-DEV"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.ORACLE)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.neo.resource"
            artifactId = "resource"
            version = "1.1.0-DEV"

            from(components["java"])
        }
    }
}