package com.angelparraga.services.stats

import com.angelparraga.NTRunDto
import com.angelparraga.NuclearError
import com.angelparraga.World
import com.angelparraga.services.db.NuclearRunDAO
import com.angelparraga.services.db.UserDAO
import com.angelparraga.services.db.domain.*

interface StatsService {
    suspend fun getStatsOverview(steamId: String): StatsOverview
    suspend fun updateStatsOverview(steamId: String): StatsOverview
}

class StatsServiceImpl(
    private val userDAO: UserDAO,
    private val nuclearRunDAO: NuclearRunDAO
) : StatsService {
    override suspend fun getStatsOverview(steamId: String): StatsOverview {
        val user = userDAO.getUser(steamId) ?: throw NuclearError.UserNotFound(steamId)
        return user.statsOverview ?: throw NuclearError.NoStatsOverview()
    }

    override suspend fun updateStatsOverview(steamId: String): StatsOverview {
        val user = userDAO.getUser(steamId) ?: User(steamId)
        userDAO.addUser(user)

        val lastUpdate = user.statsOverview?.lastUpdate ?: 0
        val runs = nuclearRunDAO.findBySteamIdAndTimestampGreaterThan(steamId, lastUpdate)

        if (runs.isEmpty()) {
            throw NuclearError.NoRunsFound()
        }

        val statsOverview = buildStatsOverview(runs, user)
        userDAO.updateStatsOverview(steamId, statsOverview)

        return statsOverview
    }

    private fun buildStatsOverview(runs: List<NTRunDto>, user: User): StatsOverview {
        val currentStatsOverview = user.statsOverview
        var bestLevel = currentStatsOverview?.bestLevel
        var mostKills = currentStatsOverview?.mostKills
        var longestRun = currentStatsOverview?.longestRun
        val lastUpdate = System.currentTimeMillis()
        val character = user.characters.maxByOrNull { it.value }
        val mostUsedCharacter = character?.key ?: "None"
        val characterRuns = character?.value ?: 0
        val enemy = user.enemies.maxByOrNull { it.value }
        val mostKilledBy = enemy?.key ?: "None"
        val enemyRuns = enemy?.value ?: 0

        runs.forEach {
            mostKills = if (mostKills == null || it.kills > mostKills!!) it.kills else mostKills
            longestRun = if (longestRun == null || it.timestamp > longestRun!!) it.timestamp else longestRun
            bestLevel = getBestLevel(bestLevel, it.worldLevel, it.world, it.loops)
        }

        return StatsOverview(
            bestLevel = bestLevel!!,
            mostKills = mostKills!!,
            mostUsedCharacter = CharacterRuns(mostUsedCharacter, characterRuns),
            mostKilledBy = EnemyRuns(mostKilledBy, enemyRuns),
            longestRun = longestRun!!,
            lastUpdate = lastUpdate
        )
    }

    /**
     * Determines the best level based on the current best level and a new level.
     *
     * @param currentBestLevel The current best level.
     * @param newLevel The new level to compare.
     * @param newWorldName The name of the new world.
     * @param newLoop The new loop value.
     * @return The best level between the current best level and the new level.
     * @throws NuclearError.WorldNotFound If the world name cannot be found.
     */
    private fun getBestLevel(currentBestLevel: Level?, newLevel: Int, newWorldName: String, newLoop: Int): Level {
        if (currentBestLevel == null) {
            return Level(newLevel, newWorldName, newLoop)
        }

        val newWorld = World.fromWorldName(newWorldName) ?: throw NuclearError.WorldNotFound(newWorldName)
        val currentWorld =
            World.fromWorldName(currentBestLevel.world) ?: throw NuclearError.WorldNotFound(currentBestLevel.world)

        /*
         * Worlds with id greater than 7 are special areas. They have an id of 100 + the area id.
         * This approach is to ensure that these areas are not always considered the best level, as they may not be the best level.
         */
        val maxValidId = 7
        val worldId = if (newWorld.id < maxValidId) newWorld.id else newWorld.id - 100

        if (newLoop < currentBestLevel.loop ||
            (newLoop == currentBestLevel.loop && worldId < currentWorld.id) ||
            (newLoop == currentBestLevel.loop && worldId == currentWorld.id && newLevel < currentBestLevel.level)
        ) {
            return currentBestLevel
        }

        return Level(newLevel, newWorldName, newLoop)
    }
}