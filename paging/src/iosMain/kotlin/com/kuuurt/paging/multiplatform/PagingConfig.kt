package com.kuuurt.paging.multiplatform

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class PagingConfig actual constructor(
    val pageSize: Int,
    val prefetchDistance: Int,
    val enablePlaceholders: Boolean,
    val initialLoadSize: Int,
    val maxSize: Int,
    val jumpThreshold: Int
)