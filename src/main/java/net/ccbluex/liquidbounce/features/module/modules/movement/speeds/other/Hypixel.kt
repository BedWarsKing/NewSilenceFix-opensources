package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils

class Hypixel : SpeedMode("Hypixel") {
    override fun onEnable() {}
    override fun onMove(event: MoveEvent) {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava && !mc.thePlayer.isInWater && !mc.thePlayer.isOnLadder && mc.thePlayer.ridingEntity == null) {
            if (MovementUtils.isMoving()) {
                mc.gameSettings.keyBindJump.pressed = false
                if (mc.thePlayer.onGround) {
                    MovementUtils.strafe(0.3154f)
                    mc.thePlayer.speedInAir = 0.0221f
                    mc.thePlayer.jump()
                    mc.thePlayer.motionY = 0.032
                    ClientUtils.displayChatMessage("MotionY:" + mc.thePlayer.motionY.toString())
                    event.y = 0.42
                }
            }
        }
    }

    @EventTarget
    fun onJump(event: JumpEvent) {
        event.cancelEvent()
    }

    override fun onDisable() {
        mc.thePlayer.speedInAir = 0.02f
        MovementUtils.strafe()
    }
}
