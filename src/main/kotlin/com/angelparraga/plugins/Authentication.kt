package com.angelparraga.plugins

import com.angelparraga.services.db.PartnerDAO
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.koin.ktor.ext.inject

fun Application.authentication() {
    val partnerDAO by inject<PartnerDAO>()

    install(Authentication) {
        basic {
            realm = "All"
            validate { credentials ->
                validate(credentials, partnerDAO)
            }
        }
    }
}

private suspend fun validate(credentials: UserPasswordCredential, partnerDAO: PartnerDAO): Principal? {
    val partner = partnerDAO.getPartner(credentials.name) ?: return null
    return if (partner.password == credentials.password) UserIdPrincipal(credentials.name) else null
}