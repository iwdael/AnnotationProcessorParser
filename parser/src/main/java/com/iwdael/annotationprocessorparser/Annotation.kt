package com.iwdael.annotationprocessorparser


import javax.lang.model.element.AnnotationMirror

/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 */
class Annotation(mirror: AnnotationMirror) {
    val name = mirror.annotationType.toString()
    val values = mirror.elementValues.entries.map { it.key.simpleName to it.value }
//    override fun toString(): String {
//        return "{" +
//                "name:\"${name}\",\n" +
//                "values:\"${values}\"" +
//                "}"
//    }

    override fun toString(): String {
        return "{name:\"${name}\"}"
    }
}