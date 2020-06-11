package com.kuuurt.paging.multiplatform.paginator

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

expect class PagingConfig {
    val pageSize: Int
    val enablePlaceholders: Boolean
}