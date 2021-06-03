package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
expect class Pager<K: Any, V: Any>(
    clientScope: CoroutineScope,
    config: PagingConfig,
    initialKey: K,
    getItems: suspend (K, Int) -> PagingResult<K, V>
) {
    val pagingData: Flow<PagingData<V>>
}
