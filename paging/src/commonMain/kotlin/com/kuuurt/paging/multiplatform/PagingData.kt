package com.kuuurt.paging.multiplatform


/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

expect class PagingData<T : Any> {
    fun <R : Any> mapSync(transform: (T) -> R): PagingData<R>
    fun <R : Any> flatMapSync(transform: (T) -> Iterable<R>): PagingData<R>
    fun filterSync(predicate: (T) -> Boolean): PagingData<T>
    fun insertHeaderItem(item: T): PagingData<T>
    fun insertFooterItem(item: T): PagingData<T>
}