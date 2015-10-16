package com.vvfox.gradle.plugin

import com.vvfox.gradle.plugin.task.GenerateTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by fox on 2015/10/16.
 */

public class CodeGeneratePlugin implements Plugin<Project> {
    static final String EX_NAME = CodeGeneratePluginExtension.NAME;
     static final Logger LOGGER = LoggerFactory.getLogger(CodeGeneratePlugin)
    private Project project

    @Override
    void apply(Project project) {
        this.project = project
        LOGGER.info("${EX_NAME} Applying CodeGeneratePlugin")
        if (project.plugins.hasPlugin(CodeGeneratePlugin))
            return
        project.extensions.create(CodeGeneratePluginExtension.NAME, CodeGeneratePluginExtension, this, project)
        LOGGER.debug("Registered extension '$EX_NAME'")
        def jnswJcenter = project.repositories.jcenter {
            url 'http://jarlib.vvfox.com/jcenter/'
        }
        def publicMaven = project.repositories.maven {
            url 'http://jarlib.vvfox.com/public/'
        }
        def repos = [jnswJcenter,publicMaven]
        def repoHandles = [  project.buildscript.repositories, project.buildscript.repositories]
        repoHandles.each {handle ->
            repos.each {repo ->handle.add(repo)}
        }
        LOGGER.debug("Added repository 'JCenter {url '....'}' and maven {url '...'} to project and buildScript")

        GenerateTask generateTask = project.tasks.create("generateOrmlite",GenerateTask)
        generateTask.description = "通过数据库生成ormlite 数据代码"
        generateTask.group = "generate"

    }
}