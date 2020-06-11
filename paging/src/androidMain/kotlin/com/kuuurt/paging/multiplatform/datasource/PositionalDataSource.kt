package com.kuuurt.paging.multiplatform.datasource

import com.kuuurt.paging.multiplatform.paginator.PaginatorState
import androidx.paging.PositionalDataSource as AndroidXPositionalDataSource
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
actual class PositionalDataSource<T: Any> actual constructor(
    private val clientScope: CoroutineScope,
    private val getCount: suspend () -> Int,
    private val getBlock: suspend (Int, Int) -> List<T>
) : AndroidXPositionalDataSource<T>(), DataSource<T> {
    private val _getState = ConflatedBroadcastChannel<PaginatorState>()
    override val getState = _getState.asFlow()

    private val _totalCount = ConflatedBroadcastChannel(0)
    override val totalCount = _totalCount.asFlow()

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

    override fun refresh() {
        invalidate()
    }

    internal actual class Factory<T: Any> actual constructor(
        private val clientScope: CoroutineScope,
        private val getCount: suspend () -> Int,
        private val getBlock: suspend (Int, Int) -> List<T>
    ) : AndroidXDataSource.Factory<Int, T>() {

        private val _dataSource = ConflatedBroadcastChannel<PositionalDataSource<T>>()
        actual val dataSource = _dataSource.asFlow()

        override fun create(): AndroidXDataSource<Int, T> {
            val source =
                PositionalDataSource(
                    clientScope,
                    getCount,
                    getBlock
                )
            _dataSource.offer(source)
            return source
        }
    }
}