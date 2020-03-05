package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Copyright 2020, Kurt Renzo Acosta, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
actual class DataSource<T> actual constructor(
    private val clientScope: CoroutineScope,
    private val getCount: suspend () -> Int,
    private val getBlock: suspend (Int, Int) -> List<T>
) {
    private val _getState = ConflatedBroadcastChannel<PaginatorState>()
    actual val getState = _getState.asFlow()

    private val _totalCount = ConflatedBroadcastChannel(0)
    actual val totalCount = _totalCount.asFlow()

    private val _itemsList = mutableListOf<T>()
    private val _items = ConflatedBroadcastChannel<List<T>>(listOf())
    val items = _items.asFlow()


    actual class Factory<T> actual constructor(
        clientScope: CoroutineScope,
        getCount: suspend () -> Int,
        getBlock: suspend (Int, Int) -> List<T>
    ) {
        actual val dataSource: Flow<DataSource<T>> = flowOf(
            DataSource(clientScope, getCount, getBlock)
        )
    }

    fun loadMore(size: Int = 10) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            val items = getBlock(_itemsList.size, size)
            _itemsList.addAll(items)
            _items.offer(_itemsList)
            _getState.offer(PaginatorState.Complete)
        }
    }

    fun loadInitial(size: Int = 10) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            _getState.offer(PaginatorState.Loading)
            val items = getBlock(0, size)
            _totalCount.offer(getCount())
            _itemsList.addAll(items)
            _items.offer(_itemsList)
            _getState.offer(PaginatorState.Complete)
            if (items.isEmpty()) {
                _getState.offer(PaginatorState.Empty)
            }
        }
    }

    actual fun refresh() {
        _itemsList.clear()
        _items.offer(_itemsList)
        loadInitial()
    }
}