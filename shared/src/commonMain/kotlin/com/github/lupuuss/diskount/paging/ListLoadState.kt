package com.github.lupuuss.diskount.paging

abstract class ListLoadState<out Request, out Data> {
    abstract val lastRequest: Request?
    abstract val data: List<Data>
    abstract val isLoading: Boolean
    abstract val error: Throwable?
    abstract val hasMore: Boolean
}

abstract class MutableListLoadState<Request, Data> : ListLoadState<Request, Data>() {
    abstract override var lastRequest: Request?
    abstract override val data: MutableList<Data>
    abstract override var isLoading: Boolean
    abstract override var error: Throwable?
    abstract override var hasMore: Boolean
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
) : MutableListLoadState<Request, Data>()