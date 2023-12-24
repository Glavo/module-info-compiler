/*
 * Copyright 2023 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glavo.mic;

import org.glavo.mic.tasks.CompileModuleInfo;
import org.gradle.api.*;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.util.GradleVersion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class CompileModuleInfoPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply("java");

        SourceSet main;
        try {
            if (GradleVersion.current().compareTo(GradleVersion.version("7.1")) >= 0) {
                JavaPluginExtension javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
                main = javaPluginExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            } else {
                @SuppressWarnings("deprecation")
                JavaPluginConvention javaConvention = project.getConvention().getPlugin(JavaPluginConvention.class);
                main = javaConvention.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            }
        } catch (IllegalStateException | UnknownDomainObjectException e) {
            throw new GradleException("Cannot obtain JavaPluginConvention", e);
        }

        Optional<Path> moduleInfoJava = main.getAllJava()
                .getSourceDirectories()
                .getFiles()
                .stream()
                .map(sourceDir -> sourceDir.toPath().resolve("module-info.java"))
                .filter(Files::exists)
                .findAny();

        if (moduleInfoJava.isPresent()) {
            JavaCompile compileJava = (JavaCompile) project.getTasks().getByName("compileJava");
            compileJava.getModularity().getInferModulePath().set(false);
            compileJava.exclude("module-info.java");

            Path path = moduleInfoJava.get();
            Path outputDir = project.getLayout().getBuildDirectory().getAsFile().get().toPath().resolve("classes").resolve("module-info").resolve("main");
            main.getOutput().dir(outputDir);
            //noinspection Convert2Lambda
            TaskProvider<CompileModuleInfo> compileModuleInfo = project.getTasks().register("compileModuleInfo", CompileModuleInfo.class, new Action<CompileModuleInfo>() {
                @Override
                public void execute(CompileModuleInfo task) {
                    task.getSourceFile().set(path.toFile());
                    task.getTargetFile().set(outputDir.resolve("module-info.class").toFile());
                }
            });

            project.getTasks().getByName("classes").dependsOn(compileModuleInfo);
        }
    }
}
