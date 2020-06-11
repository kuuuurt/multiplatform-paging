package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.helpers.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import androidx.paging.Pager as AndroidXPager

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
actual class Pager<K : Any, V : Any> actual constructor(
    clientScope: CoroutineScope,
    config: PagingConfig,
    initialKey: K,
    prevKey: (List<V>, K) -> K,
    nextKey: (List<V>, K) -> K,
    getItems: suspend (K, Int) -> List<V>
) {
    actual val pagingData = AndroidXPager(
        config = config,
        pagingSourceFactory = {
            PagingSource(initialKey, prevKey, nextKey, getItems)
        }
    ).flow.asCommonFlow()

    class PagingSource<K : Any, V : Any>(
        private val startKey: K,
        private val prevKey: (List<V>, K) -> K,
        private val nextKey: (List<V>, K) -> K,
        private val getItems: suspend (K, Int) -> List<V>
    ) : androidx.paging.PagingSource<K, V>() {
        override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
            val currentKey = params.key ?: startKey
            return try {
                val items = getItems(currentKey, params.loadSize)
                LoadResult.Page(
                    data = items,
                    prevKey = if (currentKey == startKey) null else prevKey(items, currentKey),
                    nextKey = if (items.isEmpty()) null else nextKey(items, currentKey)
                )
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }
    }
}