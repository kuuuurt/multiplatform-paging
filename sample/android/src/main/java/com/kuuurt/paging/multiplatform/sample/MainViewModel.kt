package com.kuuurt.paging.multiplatform.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuuurt.paging.multiplatform.DataSource
import com.kuuurt.paging.multiplatform.Paginator

/**
 * Copyright 2020, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

class MainViewModel : ViewModel() {
    private val fakeData = FakeData()
    private val testDataSourceFactory = DataSource.Factory(
        viewModelScope,
        { fakeData.getCount() },
        { a, b -> fakeData.getData(a, b) }
    )
    val paginator = Paginator(viewModelScope, testDataSourceFactory)

    class FakeData {
        private var count = 0
        private val items = mutableListOf<String>()

        fun getCount() = count
        fun getData(startAt: Int, size: Int): List<String> {
            val list = mutableListOf<String>()
            if (startAt < 90) {
                for (i in startAt..(startAt + size)) {
                    list.add("Test $i")
                }
                items.addAll(list)
                count = items.size
            }
            return list
        }
    }
}