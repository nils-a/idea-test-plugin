package com.github.nilsa.ideatestplugin

import com.intellij.ide.scratch.ScratchFileCreationHelper
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project

class NilsScratchFileCreationHelper : ScratchFileCreationHelper() {
    override fun prepareText(project: Project, context: Context, dataContext: DataContext): Boolean {
        context.fileExtension = "nils"
        context.language = NilsLanguage
        return super.prepareText(project, context, dataContext)
    }
}