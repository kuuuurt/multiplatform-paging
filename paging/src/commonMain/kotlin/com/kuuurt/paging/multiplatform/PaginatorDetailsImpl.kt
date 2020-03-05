package com.kuuurt.paging.multiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PaginatorDetailsImpl<T>(
    private val clientScope: CoroutineScope,
    private val dataSourceFactory: DataSource.Factory<T>
) : PaginatorDetails {
    override val totalCount = dataSourceFactory.dataSource.flatMapLatest { it.totalCount }
    override val getState = dataSourceFactory.dataSource.flatMapLatest { it.getState }
    override fun refresh() {
        clientScope.launch {
            dataSourceFactory.dataSource.first().refresh()
        }
    }
}