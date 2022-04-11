package org.glavo.mic.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.jvm.tasks.Jar;

import java.io.File;

public class CompileModuleInfoPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(JavaPlugin.class);

        CompileModuleInfo compileModuleInfo = project.getTasks().create("compileModuleInfo", CompileModuleInfo.class, task -> {
            task.getSourceFile().set(project.file("src/main/java/module-info.java"));
            task.getTargetFile().set(new File(project.getBuildDir(), "module-info/module-info.class"));
        });

        Jar jarTask = (Jar) project.getTasks().getByName("jar");
        jarTask.dependsOn(compileModuleInfo);
        jarTask.from(compileModuleInfo.getTargetFile());
    }
}
