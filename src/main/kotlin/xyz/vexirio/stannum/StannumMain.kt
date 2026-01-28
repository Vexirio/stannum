package xyz.vexirio.stannum

import org.bukkit.plugin.java.JavaPlugin

class StannumMain : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        val stannumCommand = Stannum(this)
        getCommand("stannum")?.setExecutor(stannumCommand)
        getCommand("stannum")?.tabCompleter = StannumTabCompleter()
        if (config.getBoolean("running")) {
            stannumCommand.startStannum()
        }
    }

    override fun onDisable() {}
}
