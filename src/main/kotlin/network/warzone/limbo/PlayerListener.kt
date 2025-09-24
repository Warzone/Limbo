package network.warzone.limbo

import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin


object PlayerListener : Listener {

    lateinit var plugin: Plugin

    fun register(plugin: Plugin) {
        this.plugin = plugin
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLoginLast(event: AsyncPlayerPreLoginEvent) {
        if (event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) return
        try {
            runBlocking {
                val profile = ProfileCache.getOrFetch(event.uniqueId)
                if (profile == null) {
                    plugin.logger.info("Could not load profile for ${event.name}.")
                } else {
                    plugin.logger.info("Loaded profile for ${event.name} - ${profile.rankIds.size} ranks")
                }
            }
        } catch (ex: Exception) {
            plugin.logger.severe("Failed to load profile for ${event.name}")
            ex.printStackTrace()
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) = runBlocking {
        val player = event.player
        PlayerUtil.setDisplayName(event.player)
        event.joinMessage((text(player.name, NamedTextColor.GRAY)).append(text(" has joined.", NamedTextColor.GRAY)))
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        event.quitMessage((text(player.name, NamedTextColor.GRAY)).append(text(" has left.", NamedTextColor.GRAY)))
        ProfileCache.remove(player.uniqueId)
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) = runBlocking {
        val message = text()
            .append(event.player.displayName().colorIfAbsent(NamedTextColor.WHITE))
            .append(text(": ", NamedTextColor.GRAY))
            .append(event.message().colorIfAbsent(NamedTextColor.WHITE))
            .build()
        event.renderer { _, _, _, _ -> message }
    }

}