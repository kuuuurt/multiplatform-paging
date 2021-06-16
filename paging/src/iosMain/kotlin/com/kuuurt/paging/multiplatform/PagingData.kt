package com.kuuurt.paging.multiplatform

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class PagingData<T : Any> : MutableList<T> by mutableListOf()

actual suspend fun <T : Any> PagingData<T>.filter(predicate: suspend (T) -> Boolean): PagingData<T> {
    return (this@filter as MutableList<T>).filter {
        predicate(it)
    } as PagingData<T>
}

actual suspend fun <T : Any, R : Any> PagingData<T>.map(transform: suspend (T) -> R): PagingData<R> {
    return (this@map as MutableList<T>).map {
        transform(it)
    } as PagingData<R>
}

actual suspend fun <T : Any, R : Any> PagingData<T>.flatMap(transform: suspend (T) -> Iterable<R>): PagingData<R> {
    return (this@flatMap as MutableList<T>).flatMap {
        transform(it)
    } as PagingData<R>
}