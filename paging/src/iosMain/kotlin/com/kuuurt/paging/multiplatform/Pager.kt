package com.kuuurt.paging.multiplatform

import com.kuuurt.paging.multiplatform.helpers.CommonFlow
import com.kuuurt.paging.multiplatform.helpers.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
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
    private val items = PagingData<V>()

    private val _pagingData = ConflatedBroadcastChannel<PagingData<V>>()
    actual val pagingData: CommonFlow<PagingData<V>> get() = _pagingData.asCommonFlow()

    private var currentPagingResult: PagingResult<K, V>? = null

    init {
        loadNext()
    }

    fun loadPrevious() {
        loadItems(LoadDirection.PREVIOUS)
    }

    fun loadNext() {
        loadItems(LoadDirection.NEXT)
    }

    private fun loadItems(loadDirection: LoadDirection) {
        val pagingResult = currentPagingResult
        val key = if (pagingResult == null) {
            initialKey
        } else {
            when (loadDirection) {
                LoadDirection.NEXT -> {
                    pagingResult.nextKey()
                }
                LoadDirection.PREVIOUS -> {
                    pagingResult.prevKey()
                }
            }
        }
        if (key != null) {
            clientScope.launch {
                val newPagingResult = getItems(key, config.pageSize)
                items.addAll(newPagingResult.items)
                _pagingData.offer(items)
                currentPagingResult = newPagingResult
            }
        }
    }

    enum class LoadDirection {
        PREVIOUS,
        NEXT
    }
}