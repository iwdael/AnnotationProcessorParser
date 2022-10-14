package com.iwdael.processor

import com.iwdael.annotationprocessorparser.Class
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asAnnotation
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asField
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asFileBuilder
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asMethod
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asTypeBuilder
import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.Filer

class KtMaker(val it: Class) : Maker {
    override fun make(filer: Filer) {
        it.asFileBuilder("","EXE")
            .addType(
                it.asTypeBuilder("EXE")
                    .addAnnotations(it.annotations.map { it.asAnnotation() })
                    .addProperties(it.fields.map { it.asField() })
                    .addFunctions(it.methods.map { it.asMethod() })
                    .build()
            )
            .clearImports()
            .build()
            .writeTo(filer)

    }

}