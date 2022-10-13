package com.iwdael.annotationprocessorparser


import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Class(typeElement: Element) {
    val element = typeElement as TypeElement
    val `package` by lazy { Package(element.enclosingElement) }
    val className by lazy { element.qualifiedName.toString() }
    val classSimpleName by lazy { element.simpleName.toString() }
    val fields by lazy {
        element.enclosedElements.filter { it.kind == ElementKind.FIELD }.map { Field(it) }
    }
    val methods by lazy {
        element.enclosedElements.filter { it.kind == ElementKind.METHOD }.map { Method(it) }
    }
    val superClass by lazy { element.superclass }
    val interfaces by lazy { element.interfaces }
    val annotations by lazy { element.annotationMirrors.map { Annotation(it) } }
    val modifiers by lazy { element.modifiers }
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return element.getAnnotation(clazz)
    }
}
