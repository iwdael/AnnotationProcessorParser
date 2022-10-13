package com.iwdael.annotationprocessorparser

import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Field(variableElement: Element) {
    val element = variableElement as VariableElement
    val `package` = element.enclosingElement.enclosingElement.toString()
    val ownerClassSimpleName = element.enclosingElement.simpleName.toString()
    val ownerClassName = "${`package`}.${ownerClassSimpleName}"
    val className = element.asType().toString()
    val name = element.simpleName.toString()
    val value = element.constantValue
    val annotation = element.annotationMirrors.map { Annotation(it) }
    val modifiers = element.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return element.getAnnotation(clazz)
    }

}
