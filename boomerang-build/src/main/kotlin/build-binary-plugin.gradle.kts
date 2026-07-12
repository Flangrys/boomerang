plugins {
    id("java")
    id("application")
}


repositories {
    val dataVersion = libs.boomerang.datafix.get().version ?: ""

    val dataVersionStage = dataversion.split("-")[0]

    when(dataVersionStage) {
        "DEV" -> mavenLocal()
        else -> mavenCentral()
    }
}