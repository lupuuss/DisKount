package com.github.lupuuss.diskount.paging

data class PageRequest<T>(
    val request: T,
    val pageNumber: Int = FIRST_PAGE,
    val pageSize: Int = DEFAULT_PAGE_SIZE,
) {

    companion object {
        const val FIRST_PAGE = 0
        const val DEFAULT_PAGE_SIZE = 10
    }
}