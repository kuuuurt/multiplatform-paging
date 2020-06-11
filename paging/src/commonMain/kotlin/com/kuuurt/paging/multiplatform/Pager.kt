package com.kuuurt.paging.multiplatform

import com.kuuurt.paging.multiplatform.helpers.CommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

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
    prevKey: (List<V>, K) -> K?,
    nextKey: (List<V>, K) -> K?,
    getItems: suspend (K, Int) -> List<V>
) {
    val pagingData: CommonFlow<PagingData<V>>
}
