package network.warzone.limbo

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object PlayerUtil {
    suspend fun setDisplayName(player: Player) {
        val profile = ProfileCache.getOrFetch(player.uniqueId)
        val name = player.name
        val prefix = profile?.prefix() ?: text("")
        val suffix = profile?.suffix() ?: text("")

        player.displayName(
            prefix
                .append(
                    text(name, NamedTextColor.GRAY)
                        .decoration(TextDecoration.BOLD, false)
                )
                .append(suffix)
        )
    }
}