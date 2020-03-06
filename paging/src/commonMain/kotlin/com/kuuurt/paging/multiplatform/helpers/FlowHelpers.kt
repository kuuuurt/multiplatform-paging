package com.kuuurt.paging.multiplatform.helpers

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.io.core.Closeable

/**
 * Copyright 2019, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 12/11/2019
 */

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> ConflatedBroadcastChannel<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(asFlow())

fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)

class CommonFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()
        onEach { block(it) }.launchIn(CoroutineScope(job + dispatcher()))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}