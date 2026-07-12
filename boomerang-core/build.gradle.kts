plugins {
    id("java")
}

group = "com.boomerang.core"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":boomerang-proto"))

    implementation("commons-cli:commons-cli:1.11.0")
    implementation("io.netty:netty-all:4.1.121.Final")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}