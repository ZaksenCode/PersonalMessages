package me.zaksen.pms.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.context.CommandContext
import me.zaksen.pms.Pms
import me.zaksen.pms.data.IgnoreList
import me.zaksen.pms.data.MainConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.MessageArgumentType
import net.minecraft.server.command.ServerCommandSource

class PmCommand(
    private val ignoreList: IgnoreList,
    private val mainConfig: MainConfig
): Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        val from = context.source.player ?: return 0
        val to = EntityArgumentType.getPlayer(context, "player") ?: return 0
        val msg = MessageArgumentType.getMessage(context, "message") ?: return 0

        val fromName = from.nameForScoreboard
        val toName = to.nameForScoreboard

        val toIgnoreList = ignoreList.entries[toName]

        if(toIgnoreList != null && toIgnoreList.contains(fromName)) {
            // Send message only to sender.
            from.sendMessage(MiniMessage.miniMessage().deserialize(mainConfig.messageFormatFrom,
                Placeholder.unparsed("from-name", fromName),
                Placeholder.unparsed("to-name", toName),
                Placeholder.component("message", msg.asComponent())
            ))
            return SINGLE_SUCCESS
        }

        from.sendMessage(MiniMessage.miniMessage().deserialize(mainConfig.messageFormatFrom,
            Placeholder.unparsed("from-name", fromName),
            Placeholder.unparsed("to-name", toName),
            Placeholder.component("message", msg.asComponent())
        ))
        to.sendMessage(MiniMessage.miniMessage().deserialize(mainConfig.messageFormatTo,
            Placeholder.unparsed("from-name", fromName),
            Placeholder.unparsed("to-name", toName),
            Placeholder.component("message", msg.asComponent())
        ))

        Pms.setReplyPlayer(to, from)

        return SINGLE_SUCCESS
    }
}