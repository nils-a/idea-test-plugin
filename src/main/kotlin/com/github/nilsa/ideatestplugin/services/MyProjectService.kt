package com.github.nilsa.ideatestplugin.services

import com.github.nilsa.ideatestplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
