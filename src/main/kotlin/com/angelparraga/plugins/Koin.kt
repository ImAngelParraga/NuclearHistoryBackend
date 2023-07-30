package com.angelparraga.plugins

import com.angelparraga.services.auth.PartnerDAO
import com.angelparraga.services.auth.PartnerDAOCacheImpl
import com.angelparraga.services.auth.PartnerDAOImpl
import com.angelparraga.services.db.MongoNuclearRunDAO
import com.angelparraga.services.db.NuclearRunDAO
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
    single<NuclearRunDAO> { MongoNuclearRunDAO() }
    single<NuclearService> { NuclearServiceImpl(get()) }
    single { PartnerDAOImpl() }
    single<PartnerDAO> { PartnerDAOCacheImpl(get()) }
}