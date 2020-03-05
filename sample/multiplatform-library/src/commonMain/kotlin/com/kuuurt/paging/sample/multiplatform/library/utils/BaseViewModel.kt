package com.kuuurt.paging.sample.multiplatform.library.utils

import kotlinx.coroutines.CoroutineScope

/**
 * Copyright 2019, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 12/11/2019
 */

expect open class BaseViewModel() {
    val clientScope: CoroutineScope
    protected open fun onCleared()
}