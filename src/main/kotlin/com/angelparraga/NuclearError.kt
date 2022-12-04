package com.angelparraga

sealed class NuclearError(override val message: String) : Exception(message) {
    class NoSteamIdProvided : NuclearError("No steamId provided.")
    class NoKeyProvided : NuclearError("No key provided.")
    class NoRunFound : NuclearError("No run found.")
    class NoIdProvided : NuclearError("No id provided.")
}