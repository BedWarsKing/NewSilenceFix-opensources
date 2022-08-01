package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.exploit.disabler.Hypixel
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import kotlin.math.abs

class HypixelSlow: SpeedMode("HypixelSlow") {
    override fun onMove(event: MoveEvent) {
        if (mc.thePlayer.onGround) mc.thePlayer.jump()
        if (mc.gameSettings.keyBindJump.pressed) {
            if ((mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindLeft.pressed) && abs(
                    Hypixel.yawDiff) > 20) {
                MovementUtils.strafe(MovementUtils.getSpeed() * 0.85f);
            } else {
                MovementUtils.strafe();
            }
        }
    }
}