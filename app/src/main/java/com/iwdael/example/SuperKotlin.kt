package com.iwdael.example

import com.iwdael.annotations.FieldAnnotation
import com.iwdael.annotations.ParserAnnotation
import java.lang.Deprecated


@ParserAnnotation
@Deprecated
class SuperKotlin {
    @FieldAnnotation
    var name: Int = 132

}