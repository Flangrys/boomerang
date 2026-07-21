plugins {
    id("java")
}

group = "com.boomerang.core"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":boomerang-proto"))

    implementation("commons-cli:commons-cli:1.11.0")
    implementation("io.netty:netty-all:4.1.121.Final")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")

    implementation("com.fasterxml.jackson.core:jackson-core:2.21.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.5")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.21.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}