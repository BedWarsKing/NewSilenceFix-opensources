package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.blocksmc

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class BlocksmcLong : SpeedMode("BlocksmcLong") {
    override fun onPreMotion() {
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) {
                MovementUtils.strafe(1.1f)
                mc.thePlayer.motionY = 0.41999999801
            } else {
                MovementUtils.strafe(0.87f)
            }
        }
    }
}