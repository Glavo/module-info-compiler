plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.glavo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.javaparser:javaparser-core:3.24.2")
    // implementation("com.github.javaparser:javaparser-symbol-solver-core:3.24.2")
    implementation("org.ow2.asm:asm:9.3")



    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "org.glavo.mic.ModuleInfoCompiler"
        )
    }
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    // manifest.inheritFrom(project.tasks.getByName<Jar>("jar").manifest)

    relocate("org.objectweb.asm", "org.glavo.mic.asm")
    relocate("com.github.javaparser", "org.glavo.mic.javaparser")
    minimize()
}