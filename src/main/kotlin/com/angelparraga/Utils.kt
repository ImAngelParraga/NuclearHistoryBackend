package com.angelparraga

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
