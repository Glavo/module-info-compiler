plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.gradle.plugin-publish") version "1.0.0-rc-1"
}

dependencies {
    compileOnly(gradleApi())
    implementation(rootProject)
}

tasks.jar {
    enabled = false
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set(null as String?)

    relocate("org.objectweb.asm", "org.glavo.mic.plugin.asm")
    relocate("com.github.javaparser", "org.glavo.mic.plugin.javaparser")
    minimize()
}

gradlePlugin {
    plugins {
        create("kala-retro8") {
            id = "org.glavo.mic.plugin"
            displayName = "ModuleInfo Compiler"
            description = ""
            implementationClass = "kala.plugins.retro8.Retro8Plugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/Glavo/kala-retro8"
    vcsUrl = "https://github.com/Glavo/kala-retro8.git"
    tags = listOf("java", "modules", "jpms", "modularity")
}

publishing {
    repositories {
        mavenLocal()
    }

    publications {
        this.create<MavenPublication>("pluginMaven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}