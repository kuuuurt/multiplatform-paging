package com.kuuurt.paging.multiplatform.helpers

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/06/2020
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
/* Actual is implemented on iosMain but `expect` looks for iosArm64 and iosX64 */
expect fun dispatcher(): CoroutineDispatcher