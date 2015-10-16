package com.vvfox.gradle.plugin

import org.gradle.api.Project

public class CodeGeneratePluginExtension{
    static final String NAME = "ormGen"
    CodeGeneratePlugin plugin;
    Project project

    CodeGeneratePluginExtension(CodeGeneratePlugin plugin,Project project) {
        this.plugin = plugin
        this.project = project
    }

}