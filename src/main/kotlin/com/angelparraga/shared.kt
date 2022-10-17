package com.angelparraga

const val BASE_URL = "https://tb-api.xyz/stream/get?s="
const val MY_KEY = "CDGNSTVY4"
const val MY_STEAMID = "76561198087280179"

data class NTResponse(
    val current: NTRun,
    val previous: NTRun
)

data class NTRun(
    val char: Int,
    val lastHit: Int,
    val world: Int,
    val level: Int,
    val crown: Int,
    val wepA: Int,
    val wepB: Int,
    val skin: Int,
    val ultra: Int,
    val charLvl: Int,
    val loops: Int,
    val win: Boolean,
    val mutations: String,
    val kills: Int,
    val health: Int,
    val steamId: String,
    val type: String,
    val timeStamp: Long
)