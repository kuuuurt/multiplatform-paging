package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.datasource.PositionalDataSource
import com.kuuurt.paging.multiplatform.helpers.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@ExperimentalCoroutinesApi
@FlowPreview
actual class PositionalPaginator<T> actual constructor(
    private val clientScope: CoroutineScope,
    private val dataSourceFactory: PositionalDataSource.Factory<T>
) : PaginatorDetails {
    val pagedList = dataSourceFactory.dataSource
        .flatMapLatest { it.items }
        .asCommonFlow()

    override val totalCount = dataSourceFactory
        .dataSource
        .flatMapLatest { it.totalCount }
        .asCommonFlow()

    override val getState = dataSourceFactory
        .dataSource
        .flatMapLatest { it.getState }
        .asCommonFlow()

    override fun refresh() {
        clientScope.launch {
            dataSourceFactory.dataSource.first().refresh()
        }
    }

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