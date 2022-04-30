import java.util.Properties

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

allprojects {
    apply {
        plugin("java-library")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "org.glavo"
    version = "2.0-RC1" + "-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    tasks.withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).also {
            it.encoding("UTF-8")
            it.addStringOption("link", "https://docs.oracle.com/en/java/javase/17/docs/api/")
            it.addBooleanOption("html5", true)
            it.addStringOption("Xdoclint:none", "-quiet")
        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.jar {
        archiveClassifier.set("core")
    }

    tasks.shadowJar {
        archiveClassifier.set("")

        relocate("org.objectweb.asm", "org.glavo.mic.asm")
        relocate("com.github.javaparser", "org.glavo.mic.javaparser")

        minimize()
    }
}


dependencies {
    implementation("com.github.javaparser:javaparser-core:3.24.2")
    implementation("org.ow2.asm:asm:9.3")
}

loadMavenPublishProperties()

description = "Compiler for module-info.java"

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "org.glavo.mic.ModuleInfoCompiler"
        )
    }
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {

            project.shadow.component(this)

            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name
            //artifact(tasks.shadowJar)
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/Glavo/GMIC")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("glavo")
                        name.set("Glavo")
                        email.set("zjx001202@gmail.com")
                    }
                }

                scm {
                    url.set("https://github.com/Glavo/GMIC")
                }
            }
        }
    }
}

if (rootProject.ext.has("signing.key")) {
    signing {
        useInMemoryPgpKeys(
            rootProject.ext["signing.keyId"].toString(),
            rootProject.ext["signing.key"].toString(),
            rootProject.ext["signing.password"].toString(),
        )
        sign(publishing.publications["maven"])
    }
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(rootProject.ext["sonatypeStagingProfileId"].toString())
            username.set(rootProject.ext["sonatypeUsername"].toString())
            password.set(rootProject.ext["sonatypePassword"].toString())
        }
    }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

fun loadMavenPublishProperties() {
    var secretPropsFile = project.rootProject.file("gradle/maven-central-publish.properties")
    if (!secretPropsFile.exists()) {
        secretPropsFile =
            file(System.getProperty("user.home")).resolve(".gradle").resolve("maven-central-publish.properties")
    }

    if (secretPropsFile.exists()) {
        // Read local.properties file first if it exists
        val p = Properties()
        secretPropsFile.reader().use {
            p.load(it)
        }

        p.forEach { (name, value) ->
            rootProject.ext[name.toString()] = value
        }
    }

    listOf(
        "sonatypeUsername" to "SONATYPE_USERNAME",
        "sonatypePassword" to "SONATYPE_PASSWORD",
        "sonatypeStagingProfileId" to "SONATYPE_STAGING_PROFILE_ID",
        "signing.keyId" to "SIGNING_KEY_ID",
        "signing.password" to "SIGNING_PASSWORD",
        "signing.key" to "SIGNING_KEY"
    ).forEach { (p, e) ->
        if (!rootProject.ext.has(p)) {
            rootProject.ext[p] = System.getenv(e)
        }
    }
}
