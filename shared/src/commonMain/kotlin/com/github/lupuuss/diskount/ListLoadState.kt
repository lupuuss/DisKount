package com.github.lupuuss.diskount

interface ListLoadState<out Request, out Data> {
    val lastRequest: Request?
    val data: List<Data>
    val isLoading: Boolean
    val error: Throwable?
    val hasMore: Boolean
}

interface MutableListLoadState<Request, Data> : ListLoadState<Request, Data> {
    override var lastRequest: Request?
    override val data: MutableList<Data>
    override var isLoading: Boolean
    override var error: Throwable?
    override var hasMore: Boolean
}

fun <Request, Data> listLoadState(
    lastRequest: Request? = null,
    data: List<Data> = emptyList(),
    isLoading: Boolean = false,
    error: Throwable? = null,
    hasMore: Boolean = true,
): ListLoadState<Request, Data> = ListLoadStateImpl(lastRequest, data.toMutableList(), isLoading, error, hasMore)

fun <Request, Data> ListLoadState<Request, Data>.toMutable(): MutableListLoadState<Request, Data> {
    return ListLoadStateImpl(
        lastRequest,
        data.toMutableList(),
        isLoading,
        error,
        hasMore,
    )
}

fun <Request, Data> ListLoadState<Request, Data>.mutate(
    mutation: MutableListLoadState<Request, Data>.() -> Unit,
): ListLoadState<Request, Data> = toMutable().apply(mutation)

private data class ListLoadStateImpl<Request, Data>(
    override var lastRequest: Request? = null,
    override val data: MutableList<Data> = mutableListOf(),
    override var isLoading: Boolean = false,
    override var error: Throwable? = null,
    override var hasMore: Boolean = true,
) : MutableListLoadState<Request, Data>