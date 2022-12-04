package com.angelparraga.plugins

import com.angelparraga.services.db.DBService
import com.angelparraga.services.db.DBServiceImpl
import com.angelparraga.services.nuclear.NuclearService
import com.angelparraga.services.nuclear.NuclearServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {
    single<DBService> { DBServiceImpl() }
    single<NuclearService> { NuclearServiceImpl(get()) }
}