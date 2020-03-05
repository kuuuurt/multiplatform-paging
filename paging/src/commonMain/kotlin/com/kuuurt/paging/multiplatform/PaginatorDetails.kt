package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.flow.Flow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

interface PaginatorDetails {
    val getState: Flow<PaginatorState>
    val totalCount: Flow<Int>
    fun refresh()
}