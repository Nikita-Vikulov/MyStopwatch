package com.example.mystopwatch.model

import com.example.mystopwatch.model.AppState
import com.example.mystopwatch.model.Repository

class ElapsedTimeCache(
    private val repository: Repository,
) {

    fun calculate(state: AppState.Running): Long {
        val currentTimestamp = repository.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }
}