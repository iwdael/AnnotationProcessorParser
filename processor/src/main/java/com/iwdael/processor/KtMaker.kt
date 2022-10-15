package com.iwdael.processor

import com.iwdael.annotationprocessorparser.Class
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asField
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asFileBuilder
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asMethod
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.asTypeBuilder
import com.iwdael.annotationprocessorparser.poet.KotlinPoet.stickModifier
import javax.annotation.processing.Filer

class KtMaker(val it: Class) : Maker {
    override fun make(filer: Filer) {
        System.err.println(it.fields.map { it to (it.setter to it.getter) }
            .joinToString(separator = ",", transform = { "${it.first.name} setter:${it.second.first?.name} getter:${it.second.second?.name}" })
        )
        it.asFileBuilder("", "EXE")
            .addType(
                it.asTypeBuilder("EXE")
                    .stickModifier()
                    .addProperties(it.fields.map { it.asField() })
                    .addFunctions(it.methods.map { it.asMethod() })
                    .build()
            )
            .build()
            .writeTo(filer)

    }

}