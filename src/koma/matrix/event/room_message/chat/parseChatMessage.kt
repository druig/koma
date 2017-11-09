package koma.matrix.event.room_message.chat

import koma.matrix.event.message.ChatMessageType
import koma.matrix.event.room_message.ChatMessage
import koma.model.user.UserState
import java.util.*

fun parseChatMessage(sender: UserState, date: Date, content: Map<String, Any>)
        : ChatMessage {
    val msgtype = content["msgtype"] as String?
    val msg = when (ChatMessageType.fromString(msgtype!!)) {
        ChatMessageType.Text -> ChatMessage(sender, date, TextMsg(content["body"] as String? ?: "(unexpected empty text)"))
        ChatMessageType.Emote -> ChatMessage(sender, date, EmoteMsg(content["body"] as String? ?: ""))
        ChatMessageType.Image -> ChatMessage(sender, date, ImageMsg(content["body"] as String? ?: "",
                mxcurl = content["url"] as String? ?: ""))
        else -> ChatMessage(sender, date, TextMsg("<unexpected other kind of chat message>"))
    }
    return msg
}