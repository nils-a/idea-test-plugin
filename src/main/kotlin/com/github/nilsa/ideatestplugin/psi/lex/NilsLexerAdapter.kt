package com.github.nilsa.ideatestplugin.psi

import com.intellij.lexer.FlexAdapter


class NilsLexerAdapter : FlexAdapter(com.github.nilsa.ideatestplugin.psi.lex.NilsLexer(null))