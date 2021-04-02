package com.github.nilsa.ideatestplugin

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile

class NilsFileType : LanguageFileType(NilsLanguage) {
    override fun getName() = NilsLanguage.displayName
    override fun getDescription() = "Nils files"
    override fun getIcon() = null
    override fun getDefaultExtension() = "nils"
}