package com.iwdael.annotationprocessorparser

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Packet(packageElement: Element) :Parser{
    val element = packageElement as PackageElement
    val name = element.toString()
    val annotations = element.annotationMirrors.map { Annotation(it) }
    val modifiers = element.modifiers
    fun isModifier(modifier: Modifier) = modifiers.contains(modifier)
    val parent =
        name.substring(0, if (name.lastIndexOf(".") == -1) name.length else name.lastIndexOf("."))
}
