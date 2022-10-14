package com.iwdael.annotationprocessorparser

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Parameter(variableElement: Element) : Parser {
    val element = variableElement as VariableElement
    val packet by lazy { Packet(element.enclosingElement.enclosingElement.enclosingElement) }
    val parentClass by lazy { Class(element.enclosingElement.enclosingElement) }
    val parentMethod by lazy { Method(element.enclosingElement) }
    val annotation by lazy { element.annotationMirrors.map { Annotation(it) } }
    val className by lazy { element.asType().toString() }
    val name by lazy { element.simpleName.toString() }
    val modifiers by lazy { element.modifiers }
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
}
