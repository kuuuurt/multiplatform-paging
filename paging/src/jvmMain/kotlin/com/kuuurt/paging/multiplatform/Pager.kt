package com.kuuurt.paging.multiplatform

import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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
    getItems: suspend (K, Int) -> PagingResult<K, V>
) {
    actual val pagingData: Flow<PagingData<V>> = AndroidXPager(
        config = config,
        pagingSourceFactory = {
            PagingSource(
                initialKey,
                getItems
            )
        }
    ).flow

    class PagingSource<K : Any, V : Any>(
        private val initialKey: K,
        private val getItems: suspend (K, Int) -> PagingResult<K, V>
    ) : androidx.paging.PagingSource<K, V>() {

        override val jumpingSupported: Boolean
            get() = true

        override val keyReuseSupported: Boolean
            get() = true

        override fun getRefreshKey(state: PagingState<K, V>): K? {
            return null
//            return state.anchorPosition?.let { position ->
//                state.closestPageToPosition(position)?.let { page ->
//                    page.prevKey?.let {
//                        nextKey(page.data, it)
//                    }
//                }
//            }
        }

        override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
            val currentKey = params.key ?: initialKey
            return try {
                val pagingResult = getItems(currentKey, params.loadSize)
                LoadResult.Page(
                    data = pagingResult.items,
                    prevKey = if (currentKey == initialKey) null else pagingResult.prevKey(),
                    nextKey = if (pagingResult.items.isEmpty()) null else pagingResult.nextKey()
                )
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }
    }
}