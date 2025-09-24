package network.warzone.limbo

import kotlinx.coroutines.runBlocking
import java.util.*

interface Cache<K, T> {
    fun get(id: K): T?
}

interface DynamicCache<T> : Cache<UUID, T> {
    fun add(id: UUID, obj: T)
    fun remove(id: UUID)
    suspend fun fetch(id: UUID): T?
    suspend fun getOrFetch(id: UUID): T? {
        return get(id) ?: fetch(id).also {
            if (it != null) {
                add(id, it)
            }
        }
    }
}

interface StaticCache<T> : Cache<UUID, T> {
    suspend fun refresh()
}

object RankCache : StaticCache<Rank> {
    private var cache: Map<UUID, Rank> = mapOf()

    override suspend fun refresh() {
        val r = DataFetcher.loadRanks()
        cache = r.associateBy { it._id }
    }

    override fun get(id: UUID): Rank? {
        return cache[id]
    }
}

object TagCache : StaticCache<Tag> {
    private var cache: Map<UUID, Tag> = mapOf()

    override suspend fun refresh() = runBlocking {
        val r = DataFetcher.loadTags()
        cache = r.associateBy { it._id }
    }

    override fun get(id: UUID): Tag? {
        return cache[id]
    }
}

object ProfileCache : DynamicCache<Profile> {
    private var cache: Map<UUID, Profile> = mutableMapOf()

    override fun add(id: UUID, obj: Profile) {
        cache += id to obj
    }

    override suspend fun fetch(id: UUID): Profile? = runBlocking {
        LimboPlugin.instance.logger.info("Fetching profile for $id")
        DataFetcher.getProfile(id)
    }

    override fun remove(id: UUID) {
        cache -= id
    }

    override fun get(id: UUID): Profile? {
        return cache[id]
    }
}

