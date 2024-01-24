package com.angelparraga

sealed class NuclearError(override val message: String) : Exception(message) {
    class NoSteamIdProvided : NuclearError("No steamId provided.")
    class NoKeyProvided : NuclearError("No key provided.")
    class NoRunFound : NuclearError("No run found.")
    class NoIdProvided : NuclearError("No id provided.")
    class RunAlreadyExists : NuclearError("Run already exists.")
    class UserNotFound(steamId: String) : NuclearError("User $steamId not found.")
    class NoRunsFound : NuclearError("No runs found.")
    class NoStatsOverview : NuclearError("User has no stats overview.")
    class WorldNotFound(world: String) : NuclearError("World $world not found.")
}