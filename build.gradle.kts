plugins {
    id("java") apply true
    id("com.gradleup.shadow") version "9.2.0" apply true
    id("aggregate-javadocs")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(project(":boomerang-core"))
    implementation(project(":boomerang-proto"))
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.gradleup.shadow:shadow-gradle-plugin:9.2.0")
    }

}


tasks.shadowJar {
    archiveVersion = "1.0.0-SNAPSHOT"

    configurations = project.configurations.runtimeClasspath.map { listOf(it) }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.boomerang.core.BoomerangServer"
        attributes["Description"] = "An open-source Minecraft server software"
    }
}