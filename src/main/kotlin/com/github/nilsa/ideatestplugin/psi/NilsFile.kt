package com.github.nilsa.ideatestplugin.psi

import com.github.nilsa.ideatestplugin.NilsFileType
import com.github.nilsa.ideatestplugin.NilsLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class NilsFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, NilsLanguage) {
    override fun getFileType(): FileType {
        return NilsFileType.INSTANCE
    }

    override fun toString(): String {
        return "Nils File"
    }
}