package com.iwdael.example;

import com.iwdael.annotations.FieldAnnotation;
import com.iwdael.annotations.MethodAnnotation;
import com.iwdael.annotations.ParserAnnotation;

@ParserAnnotation
@Deprecated
public class SuperJava {


    public static final int an = 916;
    @FieldAnnotation(v1 = 10, v2 = 100)
    String name;

    @MethodAnnotation(v1 = {1, 2, 3, 5, 0}, v2 = "android")
    @Deprecated
    public String getName(String str) {
        return name + str;
    }

    public void getNS(int a, int b) {
    }
}
