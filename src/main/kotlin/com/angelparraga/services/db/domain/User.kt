package com.angelparraga.services.db.domain

import org.bson.codecs.pojo.annotations.BsonId

/**
 * @param characters List of characters and their number of times played
 * @param enemies List of enemies and their number of times the player was killed by that enemy
 */
data class User(
    @BsonId
    val steamId: String,
    val statsOverview: StatsOverview? = null,
    val characters: Map<String, Int> = emptyMap(),
    val enemies: Map<String, Int> = emptyMap()
)
