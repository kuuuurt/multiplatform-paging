package com.kuuurt.paging.multiplatform.helpers

import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Copyright 2020, Plentina, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */
actual fun <T : Any> Flow<PagingData<T>>.cachedIn(scope: CoroutineScope) = this