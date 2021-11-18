package com.example.mystopwatch.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mystopwatch.*
import com.example.mystopwatch.model.ElapsedTimeCache
import com.example.mystopwatch.model.Repository
import com.example.mystopwatch.model.StopwatchStateCache
import com.example.mystopwatch.model.TimestampFormatter
import com.example.mystopwatch.viewmodel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val timestampProvider = object : Repository {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
    private val stopwatchListOrchestrator = MainViewModel(
        BaseActivity(
            StopwatchStateCache(
                timestampProvider,
                ElapsedTimeCache(timestampProvider)
            ),
            ElapsedTimeCache(timestampProvider),
            TimestampFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text_time)
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        ).launch {
            stopwatchListOrchestrator.ticker.collect {
                textView.text = it
            }
        }

        findViewById<Button>(R.id.button_start).setOnClickListener {
            stopwatchListOrchestrator.start()
        }
        findViewById<Button>(R.id.button_pause).setOnClickListener {
            stopwatchListOrchestrator.pause()
        }
        findViewById<Button>(R.id.button_stop).setOnClickListener {
            stopwatchListOrchestrator.stop()
        }

    }
}