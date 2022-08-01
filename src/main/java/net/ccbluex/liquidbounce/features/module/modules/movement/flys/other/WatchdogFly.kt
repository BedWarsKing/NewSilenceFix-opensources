package net.ccbluex.liquidbounce.features.module.modules.movement.flys.other

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.network.play.client.C03PacketPlayer

class WatchdogFly : FlyMode("Watchdog") {
    private var flytimer = MSTimer()

    override fun onPacket(Event: PacketEvent) {
        val packet = Event.packet
        val playerPacket = packet as C03PacketPlayer
        playerPacket.onGround = true
    }

    override fun onUpdate(Event: UpdateEvent) {
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionZ = 0.0
        val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
        val x = -Math.sin(yaw) * 6
        val z = Math.cos(yaw) * 6
        if (flytimer.hasTimePassed(1000)) {
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY - 2f, mc.thePlayer.posZ + z)
            flytimer.reset()
        }
    }
}