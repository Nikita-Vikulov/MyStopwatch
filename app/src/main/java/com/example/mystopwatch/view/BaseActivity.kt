package com.example.mystopwatch.view

import com.example.mystopwatch.model.TimestampFormatter
import com.example.mystopwatch.model.AppState
import com.example.mystopwatch.model.ElapsedTimeCache
import com.example.mystopwatch.model.StopwatchStateCache

class BaseActivity(
    private val stopwatchStateCache: StopwatchStateCache,
    private val elapsedTimeCache: ElapsedTimeCache,
    private val timestampMillisecondsFormatter: TimestampFormatter
) {

    var currentState: AppState = AppState.Paused(0)
        private set

    fun start() {
        currentState = stopwatchStateCache.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopwatchStateCache.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = AppState.Paused(0)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is AppState.Paused -> currentState.elapsedTime
            is AppState.Running -> elapsedTimeCache.calculate(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}
