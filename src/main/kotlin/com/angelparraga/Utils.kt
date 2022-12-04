package com.angelparraga

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


fun getUltraName(character: Character, id: Int): String {
    return if (id > 0 || character == Character.HORROR) {
        character.ultraMutations[id]
    } else {
        "None"
    }
}

fun getMutationNameList(mutations: String): List<String> {
    val response = mutableListOf<String>()
    mutations.forEachIndexed { index, c ->
        if (c.digitToInt() == 1) response.add(Mutations.values()[index].mutationName)
    }

    return response
}

fun getTestData(): NuclearRunDB {
    val data =
        "{\"current\":{\"char\":3,\"lasthit\":2,\"world\":1,\"level\":1,\"crown\":1,\"wepA\":43,\"wepB\":0,\"skin\":1,\"ultra\":0,\"charlvl\":2,\"loops\":0,\"win\":true,\"mutations\":\"00000000000000000000000000000\",\"kills\":15,\"health\":5,\"steamid\":76561198087280179,\"type\":\"normal\",\"timestamp\":1669801734},\"previous\":{\"char\":10,\"lasthit\":14,\"world\":3,\"level\":1,\"crown\":1,\"wepA\":4,\"wepB\":8,\"skin\":0,\"ultra\":0,\"charlvl\":5,\"loops\":0,\"win\":true,\"mutations\":\"00000001001100000000000000000\",\"kills\":173,\"health\":0,\"steamid\":76561198087280179,\"type\":\"normal\",\"timestamp\":1669800617}}"

    return Json.decodeFromString<NTResponse>(data).previous!!.toNuclearRunDB()
}