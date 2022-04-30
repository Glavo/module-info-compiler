plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.18.0"
}

dependencies {
    compileOnly(gradleApi())

    implementation(rootProject)
}

configurations.named(JavaPlugin.API_CONFIGURATION_NAME) {
    dependencies.remove(project.dependencies.gradleApi())
}

pluginBundle {
    website = "https://github.com/Glavo/module-info-compiler"
    vcsUrl = "https://github.com/Glavo/module-info-compiler.git"
    tags = listOf("java", "modules", "jpms", "modularity")
}

gradlePlugin {
    plugins {
        create("compileModuleInfoPlugin") {
            id = "org.glavo.compile-module-info"
            displayName = "Compile Module Info Plugin"
            description = rootProject.description
            implementationClass = "org.glavo.mic.plugin.CompileModuleInfoPlugin"
        }
    }
}

