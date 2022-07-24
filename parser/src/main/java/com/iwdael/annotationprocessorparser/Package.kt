package com.iwdael.annotationprocessorparser

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Package(element: Element) {

    val e = element as PackageElement
    val name = e.toString()
    val annotation = e.annotationMirrors.map { Annotation(it) }
    val modifiers = e.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    override fun toString(): String {
        return "{" +
                "name:\"${name}\"," +
                "annotation:${annotation}" +
                "}"
    }


}
