package com.iwdael.annotationprocessorparser

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Package(element: Element) {

    private val e = element as PackageElement
    val name = e.toString()
    val annotation = e.annotationMirrors.map { Annotation(it) }
    override fun toString(): String {
        return "{" +
                "name:\"${name}\"," +
                "annotation:${annotation}" +
                "}"
    }


}