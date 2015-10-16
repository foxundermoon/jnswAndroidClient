package com.vvfox.gradle.plugin.task

import com.vvfox.gradle.plugin.generate.GenerateTool
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;

/**
 * Created by fox on 2015/10/16.
 */
public class GenerateTask  extends DefaultTask{
    TaskAction generate(){
        new GenerateTool().run {msg "begin"}
    }
}
