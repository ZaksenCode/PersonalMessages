package me.zaksen.pms

import me.zaksen.pms.command.Commands
import me.zaksen.pms.data.IgnoreList
import me.zaksen.pms.data.MainConfig
import me.zaksen.pms.util.YamlLoader
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.network.ServerPlayerEntity

class Pms : ModInitializer {

    private val yamlLoader = YamlLoader()
    lateinit var mainConfig: MainConfig
    lateinit var ignoreList: IgnoreList

    override fun onInitialize() {
        loadData()
        Commands.setup(this)
        Commands.register()
    }

    private fun loadData() {
        mainConfig = yamlLoader.loadData<MainConfig>(FabricLoader.getInstance().configDir.toFile(), "personal_messages.yml")
        ignoreList = yamlLoader.loadData<IgnoreList>(FabricLoader.getInstance().gameDir.toFile(), "ignore_list.yml")
    }

    fun saveData() {
        yamlLoader.saveData<IgnoreList>(FabricLoader.getInstance().gameDir.toFile(), "ignore_list.yml", ignoreList)
    }

    companion object {
        private val replyCache: MutableMap<ServerPlayerEntity, ServerPlayerEntity> = mutableMapOf()

        fun getReplyPlayer(forPlayer: ServerPlayerEntity): ServerPlayerEntity? {
            return replyCache[forPlayer]
        }

        fun setReplyPlayer(forPlayer: ServerPlayerEntity, reply: ServerPlayerEntity) {
            replyCache[forPlayer] = reply
        }
    }
}