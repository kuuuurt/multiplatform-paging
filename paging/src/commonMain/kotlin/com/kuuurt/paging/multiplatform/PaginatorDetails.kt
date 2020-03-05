package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.flow.Flow

interface PaginatorDetails {
    val getState: Flow<PaginatorState>
    val totalCount: Flow<Int>
    fun refresh()
}