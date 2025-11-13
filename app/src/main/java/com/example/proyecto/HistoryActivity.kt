package com.example.proyecto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var adapter: ScanHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recycler = findViewById<RecyclerView>(R.id.recyclerHistory)
        adapter = ScanHistoryAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        val dao = ScanDatabase.getDatabase(this).scanHistoryDao()

        lifecycleScope.launch {
            val list = dao.getAllScans()
            adapter.submitList(list)
        }
    }
}
