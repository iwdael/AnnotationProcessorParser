/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 * project: https://github.com/iwdael/AnnotationProcessorParser
 */

package com.iwdael.annotationprocessorparser

import java.lang.Class
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

class Field(variableElement: Element) : Parser {
    val element = variableElement as VariableElement
    val packet by lazy { Packet(element.enclosingElement.enclosingElement) }
    val parent by lazy { Class(element.enclosingElement) }
    val className by lazy { element.asType().toString() }
    val name by lazy { element.simpleName.toString() }
    val value by lazy { element.constantValue }
    val annotation by lazy { element.annotationMirrors.map { Annotation(it) } }
    val modifiers by lazy { element.modifiers }
    val setter by lazy {
        setterOrNull
            ?: throw IllegalArgumentException("Can not found setter(${parent.className}.${name})")
    }
    val setterOrNull by lazy {
        parent.methods
            .filter { it.parameter.size == 1 }
            .firstOrNull {
                (it.name.replaceFirst("set", "").equals(name, true) ||
                        it.name.replaceFirst("set", "").equals(name.replaceFirst("is", ""), true) ||
                        it.name.equals(name, true)
                        ) &&
                        it.parameter[0].className == className
            }
    }

    val getter by lazy {
        getterOrNull
            ?: throw IllegalArgumentException("Can not found getter(${parent.className}.${name})")
    }
    val getterOrNull by lazy {
        parent.methods.firstOrNull {
            (it.name.replaceFirst("get", "").equals(name, true) ||
                    it.name.replaceFirst("get", "").equals(name.replaceFirst("is", ""), true) ||
                    it.name.equals(name, true)
                    ) &&
                    it.returnClassName == className
        }
    }

    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)

    fun <A : kotlin.Annotation> getAnnotation(clazz: Class<A>): A? {
        return element.getAnnotation(clazz)
    }

}
