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
class Class(element: Element) {
    private val e = element as TypeElement
    val `package` = Package(e.enclosingElement)
    val name = e.simpleName.toString()
    val fields = e.enclosedElements.filter { it.kind == ElementKind.FIELD }.map { Field(it) }
    val methods = e.enclosedElements.filter { it.kind == ElementKind.METHOD }.map { Method(it) }
    val superClass = e.superclass.toString()
    val interfaces = e.interfaces.map { it.toString() }
    val annotations = e.annotationMirrors.map { Annotation(it) }
    val modifiers = e.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return e.getAnnotation(clazz)
    }

    override fun toString(): String {

        return Format().formatJson("{" +
                "package:${`package`}," +
                "name:\"${`name`}\"," +
                "fields:${`fields`}," +
                "methods:${`methods`}," +
                "superClass:\"${`superClass`}\"," +
                "interfaces:${`interfaces`}," +
                "annotations:${`annotations`}" +
                "}")
    }


}