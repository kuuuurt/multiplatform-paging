package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@ExperimentalCoroutinesApi
expect class Paginator<T>(
    clientScope: CoroutineScope,
    dataSourceFactory: DataSource.Factory<T>
) : PaginatorDetails

