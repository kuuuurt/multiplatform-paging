package com.kuuurt.paging.multiplatform

import androidx.lifecycle.asFlow
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */


@FlowPreview
@ExperimentalCoroutinesApi
actual class Paginator<T> actual constructor(
    private val clientScope: CoroutineScope,
    private val dataSourceFactory: DataSource.Factory<T>
) : PaginatorDetails by PaginatorDetailsImpl<T>(clientScope, dataSourceFactory) {
    val pagedList = LivePagedListBuilder(
        dataSourceFactory, PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
    ).build().asFlow()
}