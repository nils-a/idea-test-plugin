package com.github.nilsa.ideatestplugin.psi.parser

import com.github.nilsa.ideatestplugin.NilsLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls


class NilsTokenType(@NonNls debugName: String) : IElementType(debugName, NilsLanguage) {
    override fun toString(): String {
        return "NilsTokenType." + super.toString()
    }
}

