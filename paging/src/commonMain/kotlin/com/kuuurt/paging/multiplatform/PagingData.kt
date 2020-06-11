package com.kuuurt.paging.multiplatform


/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

expect class PagingData<T : Any> {
    fun <R : Any> map(transform: (T) -> R): PagingData<R>
    fun <R : Any> flatMap(transform: (T) -> Iterable<R>): PagingData<R>
    fun filter(predicate: (T) -> Boolean): PagingData<T>
    fun insertHeaderItem(item: T): PagingData<T>
    fun insertFooterItem(item: T): PagingData<T>
    companion object {
        fun <T : R, R : Any> insertSeparators(
            pagingData: PagingData<T>,
            generator: (T?, T?) -> R?
        ): PagingData<R>
    }
}