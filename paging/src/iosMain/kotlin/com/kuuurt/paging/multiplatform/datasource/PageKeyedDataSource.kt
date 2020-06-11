package com.kuuurt.paging.multiplatform.datasource

import com.kuuurt.paging.multiplatform.paginator.PaginatorState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@ExperimentalCoroutinesApi
@FlowPreview
actual class PageKeyedDataSource<T: Any> actual constructor(
    private val clientScope: CoroutineScope,
    private val getCount: suspend () -> Int,
    private val getBlock: suspend (Int, Int) -> List<T>
) : DataSource<T> {
    private val _getState = ConflatedBroadcastChannel<PaginatorState>()
    override val getState = _getState.asFlow()

    private val _totalCount = ConflatedBroadcastChannel(0)
    override val totalCount = _totalCount.asFlow()

    private var _itemsList = mutableListOf<T>()
    private val _items = ConflatedBroadcastChannel<List<T>>(listOf())
    val items = _items.asFlow()

    private var page = 1
    var pageSize = 10

    internal actual class Factory<T: Any> actual constructor(
        clientScope: CoroutineScope,
        getCount: suspend () -> Int,
        getBlock: suspend (Int, Int) -> List<T>
    ) {
        actual val dataSource: Flow<PageKeyedDataSource<T>> = flowOf(
            PageKeyedDataSource(
                clientScope,
                getCount,
                getBlock
            )
        )
    }

    fun loadInitial() {
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            _getState.offer(PaginatorState.Loading)
            val items = getBlock(page, pageSize)
            val count = getCount()
            _totalCount.offer(count)
            _itemsList.addAll(items)
            _items.offer(_itemsList)
            _getState.offer(PaginatorState.Complete)
            if (items.isEmpty()) {
                _getState.offer(PaginatorState.Empty)
            }
            page++
        }
    }

    fun loadBefore() {
        val currentPage = page--
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            val items = getBlock(currentPage, pageSize)
            _itemsList.addAll(items)
            _items.offer(_itemsList)
            _getState.offer(PaginatorState.Complete)
        }
    }

    fun loadAfter() {
        val currentPage = page++
        clientScope.launch(CoroutineExceptionHandler { _, exception ->
            _getState.offer(PaginatorState.Error(exception))
        }) {
            val items = getBlock(currentPage, pageSize)
            _itemsList.addAll(items)
            _items.offer(_itemsList)
            _getState.offer(PaginatorState.Complete)
        }
    }

    override fun refresh() {
        _itemsList.clear()
        _items.offer(_itemsList)
        page = 1
        loadInitial()
    }
}