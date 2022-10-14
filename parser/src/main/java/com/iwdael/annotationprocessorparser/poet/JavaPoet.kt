package com.iwdael.annotationprocessorparser.poet

import com.iwdael.annotationprocessorparser.*
import com.iwdael.annotationprocessorparser.Annotation
import com.squareup.javapoet.*

object JavaPoet {

    fun String.asTypeName(): TypeName {
        return when (this) {
            "void" -> TypeName.VOID
            "char" -> TypeName.CHAR
            "short" -> TypeName.SHORT
            "int" -> TypeName.INT
            "long" -> TypeName.LONG
            "double" -> TypeName.DOUBLE
            "float" -> TypeName.FLOAT
            "byte" -> TypeName.BYTE
            "boolean" -> TypeName.BOOLEAN
            else -> ClassName.bestGuess(this)
        }
    }

    fun TypeName.asClassName(): ClassName {
        return this as ClassName
    }


    /**
     * Annotation
     */
    fun Annotation.asTypeName(): TypeName {
        return className.asTypeName()
    }

    fun Annotation.asAnnotation(): AnnotationSpec {
        return AnnotationSpec.get(this.element)
    }

    fun Annotation.asAnnotationBuilder(): AnnotationSpec.Builder {
        val parser = this
        return AnnotationSpec.builder(this.className.asTypeName().asClassName())
            .apply {
                mapPoet[this.hashCode()] = parser
            }
    }

    fun AnnotationSpec.Builder.stickMembers(): AnnotationSpec.Builder {
        parser().member.forEach { addMember(it.name, "${it.value}") }
        return this
    }

    fun AnnotationSpec.Builder.parser(): Annotation {
        return mapPoet[this.hashCode()]!! as Annotation
    }


    /**
     * Class
     */
    fun Class.asTypeName(): TypeName {
        return className.asTypeName()
    }

    fun Class.asTypeBuilder(ext: String = ""): TypeSpec.Builder {
        val parser = this
        return TypeSpec.classBuilder(classSimpleName + ext)
            .apply { mapPoet[this.hashCode()] = parser }
    }

    fun TypeSpec.Builder.stickAnnotation(): TypeSpec.Builder {
        addAnnotations(parser().annotations.map { it.asAnnotation() })
        return this
    }

    fun TypeSpec.Builder.stickModifier(): TypeSpec.Builder {
        addModifiers(* parser().modifiers.toTypedArray())
        return this
    }

    fun TypeSpec.Builder.parser(): Class {
        return mapPoet[this.hashCode()]!! as Class
    }

    /**
     * Field
     */
    fun Field.asTypeName(): TypeName {
        return className.asTypeName()
    }

    fun Field.asField(): FieldSpec {
        return asFieldBuilder()
            .stickModifier()
            .stickAnnotation()
            .stickValue()
            .build()
    }

    fun Field.asFieldBuilder(): FieldSpec.Builder {
        val parser = this
        return FieldSpec.builder(className.asTypeName(), name)
            .apply {
                mapPoet[this.hashCode()] = parser
            }
    }

    fun FieldSpec.Builder.stickModifier(): FieldSpec.Builder {
        return addModifiers(*parser().modifiers.toTypedArray())
    }

    fun FieldSpec.Builder.stickAnnotation(): FieldSpec.Builder {
        return addAnnotations(parser().annotation.map { it.asAnnotation() })
    }

    fun FieldSpec.Builder.stickValue(): FieldSpec.Builder {
        initializer("\$L",parser().value)
        return this
    }


    fun FieldSpec.Builder.parser(): Field {
        return mapPoet[this.hashCode()]!! as Field
    }

    /**
     *Method
     */
    fun Method.asMethod(ext: String = ""): MethodSpec {
        return asMethodBuilder(ext)
            .stickParameter()
            .stickAnnotation()
            .stickModifier()
            .stickReturn()
            .build()
    }

    fun Method.asMethodBuilder(ext: String = ""): MethodSpec.Builder {
        val parser = this
        return MethodSpec.methodBuilder(name + ext)
            .apply {
                mapPoet[this.hashCode()] = parser
            }
    }

    fun MethodSpec.Builder.stickParameter(): MethodSpec.Builder {
        addParameters(parser().parameter.map { ParameterSpec.get(it.element) })
        return this
    }

    fun MethodSpec.Builder.stickAnnotation(): MethodSpec.Builder {
        addAnnotations(parser().annotation.map { it.asAnnotation() })
        return this
    }

    fun MethodSpec.Builder.stickModifier(): MethodSpec.Builder {
        addModifiers(*parser().modifiers.toTypedArray())
        return this
    }

    fun MethodSpec.Builder.stickReturn(): MethodSpec.Builder {
        returns(parser().returnClassName.asTypeName())
        return this
    }

    fun MethodSpec.Builder.parser(): Method {
        return mapPoet[this.hashCode()]!! as Method
    }

    /**
     * Parameter
     */

    fun Parameter.asTypeName(): TypeName {
        return className.asTypeName()
    }

    fun Parameter.asParameter(): ParameterSpec {
        return ParameterSpec.get(element)
    }

    fun Parameter.asParameterBuilder(): ParameterSpec.Builder {
        val parser = this
        return ParameterSpec.builder(asTypeName(), name)
            .apply { mapPoet[this.hashCode()] = parser }
    }

    fun ParameterSpec.Builder.stickModifier(): ParameterSpec.Builder {
        addModifiers(* parser().modifiers.toTypedArray())
        return this
    }

    fun ParameterSpec.Builder.parser(): Parameter {
        return mapPoet[this.hashCode()]!! as Parameter
    }


}