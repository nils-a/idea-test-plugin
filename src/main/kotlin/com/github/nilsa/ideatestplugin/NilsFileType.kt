package com.github.nilsa.ideatestplugin

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon


open class NilsFileType protected constructor() : LanguageFileType(NilsLanguage) {
    override fun getName() = NilsLanguage.displayName
    override fun getDescription() = "Nils files"
    override fun getIcon(): Icon? = AllIcons.Actions.Colors
    override fun getDefaultExtension() = "nils"

    companion object {
        @JvmField
        val INSTANCE = NilsFileType()
    }
}
