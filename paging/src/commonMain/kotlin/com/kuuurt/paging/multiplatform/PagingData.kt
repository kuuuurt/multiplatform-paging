package com.kuuurt.paging.multiplatform


/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

expect class PagingData<T : Any>

expect suspend fun <T : Any> PagingData<T>.filter(predicate: suspend (T) -> Boolean): PagingData<T>

expect suspend fun <T : Any, R : Any> PagingData<T>.map(transform: suspend (T) -> R): PagingData<R>

expect suspend fun <T : Any, R : Any> PagingData<T>.flatMap(transform: suspend (T) -> Iterable<R>): PagingData<R>