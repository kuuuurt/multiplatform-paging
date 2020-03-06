package com.kuuurt.paging.multiplatform.helpers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Copyright 2020, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/06/2020
 */
actual fun dispatcher(): CoroutineDispatcher = Dispatchers.Main