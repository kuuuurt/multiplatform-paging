package com.kuuurt.paging.multiplatform


/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

expect class PagingConfig(
    pageSize: Int,
    prefetchDistance: Int = pageSize,
    enablePlaceholders: Boolean = false,
    initialLoadSize: Int = pageSize * 3,
    maxSize: Int = Int.MAX_VALUE,
    jumpThreshold: Int = Int.MIN_VALUE
)