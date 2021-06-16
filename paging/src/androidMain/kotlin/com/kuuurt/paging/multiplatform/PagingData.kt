package com.kuuurt.paging.multiplatform

import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import androidx.paging.PagingData as AndroidXPagingData

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual typealias PagingData<T> = AndroidXPagingData<T>

actual suspend fun <T : Any> PagingData<T>.filter(predicate: suspend (T) -> Boolean): PagingData<T> {
    return this.filter(predicate)
}

actual suspend fun <T : Any, R : Any> PagingData<T>.map(transform: suspend (T) -> R): PagingData<R> {
    return this.map(transform)
}

actual suspend fun <T : Any, R : Any> PagingData<T>.flatMap(transform: suspend (T) -> Iterable<R>): PagingData<R> {
    return this.flatMap(transform)
}