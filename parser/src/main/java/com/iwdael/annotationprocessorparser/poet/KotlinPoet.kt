package com.iwdael.annotationprocessorparser.poet

import com.iwdael.annotationprocessorparser.*
import com.iwdael.annotationprocessorparser.Annotation
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.jvm.transient
import com.squareup.kotlinpoet.jvm.volatile
import javax.lang.model.element.Modifier


object KotlinPoet {

    fun Class.asFileBuilder(packageExt: String = "", fileNameExt: String = ""): FileSpec.Builder {
        return FileSpec.builder(packet.name + packageExt, classSimpleName + fileNameExt)
    }

    fun Set<Modifier>.asKModifier(): Array<KModifier> {
        return this.filter {
            it != Modifier.TRANSIENT
                    && it != Modifier.VOLATILE
                    && it != Modifier.SYNCHRONIZED
                    && it != Modifier.NATIVE
                    && it != Modifier.STRICTFP
        }.map {
            return@map if (it == Modifier.PUBLIC) {
                KModifier.PUBLIC
            } else if (it == Modifier.PROTECTED) {
                KModifier.PROTECTED
            } else if (it == Modifier.PRIVATE) {
                KModifier.PRIVATE
            } else if (it == Modifier.ABSTRACT) {
                KModifier.ABSTRACT
            } else if (it == Modifier.STATIC) {
//                KModifier.CONST
                KModifier.FINAL
            } else {
                KModifier.FINAL
            }
        }.toTypedArray()
    }

    fun TypeName.asClassName(): ClassName {
        return this as ClassName
    }

    /**
     * Annotation
     */

    fun Annotation.asTypeName(): TypeName {
        return element.annotationType.asTypeName()
    }

    fun Annotation.asAnnotation(): AnnotationSpec {
        return AnnotationSpec.get(element)
    }

    fun Annotation.asAnnotationBuilder(): AnnotationSpec.Builder {
        val parser = this
        return AnnotationSpec.builder(asTypeName().asClassName())
            .apply { mapPoet[this.hashCode()] = parser }
    }

    fun AnnotationSpec.Builder.stickMembers(): AnnotationSpec.Builder {
        parser().member.forEach { addMember("${it.name} = %S", "${it.value}") }
        return this
    }

    fun AnnotationSpec.Builder.parser(): Annotation {
        return mapPoet[this.hashCode()] as Annotation
    }

    /**
     * Class
     */

    fun Class.asTypeName(): TypeName {
        return element.asClassName()
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
        addModifiers(*parser().modifiers.asKModifier())
        return this
    }


    fun TypeSpec.Builder.parser(): Class {
        return mapPoet[this.hashCode()] as Class
    }

    /**
     * Field
     */

    fun Field.asTypeName(): TypeName {
        val parser = this
        return element.asType().asTypeName()
            .copy(!(annotation.any { it.className.endsWith("NotNull") }
                    || !parser.className.contains(".")))
    }

    fun Field.asField(): PropertySpec {
        return asFieldBuilder()
            .stickModifier()
            .stickAnnotation()
            .stickValue()
            .build()
    }

    fun Field.asFieldBuilder(): PropertySpec.Builder {
        val parser = this
        return PropertySpec.builder(name, asTypeName())
            .mutable(!parser.modifiers.contains(Modifier.FINAL))
            .apply { if (parser.modifiers.any { it == Modifier.TRANSIENT }) transient() }
            .apply { if (parser.modifiers.any { it == Modifier.VOLATILE }) volatile() }
            .apply { mapPoet[this.hashCode()] = parser }
    }

    fun PropertySpec.Builder.stickModifier(): PropertySpec.Builder {
        addModifiers(*parser().modifiers.asKModifier())
        return this
    }

    fun PropertySpec.Builder.stickAnnotation(): PropertySpec.Builder {
        addAnnotations(parser().annotation.map { it.asAnnotation() })
        return this
    }

    fun PropertySpec.Builder.stickValue(): PropertySpec.Builder {
        initializer("%L", parser().value)
        return this
    }


    fun PropertySpec.Builder.parser(): Field {
        return mapPoet[this.hashCode()]!! as Field
    }

    /**
     * Method
     */
    fun Method.asMethod(ext: String = ""): FunSpec {
        return asMethodBuilder(ext)
            .stickParameter()
            .stickAnnotation()
            .stickModifier()
            .stickReturn()
            .build()
    }

    fun Method.asMethodBuilder(ext: String = ""): FunSpec.Builder {
        val parser = this
        return FunSpec.builder(name + ext)
            .apply {
                mapPoet[this.hashCode()] = parser
            }
    }

    fun FunSpec.Builder.stickParameter(): FunSpec.Builder {
        addParameters(parser().parameter.map { it.asParameter() })
        return this
    }

    fun FunSpec.Builder.stickAnnotation(): FunSpec.Builder {
        addAnnotations(parser().annotation.map { it.asAnnotation() })
        return this
    }

    fun FunSpec.Builder.stickModifier(): FunSpec.Builder {
        addModifiers(*parser().modifiers.asKModifier())
        return this
    }

    fun FunSpec.Builder.stickReturn(): FunSpec.Builder {
        returns(parser().element.returnType.asTypeName())
        return this
    }

    fun FunSpec.Builder.parser(): Method {
        return mapPoet[this.hashCode()]!! as Method
    }


    /**
     * Parameter
     */

    fun Parameter.asTypeName(): TypeName {
        val parser = this
        return element.asType().asTypeName()
            .copy(!(annotation.any { it.className.endsWith("NotNull") }
                    || !parser.className.contains(".")))
    }

    fun Parameter.asParameter(): ParameterSpec {
        return asParameterBuilder().stickModifier().build()
    }

    fun Parameter.asParameterBuilder(): ParameterSpec.Builder {
        val parser = this
        return ParameterSpec.builder(name, asTypeName())
            .apply { mapPoet[this.hashCode()] = parser }
    }

    fun ParameterSpec.Builder.stickModifier(): ParameterSpec.Builder {
        addModifiers(* parser().modifiers.asKModifier())
        return this
    }

    fun ParameterSpec.Builder.parser(): Parameter {
        return mapPoet[this.hashCode()]!! as Parameter
    }

}