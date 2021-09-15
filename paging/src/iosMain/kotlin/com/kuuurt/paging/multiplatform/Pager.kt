package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
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

    private data class State<K : Any, V : Any>(
        val pagingData: PagingData<V>? = null,
        val hasNextPage: Boolean = true,
        val currentPagingResult: PagingResult<K, V>? = null
    )

    private val state = MutableStateFlow<State<K, V>>(State())

    actual val pagingData: Flow<PagingData<V>>
        get() = state.mapNotNull { it.pagingData }

    val hasNextPage: Boolean
        get() = state.value.hasNextPage

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
        val currentState = state.value
        val pagingResult = currentState.currentPagingResult
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
                val newPagingData = currentState.pagingData?.toMutableList()?.apply {
                    addAll(newPagingResult.items)
                }?.toPagingData() ?: newPagingResult.items.toPagingData()
                state.value = currentState.copy(
                    pagingData = newPagingData,
                    hasNextPage = newPagingResult.items.size >= config.pageSize,
                    currentPagingResult = newPagingResult
                )
            }
        }
    }

    enum class LoadDirection {
        PREVIOUS,
        NEXT
    }
}