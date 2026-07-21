val aggregateJavadocs = tasks.register<Javadoc>("allJavadoc") {
    group = "documentation"
    description = "Genera la documentación Javadoc unificada."

    val javaProjects = rootProject.subprojects.filter { it.plugins.hasPlugin("java") }

    source(javaProjects.map { it.the<SourceSetContainer>()["main"].allJava })

    classpath = files(javaProjects.map { it.the<SourceSetContainer>()["main"].compileClasspath })

    destinationDir = file("${rootProject.layout.buildDirectory.get()}/docs/javadocs")

    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        charSet = "UTF-8"
        isAuthor = true
        isVersion = true
    }
}