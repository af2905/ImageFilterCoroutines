package com.github.af2905.imagefiltercoroutines.filters

import android.graphics.Bitmap

interface IFilter {
    fun apply(src: Bitmap): Bitmap?
}