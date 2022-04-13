# Module Info Compiler

[![](https://img.shields.io/maven-central/v/org.glavo/module-info-compiler?label=Maven%20Central)](https://search.maven.org/artifact/org.glavo/module-info-compiler)

A tool for compiling `module-info.java` for Java alone.

It can easily compile `module-info.java` for your Java 8 project to fit JPMS without complicated module path configuration.

It only parses the syntax of `module-info.java` without checking the actual module.
If you really don't know how to configure javac to compile `module-info.java` correctly in a complex project, it can help you.

This tool does not depend on javac, it can run on Java 8 or higher, 
and you can configure the major version of the target class file.

## Usage

Currently this tool supports being used as a command line tool, and also supports being added as a Maven dependency.
It also includes a prebuilt Gradle task type. In the future I may provide a Gradle plugin to use it.

### Use as a CLI tool

Download the jar from the [release page](https://github.com/Glavo/GMIC/releases/),
run it with `java -jar module-info-compiler.jar`.

The parameters it accepts are as follows:

```
Usage: gmic [-target <target compatibility>] [-main-class <module main class>] [-module-version <module version>] <source file> <target file>
```

### Added as a Maven dependency

This tool has been published on Maven Central, you can add dependencies to it like this:

Maven:
```xml
<dependency>
    <groupId>org.glavo</groupId>
    <artifactId>module-info-compiler</artifactId>
    <version>1.2</version>
</dependency>
```

Gradle:

```kotlin
implementation("org.glavo:module-info-compiler:1.2")
```

### Gradle Task (`CompileModuleInfo`)

First, you can add it to the classpath of your Gradle build script like this:

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.glavo:module-info-compiler:1.2")
    }
}
```

Then you can create a task that compiles `module-info.java` like this:

```kotlin
tasks.create<org.glavo.mic.tasks.CompileModuleInfo>("compileModuleInfo") {
    sourceFile.set(file("src/main/module-info.java"))
    targetFile.set(buildDir.resolve("classes/java/module-info/module-info.class"))

    targetCompatibility = 9         // Optional, defaults to 9
    encoding = "UTF-8"              // Optional, defaults to UTF-8
    moduleVersion = "1.0.0"         // Optional
    moduleMainClass = "simple.Main" // Optional
}
```

Then you can include it inside the jar like this:

```kotlin
tasks.jar {
    dependsOn(tasks["compileModuleInfo"])
    from((tasks["compileModuleInfo"] as org.glavo.mic.tasks.CompileModuleInfo).targetFile)
}
```