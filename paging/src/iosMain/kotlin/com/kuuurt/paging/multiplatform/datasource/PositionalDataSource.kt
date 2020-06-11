package com.kuuurt.paging.multiplatform.datasource

import com.kuuurt.paging.multiplatform.paginator.PaginatorState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
@Deprecated(
    message = "Deprecated in Paging 3.0.0-alpha01",
    replaceWith = ReplaceWith(
        "Pager<K, V>",
        "com.kuuurt.paging.multiplatform.paginator"
    )
)
actual class PositionalDataSource<T: Any> actual constructor(
    private val clientScope: CoroutineScope,
    private val getCount: suspend () -> Int,
    private val getBlock: suspend (Int, Int) -> List<T>
) : DataSource<T> {
    private val _getState = ConflatedBroadcastChannel<PaginatorState>()
    override val getState = _getState.asFlow()

    private val _totalCount = ConflatedBroadcastChannel(0)
    override val totalCount = _totalCount.asFlow()

    private val _itemsList = mutableListOf<T>()
    private val _items = ConflatedBroadcastChannel<List<T>>(listOf())
    val items = _items.asFlow()

    private var isLoading = false
    var pageSize = 10

    internal actual class Factory<T: Any> actual constructor(
        clientScope: CoroutineScope,
        getCount: suspend () -> Int,
        getBlock: suspend (Int, Int) -> List<T>
    ) {
        actual val dataSource: Flow<PositionalDataSource<T>> = flowOf(
            PositionalDataSource(
                clientScope,
                getCount,
                getBlock
            )
        )
    }

    fun loadMore() {
        if (!isLoading) {
            clientScope.launch(CoroutineExceptionHandler { _, exception ->
                _getState.offer(PaginatorState.Error(exception))
            }) {
                isLoading = true
                val items = getBlock(_itemsList.size, pageSize)
                _itemsList.addAll(items)
                _items.offer(_itemsList)
                _totalCount.offer(getCount())
                _getState.offer(PaginatorState.Complete)
                isLoading = false
            }
        }
    }

    fun loadInitial() {
        if (!isLoading) {
            clientScope.launch(CoroutineExceptionHandler { _, exception ->
                _getState.offer(PaginatorState.Error(exception))
            }) {
                isLoading = true
                _getState.offer(PaginatorState.Loading)
                val items = getBlock(0, pageSize)
                _totalCount.offer(getCount())
                _itemsList.addAll(items)
                _items.offer(_itemsList)
                _getState.offer(PaginatorState.Complete)
                if (items.isEmpty()) {
                    _getState.offer(PaginatorState.Empty)
                }
                isLoading = false
            }
        }
    }

    override fun refresh() {
        _itemsList.clear()
        _items.offer(_itemsList)
        loadInitial()
    }
}