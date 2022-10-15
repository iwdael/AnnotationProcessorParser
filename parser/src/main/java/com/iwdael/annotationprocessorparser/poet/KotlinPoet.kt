/**
 * author : iwdael
 * e-mail : iwdael@outlook.com
 * project: https://github.com/iwdael/AnnotationProcessorParser
 */

package com.iwdael.annotationprocessorparser.poet

import com.iwdael.annotationprocessorparser.*
import com.iwdael.annotationprocessorparser.Annotation
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.jvm.transient
import com.squareup.kotlinpoet.jvm.volatile
import javax.lang.model.element.Modifier


object KotlinPoet {

    private val javaMapKotlin = listOf(
        "java.lang.IllegalStateException" to "kotlin.IllegalStateException",
        "java.lang.NullPointerException" to "kotlin.NullPointerException",
        "java.lang.StringBuilder" to "kotlin.text.StringBuilder",
        "java.lang.Integer" to "kotlin.Int",
        "java.lang.AssertionError" to "kotlin.AssertionError",
        "java.lang.Iterable" to "kotlin.collections.Iterable",
        "java.lang.Exception" to "kotlin.Exception",
        "java.lang.Byte" to "kotlin.Byte",
        "java.lang.Short" to "kotlin.Short",
        "java.lang.Void" to "kotlin.Unit",
        "java.lang.CharSequence" to "kotlin.CharSequence",
        "java.lang.Deprecated" to "kotlin.Deprecated",
        "java.lang.IllegalArgumentException" to "kotlin.IllegalArgumentException",
        "java.lang.Comparable" to "kotlin.Comparable",
        "java.lang.Float" to "kotlin.Float",
        "java.lang.Long" to "kotlin.Long",
        "java.lang.RuntimeException" to "kotlin.RuntimeException",
        "java.lang.Throwable" to "kotlin.Throwable",
        "java.lang.ClassCastException" to "kotlin.ClassCastException",
        "java.lang.String" to "kotlin.String",
        "java.lang.Number" to "kotlin.Number",
        "java.lang.Cloneable" to "kotlin.Cloneable",
        "java.lang.Double" to "kotlin.Double",
        "java.lang.Enum" to "kotlin.Enum",
        "java.lang.NumberFormatException" to "kotlin.NumberFormatException",
        "java.lang.Boolean" to "kotlin.Boolean",
        "java.lang.Error" to "kotlin.Error",
        "java.lang.ArithmeticException" to "kotlin.ArithmeticException",
        "java.lang.UnsupportedOperationException" to "kotlin.UnsupportedOperationException",
        "java.lang.IndexOutOfBoundsException" to "kotlin.IndexOutOfBoundsException"
    )

    fun Class.asFileBuilder(packageExt: String = "", fileNameExt: String = ""): FileSpec.Builder {
        return FileSpec.builder(packet.name + packageExt, classSimpleName + fileNameExt)
    }

    fun Set<Modifier>.asKotlin(): Array<KModifier> {
        return this.filter {
            it != Modifier.TRANSIENT
                    && it != Modifier.VOLATILE
                    && it != Modifier.SYNCHRONIZED
                    && it != Modifier.NATIVE
                    && it != Modifier.STRICTFP
                    && it != Modifier.PUBLIC
        }.map {
            return@map if (it == Modifier.PROTECTED) {
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

    fun TypeName.asKotlin(): TypeName {
        val pair = javaMapKotlin.filter { this.toString() == it.first }.firstOrNull() ?: return this
        return ClassName.bestGuess(pair.second)
    }

    fun String.asTypeName(): TypeName {
        val kotlinClass = when {
            this == "java.lang.String" -> "kotlin.String"
            this == "java.lang.String[]" -> "kotlin.Array<String>"
            this == "int" -> "kotlin.Int"
            this == "int[]" -> "kotlin.IntArray"
            this == "boolean" -> "kotlin.Boolean"
            this == "boolean[]" -> "kotlin.BooleanArray"
            this == "float" -> "kotlin.Float"
            this == "float[]" -> "kotlin.FloatArray"
            this == "long" -> "kotlin.Long"
            this == "long[]" -> "kotlin.LongArray"
            this == "double" -> "kotlin.Double"
            this == "double[]" -> "kotlin.DoubleArray"
            this == "short" -> "kotlin.Short"
            this == "short[]" -> "kotlin.ShortArray"
            this == "byte" -> "kotlin.Byte"
            this == "byte[]" -> "kotlin.ByteArray"
            this == "char" -> "kotlin.Char"
            this == "char[]" -> "kotlin.CharArray"
            this == "java.util.List" -> "kotlin.collections.List"
            this == "java.lang.Integer" -> "kotlin.Int"
            else -> this
        }
        return ClassName.bestGuess(kotlinClass).asKotlin()
    }

    /**
     * Annotation
     */

    fun List<Annotation>.asKotlin(): List<Annotation> {
        return this
            .filter { it.className != Metadata::class.java.name }
            .filter { !it.className.endsWith("NotNull") }
            .filter { !it.className.endsWith("Nullable") }
    }

    fun Annotation.asTypeName(): TypeName {
        return element.annotationType.asTypeName().asKotlin()
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
        parser().member.forEach { addMember("${it.name} = %L", "${it.value}") }
        return this
    }

    fun AnnotationSpec.Builder.parser(): Annotation {
        return mapPoet[this.hashCode()] as Annotation
    }

    /**
     * Class
     */

    fun Class.asTypeName(): TypeName {
        return element.asClassName().asKotlin()
    }

    fun Class.asTypeBuilder(ext: String = ""): TypeSpec.Builder {
        val parser = this
        return TypeSpec.classBuilder(classSimpleName + ext)
            .apply { mapPoet[this.hashCode()] = parser }
    }

    fun TypeSpec.Builder.stickAnnotation(): TypeSpec.Builder {
        addAnnotations(parser().annotations.asKotlin().map { it.asAnnotation() })
        return this
    }

    fun TypeSpec.Builder.stickModifier(): TypeSpec.Builder {
        addModifiers(*parser().modifiers.asKotlin())
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
        return element.asType().asTypeName().asKotlin()
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
        addModifiers(*parser().modifiers.asKotlin())
        return this
    }

    fun PropertySpec.Builder.stickAnnotation(): PropertySpec.Builder {
        addAnnotations(parser().annotation.asKotlin().map { it.asAnnotation() })
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
        addAnnotations(parser().annotation.asKotlin().map { it.asAnnotation() })
        return this
    }

    fun FunSpec.Builder.stickModifier(): FunSpec.Builder {
        addModifiers(*parser().modifiers.asKotlin())
        return this
    }

    fun FunSpec.Builder.stickReturn(): FunSpec.Builder {
        returns(parser().element.returnType.asTypeName().asKotlin())
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
        return element.asType().asTypeName().asKotlin()
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
        addModifiers(* parser().modifiers.asKotlin())
        return this
    }

    fun ParameterSpec.Builder.parser(): Parameter {
        return mapPoet[this.hashCode()]!! as Parameter
    }

}