package network.warzone.limbo

import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LimboPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: LimboPlugin
            private set

        fun async(block: suspend () -> Unit) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, Runnable {
                runBlocking {
                    block()
                }
            })
        }
    }

    private lateinit var commandGraph: CommandGraph

    override fun onEnable() {
        instance = this
        val url = this.config.getString("api.url", "https://api.warz.one")!!
        DataFetcher.configure(url)
        commandGraph = CommandGraph(this)
        populateCache()
        PlayerListener.register(this)
    }

    override fun onDisable() {
    }

    private fun populateCache() = runBlocking {
        RankCache.refresh()
        TagCache.refresh()
    }
}
