package com.kuuurt.paging.multiplatform.datasource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
expect class PositionalDataSource<T>(
    clientScope: CoroutineScope,
    getCount: suspend () -> Int,
    getBlock: suspend (Int, Int) -> List<T>
) : DataSource<T> {
    internal class Factory<T>(
        clientScope: CoroutineScope,
        getCount: suspend () -> Int,
        getBlock: suspend (Int, Int) -> List<T>
    ) {
        val dataSource: Flow<PositionalDataSource<T>>
    }
}