package com.kuuurt.paging.multiplatform.sample

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright 2020, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 03/05/2020
 */

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel = MainViewModel()
    private val testAdapter by lazy { SamplePagedListAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recSample = findViewById<RecyclerView>(R.id.rec_sample)
        recSample.adapter = testAdapter

        viewModel.paginator.pagedList.asLiveData().observe(this, Observer {
            testAdapter.submitList(it)
        })
    }

}