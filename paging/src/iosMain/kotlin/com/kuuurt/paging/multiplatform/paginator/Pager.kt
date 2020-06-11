package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.helpers.CommonFlow
import com.kuuurt.paging.multiplatform.helpers.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class Pager<K : Any, V : Any> actual constructor(
    private val clientScope: CoroutineScope,
    private val config: PagingConfig,
    private val initialKey: K,
    private val prevKey: (List<V>, K) -> K,
    private val nextKey: (List<V>, K) -> K,
    private val getItems: suspend (K, Int) -> List<V>
) {
    private val items = PagingData<V>()

    private val _pagingData = ConflatedBroadcastChannel<PagingData<V>>()
    actual val pagingData: CommonFlow<PagingData<V>> get() = _pagingData.asCommonFlow()

    private var currentKey: K? = initialKey

    init {
        loadNext()
    }

    fun loadPrevious() {
        loadItems { items, currentKey ->
            if (currentKey == initialKey) null else prevKey(items, currentKey)
        }
    }

    fun loadNext() {
        loadItems { items, currentKey ->
            if (items.isEmpty()) null else nextKey(items, currentKey)
        }
    }

    private fun loadItems(newKey: (List<V>, K) -> K?) {
        val key = currentKey
        if (key != null) {
            clientScope.launch {
                print("Loading items at $key\n")
                print("Items count: ${items.size}\n")
                val newItems = getItems(key, config.pageSize)
                print("New items count: ${newItems.size}\n")
                items.addAll(getItems(key, config.pageSize))
                print("Items after count: ${newItems.size}\n")
                print("Items offered\n")
                _pagingData.offer(items)
                currentKey = newKey(newItems, key)
                print("New Key: ${currentKey}\n")
            }
        }
    }
}