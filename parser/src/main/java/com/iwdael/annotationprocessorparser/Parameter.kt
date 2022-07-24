package com.iwdael.annotationprocessorparser

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Parameter(element: Element) {
    val e = element as  VariableElement
    val `package` = e.enclosingElement.enclosingElement.enclosingElement.asType().toString()
    val className = e.enclosingElement.enclosingElement.simpleName.toString()
    val methodName = e.enclosingElement.simpleName.toString()
    val owner = "${`package`}.${className}.${methodName}"
    val type = e.asType().toString()
    val name = e.simpleName.toString()
    val modifiers = e.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    override fun toString(): String {
        return "{" +
                "package:\"${`package`}\"," +
                "className:\"${`className`}\"," +
                "methodName:\"${`methodName`}\"," +
                "owner:\"${owner}\"," +
                "type:\"${type}\"," +
                "name:\"${name}\"" +
                "}"
    }
}
