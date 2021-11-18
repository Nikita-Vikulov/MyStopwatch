package com.example.mystopwatch.model

class StopwatchStateCache(
    private val repository: Repository,
    private val elapsedTimeCache: ElapsedTimeCache,
) {

    fun calculateRunningState(oldState: AppState): AppState.Running =
        when (oldState) {
            is AppState.Running -> oldState
            is AppState.Paused -> {
                AppState.Running(
                    startTime = repository.getMilliseconds(),
                    elapsedTime = oldState.elapsedTime
                )
            }
        }

    fun calculatePausedState(oldState: AppState): AppState.Paused =
        when (oldState) {
            is AppState.Running -> {
                val elapsedTime = elapsedTimeCache.calculate(oldState)
                AppState.Paused(elapsedTime = elapsedTime)
            }
            is AppState.Paused -> oldState
        }
}
