package com.github.nilsa.ideatestplugin.psi.lex

import com.intellij.lexer.FlexAdapter

class NilsLexerAdapter : FlexAdapter(NilsLexer(null))