package network.warzone.limbo

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.meta.CommandMeta
import org.incendo.cloud.paper.LegacyPaperCommandManager

class CommandGraph(
    plugin: JavaPlugin
) {

    private val manager: LegacyPaperCommandManager<CommandSender> = LegacyPaperCommandManager.createNative(
        plugin,
        ExecutionCoordinator.asyncCoordinator()
    )
    private val annotationParser: AnnotationParser<CommandSender> = AnnotationParser(manager, CommandSender::class.java) {
        CommandMeta.empty()
    }

    init {
        registerCommand(RefreshCommand())
    }

    private fun registerCommand(obj: Any) {
        annotationParser.parse(obj)
    }

}