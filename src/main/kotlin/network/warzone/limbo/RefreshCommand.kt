package network.warzone.limbo

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class RefreshCommand {

    @Command("refresh")
    @CommandDescription("Refresh the cache")
    @Permission("limbo.refresh")
    fun refresh(sender: CommandSender) = LimboPlugin.async {
        try {
            RankCache.refresh()
            TagCache.refresh()
            Bukkit.getOnlinePlayers().forEach { PlayerUtil.setDisplayName(it) }
            sender.sendMessage(text("Cache refreshed.", NamedTextColor.GREEN))
        } catch (ex: Exception) {
            sender.sendMessage(text("Failed to refresh cache.", NamedTextColor.RED))
            ex.printStackTrace()
        }

    }

}