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
package org.glavo.mic.tasks;

import org.glavo.mic.ModuleInfoCompiler;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.*;

import java.io.*;
import java.nio.file.Files;

public abstract class CompileModuleInfo extends DefaultTask {
    private int targetCompatibility = ModuleInfoCompiler.DEFAULT_TARGET_COMPATIBILITY;
    private String moduleVersion;
    private String moduleMainClass;
    private String encoding = "UTF-8";

    @InputFile
    public abstract RegularFileProperty getSourceFile();

    @OutputFile
    public abstract RegularFileProperty getTargetFile();

    @Input
    public int getTargetCompatibility() {
        return targetCompatibility;
    }

    public void setTargetCompatibility(int targetCompatibility) {
        this.targetCompatibility = targetCompatibility;
    }

    @Input
    @Optional
    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    @Input
    @Optional
    public String getModuleMainClass() {
        return moduleMainClass;
    }

    public void setModuleMainClass(String moduleMainClass) {
        this.moduleMainClass = moduleMainClass;
    }

    @Input
    @Optional
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @TaskAction
    public void compile() throws IOException {
        File source = getSourceFile().getAsFile().get();
        File target = getTargetFile().getAsFile().get();

        target.getParentFile().mkdirs();

        ModuleInfoCompiler compiler = new ModuleInfoCompiler(targetCompatibility, moduleVersion, moduleMainClass);

        try (Reader reader = new InputStreamReader(Files.newInputStream(source.toPath()), encoding);
             OutputStream output = Files.newOutputStream(target.toPath())) {
            compiler.compile(reader, output);
        }
    }
}
