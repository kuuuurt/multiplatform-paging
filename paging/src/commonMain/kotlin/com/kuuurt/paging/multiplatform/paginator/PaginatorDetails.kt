package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.helpers.CommonFlow

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@Deprecated(
    message = "Deprecated in Paging 3.0.0-alpha01",
    replaceWith = ReplaceWith(
        "Pager<K, V>",
        "com.kuuurt.paging.multiplatform.paginator"
    )
)
interface PaginatorDetails {
    val getState: CommonFlow<PaginatorState>
    val totalCount: CommonFlow<Int>
    fun refresh()
}