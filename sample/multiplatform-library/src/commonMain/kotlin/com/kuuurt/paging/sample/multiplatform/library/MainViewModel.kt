package com.kuuurt.paging.sample.multiplatform.library

import com.kuuurt.paging.multiplatform.paginator.PageKeyedPaginator
import com.kuuurt.paging.sample.multiplatform.library.utils.BaseViewModel

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

class MainViewModel : BaseViewModel() {
    private val fakeData = FakePagedData()

    val paginator = PageKeyedPaginator(
        clientScope,
        { fakeData.getCount() },
        { a, b -> fakeData.getData(a, b) }
    )

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
        private val totalPages = 10
        private val count = 95
        private val items = mutableListOf<String>()

        fun getCount() = count
        fun getData(page: Int, size: Int): List<String> {
            println(page)
            println(size)
            val list = mutableListOf<String>()
            if (page in 1..totalPages) {
                var startSize = (page-1) * size
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
            }
            return list
        }
    }
}