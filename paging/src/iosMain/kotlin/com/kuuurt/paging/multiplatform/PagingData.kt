package com.kuuurt.paging.multiplatform

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class PagingData<T : Any> : MutableList<T> by mutableListOf() {
    actual fun <R : Any> mapSync(transform: (T) -> R): PagingData<R> {
        return (this as MutableList<T>).map(transform) as PagingData<R>
    }

    actual fun <R : Any> flatMapSync(transform: (T) -> Iterable<R>): PagingData<R> {
        return (this as MutableList<T>).flatMap(transform) as PagingData<R>
    }

    actual fun filterSync(predicate: (T) -> Boolean): PagingData<T> {
        return (this as MutableList<T>).filter(predicate) as PagingData<T>
    }

    actual fun insertHeaderItem(item: T): PagingData<T> {
        return (this as MutableList<T>).apply {
            add(0, item)
        } as PagingData<T>
    }

    actual fun insertFooterItem(item: T): PagingData<T> {
        return (this as MutableList<T>).apply {
            add(size -1 , item)
        } as PagingData<T>
    }
}