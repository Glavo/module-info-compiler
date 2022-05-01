# Module Info Compiler
[![](https://img.shields.io/maven-central/v/org.glavo/module-info-compiler?label=Maven%20Central)](https://search.maven.org/artifact/org.glavo/module-info-compiler)
[![](https://img.shields.io/gradle-plugin-portal/v/org.glavo.compile-module-info-plugin)](https://plugins.gradle.org/plugin/org.glavo.compile-module-info-plugin)

A tool for compiling `module-info.java` for Java alone. 

It can easily compile `module-info.java` for your Java 8 project to fit JPMS without complicated module path configuration.

It can be used as a dependency library, as a command line tool, or as a Gradle plugin.

It only parses the syntax of `module-info.java` without checking the actual module.
If you really don't know how to configure javac to compile `module-info.java` correctly in a complex project, it can help you.

This tool does not depend on javac, it can run on Java 8 or higher, 
and you can configure the major version of the target class file.

## Usage

### Gradle Plugin

Using the plugins:

Groovy DSL:
```groovy
plugins {
  id "org.glavo.compile-module-info-plugin" version "2.0"
}
```


Kotlin DSL:
```kotlin
plugins {
  id("org.glavo.compile-module-info-plugin") version "2.0"
}
```

It takes over compiling `module-info.java`, so you can easily mix `module-info.java` in your Java 8 project.

You can also do some configuration like this:

```kotlin
tasks.named<org.glavo.mic.tasks.CompileModuleInfo>("compileModuleInfo") {
    targetCompatibility     = 9             // Optional, defaults to 9
    encoding                = "UTF-8"       // Optional, defaults to UTF-8
    moduleVersion           = "1.0.0"       // Optional
    moduleMainClass         = "simple.Main" // Optional
}
```

### Use as a CLI tool

Download the jar from the [release page](https://github.com/Glavo/module-info-compiler/releases/),
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
    <version>2.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("org.glavo:module-info-compiler:2.0")
```
