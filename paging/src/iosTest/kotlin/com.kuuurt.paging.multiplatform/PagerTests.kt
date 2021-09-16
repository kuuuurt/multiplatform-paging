package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.native.concurrent.freeze
import kotlin.test.Test

class PagerTests {

    @Test
    fun ensureFrozenPagerCanLoadData(): Unit = runBlocking {
        val pageSize = 15
        val pager = Pager(
            CoroutineScope(Dispatchers.Default),
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                initialLoadSize = pageSize,
                prefetchDistance = pageSize,
                maxSize = Int.MAX_VALUE,
                jumpThreshold = Int.MIN_VALUE
            ),
            initialKey = 1,
            getItems = { currentKey, size ->
                val items = currentKey until (currentKey + size)
                PagingResult(
                    items = items.toList(),
                    currentKey = currentKey,
                    prevKey = { currentKey - pageSize - 1 },
                    nextKey = { currentKey + pageSize + 1 }
                )
            }
        )
        pager.freeze()
        pager.pagingData.first { it.size == pageSize }
        pager.loadNext()
        pager.pagingData.first { it.size == pageSize * 2 }
    }
}