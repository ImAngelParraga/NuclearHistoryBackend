package com.angelparraga.plugins

import com.angelparraga.services.db.*
import com.angelparraga.services.nuclear.NuclearService
import com.angelparraga.services.nuclear.NuclearServiceImpl
import com.angelparraga.services.stats.StatsService
import com.angelparraga.services.stats.StatsServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {

    install(Koin) {
        modules(appModule)
    }

}

val appModule = module {
    single<NuclearRunDAO> { MongoNuclearRunDAO() }
    single<NuclearService> { NuclearServiceImpl(get(), get()) }
    single { PartnerDAOImpl() }
    single<PartnerDAO> { PartnerDAOCacheImpl(get()) }
    single<UserDAO> { UserDAOImpl() }
    single<StatsService> { StatsServiceImpl(get(), get()) }
}