package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Copyright 2020, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@ExperimentalCoroutinesApi
@FlowPreview
actual class Paginator<T> actual constructor(
    private val clientScope: CoroutineScope,
    private val dataSourceFactory: DataSource.Factory<T>
) : PaginatorDetails by PaginatorDetailsImpl<T>(clientScope, dataSourceFactory) {
    val pagedList = dataSourceFactory.dataSource
        .flatMapLatest { it.items }

    fun loadMore() {
        clientScope.launch {
            dataSourceFactory.dataSource.first().loadMore()
        }
    }

    init {
        clientScope.launch {
            dataSourceFactory.dataSource.first().loadInitial()
        }
    }
}