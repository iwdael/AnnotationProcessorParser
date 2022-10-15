/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 * project: https://github.com/iwdael/AnnotationProcessorParser
 */

package com.iwdael.annotationprocessorparser


import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier

class Method(executableElement: Element) :Parser{
    val element = executableElement as ExecutableElement
    val parent by lazy { Class(element.enclosingElement) }
    val name by lazy { element.simpleName.toString() }
    val parameter by lazy { element.parameters.map { Parameter(it) } }
    val annotation by lazy { element.annotationMirrors.map { Annotation(it) } }
    val returnClassName by lazy { element.returnType.toString() }
    val modifiers by lazy { element.modifiers }
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return element.getAnnotation(clazz)
    }

}
