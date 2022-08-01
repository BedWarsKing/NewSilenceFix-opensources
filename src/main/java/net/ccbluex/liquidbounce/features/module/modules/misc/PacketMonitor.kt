package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "PacketMonitor", category = ModuleCategory.MISC)
class PacketMonitor : Module() {
    private val warnValue = BoolValue("HighWarning", true)

    private var packets = 0
    private var packet = 0
    private var ticks = 0

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (event.packet.javaClass.simpleName.contains("C")) packets += 1
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (ticks >= 20) {
            ticks = 0
            packet = packets
            if (packet > 500 && warnValue.get()) {
                ClientUtils.displayChatMessage("§e[PacketMonitor] §cSending TOO MANY packets")
            }
            packets = 0
        }

        ticks += 1
    }

    override val tag: String
        get() = packet.toString()
}