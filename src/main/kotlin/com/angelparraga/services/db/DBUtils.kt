package com.angelparraga.services.db

import software.amazon.awssdk.enhanced.dynamodb.Key

fun keyFromId(id: String): Key = Key.builder().partitionValue(id).build()

/*
val string = String::class.createType()
val int = Int::class.createType()
val boolean = Boolean::class.createType()
val list = typeOf<List<String>>()
val char = Char::class.createType()
val long = Long::class.createType()
val uuid = UUID::class.createType()


@Suppress("UNCHECKED_CAST")
fun MutableMap<String, AttributeValue>.addContent(run: NuclearRunDB) {
    run::class.declaredMemberProperties.forEach {
        when (it.returnType) {
            uuid -> this[it.name] = AttributeValue.S(run.id)
            string -> this[it.name] = AttributeValue.S(it.getter.call(run) as String)
            int -> this[it.name] = AttributeValue.N((it.getter.call(run) as Int).toString())
            boolean -> this[it.name] = AttributeValue.Bool(it.getter.call(run) as Boolean)
            list -> this[it.name] = AttributeValue.Ss(it.getter.call(run) as List<String>)
            char -> this[it.name] = AttributeValue.S((it.getter.call(run) as Char).toString())
            long -> this[it.name] = AttributeValue.N((it.getter.call(run) as Long).toString())
        }
        println("Added ${it.name} to the item. Value is ${it.getter.call(run)}")
    }
}*/
