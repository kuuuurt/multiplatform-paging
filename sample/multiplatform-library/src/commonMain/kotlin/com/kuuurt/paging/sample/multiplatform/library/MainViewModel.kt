package com.kuuurt.paging.sample.multiplatform.library

import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.helpers.asCommonFlow
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import com.kuuurt.paging.sample.multiplatform.library.utils.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

class MainViewModel : BaseViewModel() {
    private val fakeData = FakePositionalData()
    private val pageSize = 15

    val pager = Pager(
        clientScope,
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false,
            initialLoadSize = pageSize
        ),
        initialKey = 1,
        prevKey = { _, currentKey -> currentKey - pageSize - 1 },
        nextKey = { _, currentKey -> currentKey + pageSize + 1 },
        getItems = { a, b -> fakeData.getData(a, b) }
    )

    private var _removedItemsFlow = MutableStateFlow(mutableListOf<String>())
    private val removedItemsFlow: Flow<MutableList<String>> get() = _removedItemsFlow

    val pagingData
        get() = pager.pagingData
            .combine(removedItemsFlow) { pagingData, removed ->
                pagingData.filterSync { it !in removed }
            }
            .cachedIn(clientScope)
            .asCommonFlow()

    fun removeItem(item: String) {
        var removedItems = _removedItemsFlow.value
        removedItems.add(item)
        removedItems = removedItems.distinctBy { it }.toMutableList()
        _removedItemsFlow.value = removedItems
    }

    class FakePositionalData {
        private val count = 100
        private val items = mutableListOf<String>()

        fun getCount() = count
        fun getData(startAt: Int, size: Int): List<String> {
            val list = mutableListOf<String>()
            var endSize = startAt + size
            if (endSize > count) {
                endSize = count
            }
            if (startAt < endSize) {
                for (i in startAt..endSize) {
                    list.add("Positional Test $i")
                }
                items.addAll(list)
            }
            return list
        }
    }

    class FakePagedData {
        private val count = 95
        private val items = mutableListOf<String>()

        fun getCount() = count
        fun getData(page: Int, size: Int): List<String> {
            val list = mutableListOf<String>()
            var startSize = (page - 1) * size
            var endSize = startSize + size
            if (endSize > count) {
                endSize = count
            }
            if (startSize < endSize) {
                for (i in startSize until endSize) {
                    list.add("Paged Test $i")
                }
                items.addAll(list)
            }
            return list
        }
    }
}