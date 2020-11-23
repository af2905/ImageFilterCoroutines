package com.github.af2905.imagefiltercoroutines

import com.github.af2905.imagefiltercoroutines.filters.IFilter

class FilterItem(
    val previewUrl: String? = null,
    val imageUrl: String? = null,
    var filterName: String? = null,
    var filter: IFilter? = null
)