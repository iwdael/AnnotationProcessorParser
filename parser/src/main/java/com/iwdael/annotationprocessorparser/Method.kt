package com.iwdael.annotationprocessorparser


import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Method(element: Element) {
    private val e = element as ExecutableElement
    val `package` = e.enclosingElement.enclosingElement.toString()
    val className = e.enclosingElement.simpleName.toString()
    val owner = "${`package`}.${className}"
    val name = e.simpleName.toString()
    val parameter = e.parameters.map { Parameter(it) }
    val `return` = e.returnType.toString()
    val modifiers = e.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return e.getAnnotation(clazz)
    }

    override fun toString(): String {

        return "{" +
                "package:\"${`package`}\"," +
                "className:\"${className}\"," +
                "owner:\"${owner}\"," +
                "name:\"${name}\"," +
                "parameter:${parameter}," +
                "return:${`return`}" +
                "}"
    }

}