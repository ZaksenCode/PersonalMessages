package me.zaksen.pms.data

import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MainConfig {
    @SerialName("message_format_from")
    @YamlComment(
        "Message, that sender will be received when use /pm",
        "Supported placeholders: <from-name>, <to-name>, <message>"
    )
    val messageFormatFrom: String = "<gray>[<aqua>you</aqua>] -> [<aqua><to-name></aqua>]: <white><message>"
    @SerialName("message_format_to")
    @YamlComment(
        "Message, that will be received when other player use /pm",
        "Supported placeholders: <from-name>, <to-name>, <message>"
    )
    val messageFormatTo: String = "<gray>[<aqua><from-name></aqua>] -> [<aqua>you</aqua>]: <white><message>"
    @SerialName("ignore_on_message")
    @YamlComment(
        "Supported placeholders: <player>"
    )
    val ignoreOnMessage: String = "<red>Now you will not receive personal messages from <player>"
    @SerialName("ignore_off_message")
    @YamlComment(
        "Supported placeholders: <player>"
    )
    val ignoreOffMessage: String = "<green>Now you will receive personal messages from <player>"
}