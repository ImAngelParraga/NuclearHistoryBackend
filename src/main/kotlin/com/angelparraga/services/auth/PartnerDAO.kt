package com.angelparraga.services.auth

import com.angelparraga.services.db.DatabaseFactory
import com.angelparraga.services.db.DatabaseFactory.dbQuery
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull

data class Partner(val partnerId: String, val password: String)

interface PartnerDAO {
    suspend fun getPartner(partnerId: String): Partner?
}

class PartnerDAOImpl() : PartnerDAO {
    private val partnerCollection = DatabaseFactory.dbNTHistory.getCollection<Partner>("partners")

    override suspend fun getPartner(partnerId: String): Partner? = dbQuery {
        partnerCollection.find(eq(Partner::partnerId.name, partnerId)).firstOrNull()
    }

}