package com.kuuurt.paging.multiplatform.paginator

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual class PagingData<T: Any> {
    actual fun <R : Any> map(transform: (T) -> R): PagingData<R> {
        TODO("Not yet implemented")
    }

    actual fun <R : Any> flatMap(transform: (T) -> Iterable<R>): PagingData<R> {
        TODO("Not yet implemented")
    }

    actual fun filter(predicate: (T) -> Boolean): PagingData<T> {
        TODO("Not yet implemented")
    }

    actual fun insertHeaderItem(item: T): PagingData<T> {
        TODO("Not yet implemented")
    }

    actual fun insertFooterItem(item: T): PagingData<T> {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual fun <T : R, R : Any> insertSeparators(
            pagingData: PagingData<T>,
            generator: (T?, T?) -> R?
        ): PagingData<R> {
            TODO("Not yet implemented")
        }
    }
}