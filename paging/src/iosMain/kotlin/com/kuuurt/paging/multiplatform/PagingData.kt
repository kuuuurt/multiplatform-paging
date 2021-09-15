package com.kuuurt.paging.multiplatform

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class PagingData<T : Any> internal constructor(items: List<T>) : List<T> by items

internal fun <T: Any> List<T>.toPagingData(): PagingData<T> =
    PagingData(this)

actual suspend fun <T : Any> PagingData<T>.filter(predicate: suspend (T) -> Boolean): PagingData<T> {
    return (this@filter as List<T>).filter {
        predicate(it)
    } as PagingData<T>
}

actual suspend fun <T : Any, R : Any> PagingData<T>.map(transform: suspend (T) -> R): PagingData<R> {
    return (this@map as List<T>).map {
        transform(it)
    } as PagingData<R>
}

actual suspend fun <T : Any, R : Any> PagingData<T>.flatMap(transform: suspend (T) -> Iterable<R>): PagingData<R> {
    return (this@flatMap as List<T>).flatMap {
        transform(it)
    } as PagingData<R>
}