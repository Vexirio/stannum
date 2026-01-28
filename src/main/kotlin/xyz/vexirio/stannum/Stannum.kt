package xyz.vexirio.stannum

import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class Stannum(private val plugin: JavaPlugin) : CommandExecutor {

    private var stannum: BukkitTask? = null

    fun startStannum() {
        if (stannum == null || stannum!!.isCancelled) {
            stannum = object : BukkitRunnable() {
                override fun run() {
                    if (!plugin.config.getBoolean("running")) {
                        cancel()
                        return
                    }

                    val particle = Particle.valueOf(plugin.config.getString("particle")!!)
                    val count = plugin.config.getInt("count")
                    val radius = plugin.config.getDouble("radius")

                    for (player in Bukkit.getOnlinePlayers()) {
                        player.spawnParticle(
                            particle,
                            player.eyeLocation,
                            count,
                            radius, radius, radius,
                            0.0
                        )
                    }
                }
            }.runTaskTimer(plugin, 0L, 5L)
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("stannum.admin")) {
            return true
        }
        val function = args.getOrNull(0) ?: run {
            sender.sendMessage("function?")
            return true
        }

        when (function) {
            "start" -> {
                plugin.config.set("running", true)
                plugin.saveConfig()
                startStannum()
            }

            "stop" -> {
                plugin.config.set("running", false)
                plugin.saveConfig()
                stannum?.cancel()
            }

            "edit" -> {
                val inputParticle = args.getOrNull(1)?.uppercase() ?: return true
                val particle = try {
                    Particle.valueOf(inputParticle)
                } catch (e: Exception) {
                    return true
                }
                val count = args.getOrNull(2)?.toIntOrNull() ?: return true
                val radius = args.getOrNull(3)?.toDoubleOrNull() ?: return true

                plugin.config.set("particle", particle.name)
                plugin.config.set("count", count)
                plugin.config.set("radius", radius)
                plugin.saveConfig()
                return true
            }
        }

        return true
    }
}
