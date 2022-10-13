package com.iwdael.processor

import com.google.auto.service.AutoService
import com.iwdael.annotation.ParserAnnotation
import com.iwdael.annotationprocessorparser.Class
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class Processor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes() =
        mutableSetOf(ParserAnnotation::class.java.canonicalName)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        env: RoundEnvironment
    ): Boolean {
        (env.getElementsAnnotatedWith(ParserAnnotation::class.java) ?: arrayListOf())
            .map { Class(it) }
            .forEach {

            }
        return false
    }
}