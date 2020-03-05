package com.kuuurt.paging.multiplatform

import androidx.paging.PositionalDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import androidx.paging.DataSource as AndroidXDataSource

/**
 * Copyright 2020, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@ExperimentalCoroutinesApi
@FlowPreview
actual class DataSource<T> actual constructor(
    private val clientScope: CoroutineScope,
    private val getCount: suspend () -> Int,
    private val getBlock: suspend (Int, Int) -> List<T>
) : PositionalDataSource<T>() {
    private val _getState = ConflatedBroadcastChannel<PaginatorState>()
    actual val getState = _getState.asFlow()

    private val _totalCount = ConflatedBroadcastChannel(0)
    actual val totalCount = _totalCount.asFlow()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            val items = getBlock(params.startPosition, params.loadSize)
            callback.onResult(items)
            _getState.offer(PaginatorState.Complete)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            _getState.offer(PaginatorState.Loading)
            val items = getBlock(0, params.requestedLoadSize)
            _totalCount.offer(getCount())
            callback.onResult(items, 0)
            _getState.offer(PaginatorState.Complete)
            if (items.isEmpty()) {
                _getState.offer(PaginatorState.Empty)
            }
        }
    }

    actual class Factory<T> actual constructor(
        private val clientScope: CoroutineScope,
        private val getCount: suspend () -> Int,
        private val getBlock: suspend (Int, Int) -> List<T>
    ) : AndroidXDataSource.Factory<Int, T>() {

        private val _dataSource = ConflatedBroadcastChannel<DataSource<T>>()
        actual val dataSource = _dataSource.asFlow()

        override fun create(): AndroidXDataSource<Int, T> {
            val source = DataSource(clientScope, getCount, getBlock)
            _dataSource.offer(source)
            return source
        }
    }

    actual fun refresh() {
        invalidate()
    }
}