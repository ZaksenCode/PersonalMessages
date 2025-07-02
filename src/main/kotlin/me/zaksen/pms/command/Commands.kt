package me.zaksen.pms.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.zaksen.pms.Pms
import me.zaksen.pms.data.IgnoreList
import me.zaksen.pms.data.MainConfig
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.MessageArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object Commands {

    private lateinit var ignoreCommand: IgnoreCommand
    private lateinit var replyCommand: ReplyCommand
    private lateinit var pmCommand: PmCommand

    fun setup(mod: Pms) {
        ignoreCommand = IgnoreCommand(mod, mod.mainConfig, mod.ignoreList)
        replyCommand = ReplyCommand(mod.ignoreList, mod.mainConfig)
        pmCommand = PmCommand(mod.ignoreList, mod.mainConfig)
    }

    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            dispatcher.register(buildIgnoreCommand())

            val reply = dispatcher.register(buildReplyCommand())
            dispatcher.register(CommandManager.literal("/r").redirect(reply))

            dispatcher.register(buildPmCommand())
        }
    }

    private fun buildIgnoreCommand(): LiteralArgumentBuilder<ServerCommandSource?>? {
        return CommandManager.literal("ignore")
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes(ignoreCommand::run))
    }

    private fun buildReplyCommand(): LiteralArgumentBuilder<ServerCommandSource?>? {
        return CommandManager.literal("reply")
            .then(CommandManager.argument("message", MessageArgumentType.message())
            .executes(replyCommand::run))
    }

    private fun buildPmCommand(): LiteralArgumentBuilder<ServerCommandSource?>? {
        return CommandManager.literal("pm").then(CommandManager.argument("player", EntityArgumentType.player())
            .then(CommandManager.argument("message", MessageArgumentType.message())
                .executes(pmCommand::run)))
    }
}