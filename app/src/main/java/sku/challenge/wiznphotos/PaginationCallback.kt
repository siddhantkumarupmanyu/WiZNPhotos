package sku.challenge.wiznphotos

interface PaginationCallback {

    fun loadNext(count: Int)

    fun loadPrevious(count: Int)
}
