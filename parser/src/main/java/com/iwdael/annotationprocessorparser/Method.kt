package com.iwdael.annotationprocessorparser


import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Method(executableElement: Element) {
    val element = executableElement as ExecutableElement
    val `package` = element.enclosingElement.enclosingElement.toString()
    val ownerClassSimpleName = element.enclosingElement.simpleName.toString()
    val ownerClassName = "${`package`}.${ownerClassSimpleName}"
    val name = element.simpleName.toString()
    val parameter = element.parameters.map { Parameter(it) }
    val returnClassName = element.returnType.toString()
    val modifiers = element.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return element.getAnnotation(clazz)
    }

}
