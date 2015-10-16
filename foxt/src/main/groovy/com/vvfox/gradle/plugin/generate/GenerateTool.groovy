package com.vvfox.gradle.plugin.generate

import com.vvfox.gradle.plugin.CodeGeneratePlugin;

/**
 * Created by fox on 2015/10/16.
 */
class GenerateTool {

    def run(Closure closure) {
        CodeGeneratePlugin.LOGGER.trace("$CodeGeneratePlugin.EX_NAME begin generate class $closure.msg")
    }
}
