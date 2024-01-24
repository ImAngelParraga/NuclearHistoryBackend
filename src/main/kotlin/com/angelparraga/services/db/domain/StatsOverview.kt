package com.angelparraga.services.db.domain

/**
 * @param bestLevel Farthest level reached
 * @param mostKills Most kills in a single run
 * @param mostUsedCharacter Most used character
 * @param mostKilledBy Enemy most killed by
 * @param longestRun Longest run in seconds
 * @param lastUpdate Last update time in milliseconds
 */
data class StatsOverview(
    val bestLevel: Level,
    val mostKills: Int,
    val mostUsedCharacter: CharacterRuns,
    val mostKilledBy: EnemyRuns,
    val longestRun: Long,
    val lastUpdate: Long
)
