package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.helpers.CommonFlow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

interface PaginatorDetails {
    val getState: CommonFlow<PaginatorState>
    val totalCount: CommonFlow<Int>
    fun refresh()
}