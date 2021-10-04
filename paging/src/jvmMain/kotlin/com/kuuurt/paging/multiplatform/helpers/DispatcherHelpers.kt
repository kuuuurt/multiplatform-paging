package com.kuuurt.paging.multiplatform.helpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/06/2020
 */
actual fun dispatcher(): CoroutineDispatcher = Dispatchers.Main