package xyz.vexirio.stannum

import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class StannumTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        if (args.size == 1) return listOf("start", "stop", "edit")
        if (args.size == 2) {
            return Particle.entries
                .map { it.name }
                .filter { it.startsWith(args[1].uppercase()) }
        }

        if (args.size == 3) return listOf("10", "25", "50", "100", "250", "500")
        if (args.size == 4) return listOf("1", "10", "25", "50")

        return emptyList()
    }
}
