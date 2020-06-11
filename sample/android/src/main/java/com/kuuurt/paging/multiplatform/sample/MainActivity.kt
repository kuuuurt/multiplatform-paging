package com.kuuurt.paging.multiplatform.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kuuurt.paging.sample.multiplatform.library.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel = MainViewModel()
    private val testAdapter by lazy {
        SamplePagedListAdapter {
            viewModel.removeItem(it)
            refresh()
        }
    }

    private fun refresh() {
        testAdapter.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recSample = findViewById<RecyclerView>(R.id.rec_sample)
        recSample.adapter = testAdapter

        testAdapter.loadStateFlow
            .onEach { Log.d("State", it.toString()) }
            .launchIn(lifecycleScope)

        viewModel.pagingData
            .onEach { testAdapter.submitData(it) }
            .launchIn(lifecycleScope)
    }
}