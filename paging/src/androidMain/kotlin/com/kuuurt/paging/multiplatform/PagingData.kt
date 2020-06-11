package com.kuuurt.paging.multiplatform

import androidx.paging.PagingData as AndroidXPagingData

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 06/11/2020
 */

actual typealias PagingData<T> = AndroidXPagingData<T>