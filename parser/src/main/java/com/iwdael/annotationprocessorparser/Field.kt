package com.iwdael.annotationprocessorparser

import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Field(variableElement: Element) : Parser {
    val element = variableElement as VariableElement
    val packet by lazy { Packet(element.enclosingElement.enclosingElement) }
    val parent by lazy { Class(element.enclosingElement) }
    val className by lazy { element.asType().toString() }
    val name by lazy { element.simpleName.toString() }
    val value by lazy { element.constantValue }
    val annotation by lazy { element.annotationMirrors.map { Annotation(it) } }
    val modifiers by lazy { element.modifiers }
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return element.getAnnotation(clazz)
    }

}
