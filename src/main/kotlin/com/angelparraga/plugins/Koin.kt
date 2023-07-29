package com.angelparraga.plugins

import com.angelparraga.services.auth.PartnerDAO
import com.angelparraga.services.auth.PartnerDAOCacheImpl
import com.angelparraga.services.auth.PartnerDAOImpl
import com.angelparraga.services.db.MongoNuclearRunDAO
import com.angelparraga.services.db.NuclearRunDAO
import com.angelparraga.services.nuclear.NuclearService
import com.angelparraga.services.nuclear.NuclearServiceImpl
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.io.File

fun Application.configureKoin() {

    install(Koin) {
        val storagePath = environment.config.property("storage.ehcacheFilePath").getString()
        val appModule = module {
            single<NuclearRunDAO> { MongoNuclearRunDAO() }
            single<NuclearService> { NuclearServiceImpl(get()) }
            singleOf(::PartnerDAOImpl)
            single<PartnerDAO> {
                PartnerDAOCacheImpl(
                    get(),
                    File(storagePath)
                )
            }
        }

        modules(appModule)
    }

}

