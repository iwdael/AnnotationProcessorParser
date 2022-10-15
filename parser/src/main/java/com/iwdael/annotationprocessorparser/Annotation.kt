/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 * project: https://github.com/iwdael/AnnotationProcessorParser
 */

package com.iwdael.annotationprocessorparser


import javax.lang.model.element.AnnotationMirror

class Annotation(mirror: AnnotationMirror):Parser {
    val element = mirror
    val className by lazy { mirror.annotationType.toString() }
    val member by lazy { mirror.elementValues.entries.map { Member (it.key.simpleName.toString() , it.value)  } }
    class Member(val name: String, val value: Any)
}
