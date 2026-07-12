plugins {
    id("java")
}

group = "com.boomerang.proto"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.121.Final")
    implementation("org.jetbrains:annotations:24.0.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}