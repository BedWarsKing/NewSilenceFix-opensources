package net.ccbluex.liquidbounce.features.module.modules.misc
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.S02PacketChat
@ModuleInfo(name = "AutoGG", category = ModuleCategory.MISC)
class AutoGG : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Hypixel", "HuaYuTing"), "Hypixel")
    private val messageValue = TextValue("GGMessage", "GG")
    private val strings = arrayOf(
        "1st Killer - ",
        "1st Place - ",
        "You died! Want to play again? Click here!",
        "You won! Want to play again? Click here!",
        " - Damage Dealt - ",
        "1st - ",
        "Winning Team - ",
        "Winners: ",
        "Winner: ",
        "Winning Team: ",
        " win the game!",
        "1st Place: ",
        "Last team standing!",
        "Winner #1 (",
        "Top Survivors",
        "Winners - "
    )
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S02PacketChat) {
            val string = packet.chatComponent.unformattedText
            when (modeValue.get().lowercase()) {
                "hypixel" -> {
                    for (s in strings) {
                        if (string.contains(s)) {
                            if (string.contains(strings[3])) sendGG()
                            break
                        }
                    }
                }
                "huayuting" -> {
                    if (string.contains("      喜欢      一般      不喜欢", true)) {
                        sendGG()
                    }
                }
            }
        }
    }
    private fun sendGG() {
        mc.thePlayer.sendChatMessage(messageValue.get())
    }
}