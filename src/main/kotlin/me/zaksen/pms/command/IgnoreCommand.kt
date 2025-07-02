package me.zaksen.pms.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import me.zaksen.pms.Pms
import me.zaksen.pms.data.IgnoreList
import me.zaksen.pms.data.MainConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource

class IgnoreCommand(
    private val mod: Pms,
    private val mainConfig: MainConfig,
    private val ignoreList: IgnoreList
): Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        val who = context.source.player ?: return 0
        val ignore = EntityArgumentType.getPlayer(context, "player") ?: return 0

        val whoName = who.nameForScoreboard
        val ignoreName = ignore.nameForScoreboard

        val whoIgnoreList = ignoreList.entries[whoName] ?: mutableSetOf()

        if(whoIgnoreList.contains(ignoreName)) {
            whoIgnoreList.remove(ignoreName)
            who.sendMessage(MiniMessage.miniMessage().deserialize(mainConfig.ignoreOffMessage,
                Placeholder.unparsed("player", ignoreName)
            ))
        } else {
            whoIgnoreList.add(ignoreName)
            who.sendMessage(MiniMessage.miniMessage().deserialize(mainConfig.ignoreOnMessage,
                Placeholder.unparsed("player", ignoreName)
            ))
        }

        ignoreList.entries[whoName] = whoIgnoreList

        mod.saveData()

        return 0;
    }
}