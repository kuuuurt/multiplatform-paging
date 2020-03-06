package com.kuuurt.paging.sample.multiplatform.library

import com.kuuurt.paging.multiplatform.datasource.PositionalDataSource
import com.kuuurt.paging.multiplatform.paginator.Paginator
import com.kuuurt.paging.sample.multiplatform.library.utils.BaseViewModel

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

class MainViewModel : BaseViewModel() {
    private val fakeData = FakeData()
    private val testDataSourceFactory = PositionalDataSource.Factory(
        clientScope,
        { fakeData.getCount() },
        { a, b -> fakeData.getData(a, b) }
    )
    val paginator = Paginator(
        clientScope,
        testDataSourceFactory
    )

    class FakeData {
        private val count = 100
        private val items = mutableListOf<String>()

        fun getCount() = count
        fun getData(startAt: Int, size: Int): List<String> {
            val list = mutableListOf<String>()
            var endSize = startAt + size
            if (endSize > 100) {
                endSize = 100
            }
            if (startAt < endSize) {
                for (i in startAt..endSize) {
                    list.add("Test $i")
                }
                items.addAll(list)
            }
            return list
        }
    }
}