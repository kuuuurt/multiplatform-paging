package com.kuuurt.paging.multiplatform

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

sealed class PaginatorState {
    object Complete : PaginatorState()
    object Loading : PaginatorState()
    object Empty : PaginatorState()
    class Error(val throwable: Throwable) : PaginatorState()
}