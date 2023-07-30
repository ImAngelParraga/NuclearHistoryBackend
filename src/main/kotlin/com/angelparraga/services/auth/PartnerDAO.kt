package com.angelparraga.services.auth

import com.angelparraga.services.db.DatabaseFactory
import com.angelparraga.services.db.DatabaseFactory.dbQuery
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import java.io.Serializable

data class Partner(val partnerId: String, val password: String) : Serializable

interface PartnerDAO {
    suspend fun getPartner(partnerId: String): Partner?
}

class PartnerDAOImpl : PartnerDAO {
    private val partnerCollection = DatabaseFactory.dbNTHistory.getCollection<Partner>("partners")

    override suspend fun getPartner(partnerId: String): Partner? = dbQuery {
        partnerCollection.find(eq(Partner::partnerId.name, partnerId)).firstOrNull()
    }

}

class PartnerDAOCacheImpl(private val partnerDAO: PartnerDAOImpl) : PartnerDAO {
    private val cacheManager = newCacheManagerBuilder()
        .withCache(
            "partnersCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.java,
                Partner::class.java,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(10, EntryUnit.ENTRIES)
                    .offheap(1, MemoryUnit.MB)
            ).build()
        ).build(true)

    private val partnersCache =
        cacheManager.getCache("partnersCache", String::class.java, Partner::class.java)

    override suspend fun getPartner(partnerId: String): Partner? =
        partnersCache[partnerId] ?: partnerDAO.getPartner(partnerId)
            .also { partner -> partnersCache.put(partnerId, partner) }

}