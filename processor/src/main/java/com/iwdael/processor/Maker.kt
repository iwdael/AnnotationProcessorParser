package com.iwdael.processor

import javax.annotation.processing.Filer

interface Maker {
    fun make(filer: Filer)
}