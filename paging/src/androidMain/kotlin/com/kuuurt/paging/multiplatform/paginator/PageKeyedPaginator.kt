package com.kuuurt.paging.multiplatform.paginator

import androidx.lifecycle.asFlow
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kuuurt.paging.multiplatform.datasource.PageKeyedDataSource
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

@FlowPreview
@ExperimentalCoroutinesApi
actual class PageKeyedPaginator<T> actual constructor(
    private val clientScope: CoroutineScope,
    pageSize: Int,
    androidEnablePlaceHolders: Boolean,
    getCount: suspend () -> Int,
    getItems: suspend (Int, Int) -> List<T>
) : PaginatorDetails {
    internal actual val dataSourceFactory = PageKeyedDataSource.Factory(
        clientScope, getCount, getItems
    )

    val pagedList = LivePagedListBuilder(
        dataSourceFactory, PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(androidEnablePlaceHolders)
            .build()
    ).build().asFlow()

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
}