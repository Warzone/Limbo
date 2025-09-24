package network.warzone.limbo

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

object DataFetcher {

    private val GSON_CFG : GsonBuilder.() -> Unit = {
        registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
            Date(json.asJsonPrimitive.asLong)
        })
        registerTypeAdapter(Date::class.java, JsonSerializer<Date> { date, _, _ ->
            JsonPrimitive(date.time)
        })
    }

    val client: HttpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer(GSON_CFG)
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
    lateinit var baseUrl: String

    fun configure(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    suspend inline fun <reified T> get(url: String): T {
        return client.get(baseUrl + url)
    }

    suspend fun loadRanks(): List<Rank> {
        val ranks = get<List<Rank>>("/mc/ranks")
        return ranks
    }

    suspend fun loadTags(): List<Tag> {
        val tags = get<List<Tag>>("/mc/tags")
        return tags
    }

    suspend fun getProfile(uuid: UUID): Profile? {
        try {
            val profile = get<Profile?>("/mc/players/$uuid")
            return profile
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) {
                return null
            }
            throw e
        }
    }

}