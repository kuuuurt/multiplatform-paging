package com.kuuurt.paging.sample.multiplatform.library.helpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

actual fun dispatcher(): CoroutineDispatcher = Dispatchers.Main