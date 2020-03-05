package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

/* Actual is implemented on iosMain but `expect` looks for iosArm64 and iosX64 */
@ExperimentalCoroutinesApi
expect class DataSource<T>(
    clientScope: CoroutineScope,
    getCount: suspend () -> Int,
    getBlock: suspend (Int, Int) -> List<T>
) {
    val getState: Flow<PaginatorState>
    val totalCount: Flow<Int>

    fun refresh()

    class Factory<T>(
        clientScope: CoroutineScope,
        getCount: suspend () -> Int,
        getBlock: suspend (Int, Int) -> List<T>
    ) {
        val dataSource: Flow<DataSource<T>>
    }
}