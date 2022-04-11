# Module Info Compiler

A tool for compiling `module-info.java` for Java alone.

It can easily compile `module-info.java` for your Java 8 project to fit JPMS without complicated module path configuration.

It only parses the syntax of `module-info.java` without checking the actual module.
If you really don't know how to configure javac to compile `module-info.java` correctly in a complex project, it can help you.

## Usage

Currently this tool supports being used as a command line tool, and also supports being added as a Maven dependency.
A Gradle plugin will be provided in the future.

### Use as a CLI tool

Download the jar from the [release page](https://github.com/Glavo/GMIC/releases/),
run it with `java -jar module-info-compiler.jar`.

The parameters it accepts are as follows:

```
Usage: gmic [-target <target compatibility>] [-main-class <module main class>] [-module-version <module version>] <source file> <target file>
```

### Added as a Maven dependency

(Maven Central takes a while to synchronize dependencies, if you can't get them, please wait)

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