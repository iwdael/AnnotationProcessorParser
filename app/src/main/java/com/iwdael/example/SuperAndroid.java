package com.iwdael.example;

import com.iwdael.annotation.FieldAnnotation;
import com.iwdael.annotation.MethodAnnotation;
import com.iwdael.annotation.ParserAnnotation;

@ParserAnnotation
public class SuperAndroid {
    @FieldAnnotation
    String name;

    @MethodAnnotation
    public String getName(String str) {
        return name+str;
    }
}
