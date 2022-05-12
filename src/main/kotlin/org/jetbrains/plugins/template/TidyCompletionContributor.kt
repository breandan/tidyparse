package org.jetbrains.plugins.template

import ai.hypergraph.kaliningraph.sat.synthesizeFromFPSolving
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PlainTextTokenTypes
import com.intellij.util.ProcessingContext

class TidyCompletionContributor: CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PlainTextTokenTypes.PLAIN_TEXT),
            TidyCompletionProvider()
        )
    }
}

class TidyCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val grammarFile = parameters.originalFile.getGrammarFile() ?: return
        cfg = recomputeGrammar(grammarFile)
        var currentLine = parameters.editor.currentLine()
        currentLine = if("_" in currentLine) currentLine else {
            val colIdx = parameters.editor.caretModel.currentCaret.visualPosition.column
            currentLine.substring(0, colIdx) + "_" + currentLine.substring(colIdx, currentLine.length)
        }
        println("Received completion event: $currentLine")
        try {
            currentLine.synthesizeFromFPSolving(cfg, " ").take(5).toList().shuffled()
                .forEach { result.addElement(LookupElementBuilder.create(it)) }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}