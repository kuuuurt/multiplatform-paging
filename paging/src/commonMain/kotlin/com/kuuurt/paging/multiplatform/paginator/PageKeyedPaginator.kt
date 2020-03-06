package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.datasource.PageKeyedDataSource
import com.kuuurt.paging.multiplatform.datasource.PositionalDataSource
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
expect class PageKeyedPaginator<T>(
    clientScope: CoroutineScope,
    dataSourceFactory: PageKeyedDataSource.Factory<T>
) : PaginatorDetails

