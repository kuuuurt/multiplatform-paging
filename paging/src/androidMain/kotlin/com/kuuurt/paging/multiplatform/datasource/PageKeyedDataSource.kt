package com.kuuurt.paging.multiplatform.datasource

import com.kuuurt.paging.multiplatform.paginator.PaginatorState
import androidx.paging.PageKeyedDataSource as AndroidXPageKeyedDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import androidx.paging.DataSource as AndroidXDataSource

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@ExperimentalCoroutinesApi
@FlowPreview
actual class PageKeyedDataSource<T> actual constructor(
    private val clientScope: CoroutineScope,
    private val getCount: suspend () -> Int,
    private val getBlock: suspend (Int, Int) -> List<T>
) : AndroidXPageKeyedDataSource<Int, T>(), DataSource<T> {
    private val _getState = ConflatedBroadcastChannel<PaginatorState>()
    override val getState = _getState.asFlow()

    private val _totalCount = ConflatedBroadcastChannel(0)
    override val totalCount = _totalCount.asFlow()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            _getState.offer(PaginatorState.Loading)
            val items = getBlock(1, params.requestedLoadSize)
            val count = getCount()
            _totalCount.offer(count)
            callback.onResult(items, items.size, count, null, 2)
            _getState.offer(PaginatorState.Complete)
            if (items.isEmpty()) {
                _getState.offer(PaginatorState.Empty)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            val items = getBlock(params.key, params.requestedLoadSize)
            callback.onResult(items, params.key + 1)
            _getState.offer(PaginatorState.Complete)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            val items = getBlock(params.key, params.requestedLoadSize)
            callback.onResult(items, params.key - 1)
            _getState.offer(PaginatorState.Complete)
        }
    }

    override fun refresh() {
        invalidate()
    }

    internal actual class Factory<T> actual constructor(
        private val clientScope: CoroutineScope,
        private val getCount: suspend () -> Int,
        private val getBlock: suspend (Int, Int) -> List<T>
    ) : AndroidXDataSource.Factory<Int, T>() {

        private val _dataSource = ConflatedBroadcastChannel<PageKeyedDataSource<T>>()
        actual val dataSource = _dataSource.asFlow()

        override fun create(): AndroidXDataSource<Int, T> {
            val source = PageKeyedDataSource(
                clientScope,
                getCount,
                getBlock
            )
            _dataSource.offer(source)
            return source
        }
    }
}