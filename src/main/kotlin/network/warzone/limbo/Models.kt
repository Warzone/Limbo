package network.warzone.limbo

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.UUID



data class Rank(
    val _id: UUID,
    val name: String,
    val nameLower: String,
    val displayName: String,
    val prefix: String?,
    val priority: Int,
    val permissions: List<String>,
    val staff: Boolean,
    val applyOnJoin: Boolean,
    val createdAt: Long
)

data class Tag(
    val _id: UUID,
    val name: String,
    val nameLower: String,
    val display: String,
    val createdAt: Long
) {
    fun formatted() = LegacyComponentSerializer.legacyAmpersand().deserialize(display)
}

data class Profile(
    val _id: UUID,
    val name: String,
    val firstJoinedAt: Long,
    val lastJoinedAt: Long,
    val rankIds: List<UUID> = listOf(),
    val activeTagId: UUID?
) {
    fun activeTag() = activeTagId?.let { TagCache.get(it) }
    fun ranks() = rankIds.mapNotNull { RankCache.get(it) }

    fun prefix(): Component? {
        val rank: Rank? = ranks()
            .filter { it.prefix != null }
            .maxByOrNull { it.priority }

        rank ?: return null

        return rank.prefix?.let {
            LegacyComponentSerializer.legacyAmpersand().deserialize(it)
            .append(text(" ").decoration(TextDecoration.BOLD, false))
        }
    }
    fun suffix(): Component? {
        val tag = activeTag()
        val formatted = tag?.formatted()
        return formatted?.let {
            text(" [", NamedTextColor.GRAY)
                .decoration(TextDecoration.BOLD, false)
                .append(it)
                .append(
                    text("]", NamedTextColor.GRAY)
                    .decoration(TextDecoration.BOLD, false)
                )
        }
    }
}