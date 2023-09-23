package id.gdev.regist.domain.model

import id.gdev.regist.utils.FilterSort

data class FilterField(
    val field: String,
    val filterSort: FilterSort
)
