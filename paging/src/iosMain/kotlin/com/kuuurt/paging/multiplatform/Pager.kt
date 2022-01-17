package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

@OptIn(ExperimentalCoroutinesApi::class)
actual class Pager<K : Any, V : Any> actual constructor(
    private val clientScope: CoroutineScope,
    private val config: PagingConfig,
    private val initialKey: K,
    private val getItems: suspend (K, Int) -> PagingResult<K, V>
) {

    private val _pagingData = MutableStateFlow<PagingData<V>?>(null)
    actual val pagingData: Flow<PagingData<V>> get() = _pagingData.filterNotNull()

    private val _hasNextPage = MutableStateFlow(true)
    val hasNextPage: Boolean
        get() = _hasNextPage.value

    private val currentPagingResult: MutableStateFlow<PagingResult<K, V>?> = MutableStateFlow(null)

    init {
        loadNext()
    }

    fun refresh() {
        currentPagingResult.value = null
        _hasNextPage.value = false
        loadNext()
    }

    fun loadPrevious() {
        loadItems(LoadDirection.PREVIOUS)
    }

    fun loadNext() {
        loadItems(LoadDirection.NEXT)
    }

    private fun loadItems(loadDirection: LoadDirection) {
        val pagingResult = currentPagingResult.value
        val key = if (pagingResult == null) {
            initialKey
        } else {
            when (loadDirection) {
                LoadDirection.NEXT -> pagingResult.nextKey()
                LoadDirection.PREVIOUS -> pagingResult.prevKey()
            }
        }

        if (key != null && hasNextPage) {
            clientScope.launch {
                val newPagingResult = getItems(key, config.pageSize)
                _pagingData.value = _pagingData.value?.toMutableList()?.apply {
                    addAll(newPagingResult.items)
                }?.toPagingData() ?: newPagingResult.items.toPagingData()
                _hasNextPage.value = newPagingResult.items.size >= config.pageSize
                currentPagingResult.value = newPagingResult
            }
        }
    }

    enum class LoadDirection {
        PREVIOUS,
        NEXT
    }
}