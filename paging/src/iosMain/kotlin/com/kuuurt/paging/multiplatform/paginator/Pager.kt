package com.kuuurt.paging.multiplatform.paginator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class Pager<K : Any, V : Any> actual constructor(
    clientScope: CoroutineScope,
    config: PagingConfig,
    initialKey: K,
    prevKey: (List<V>, K) -> K,
    nextKey: (List<V>, K) -> K,
    getItems: suspend (K, Int) -> List<V>
) {
    actual val pagingData: Flow<PagingData<V>> = TODO("Not implemented")
}