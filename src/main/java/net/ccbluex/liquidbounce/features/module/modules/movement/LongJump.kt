package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.EnumAutoDisableType
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleInfo(name = "LongJump", category = ModuleCategory.MOVEMENT, autoDisable = EnumAutoDisableType.FLAG)
class LongJump : Module() {
    private val modeValue = ListValue("Mode", arrayOf("NCP", "NCPDamage", "NewBlocksmc", "HYT"), "NCP")
    private val ncpBoostValue = FloatValue("NCPBoost", 4.25f, 1f, 10f)
    private val ncpdInstantValue = BoolValue("NCPDamageInstant", false).displayable { modeValue.equals("NCPDamage") }

    // settings
    private val autoJumpValue = BoolValue("AutoJump", true)
    private val autoDisableValue = BoolValue("AutoDisable", true)
    private var jumped = false
    private var hasJumped = false
    private var canBoost = false
    private var teleported = false
    private var timer = MSTimer()
    var airTicks = 0
    private var balance = 0
    private var x = 0.0
    private var y = 0.0
    private var z = 0.0
    private var damageStat = false
    private val jumpYPosArr = arrayOf(
        0.41999998688698,
        0.7531999805212,
        1.00133597911214,
        1.16610926093821,
        1.24918707874468,
        1.24918707874468,
        1.1707870772188,
        1.0155550727022,
        0.78502770378924,
        0.4807108763317,
        0.10408037809304,
        0.0
    )

    override fun onEnable() {
        airTicks = 0
        balance = 0
        hasJumped = false
        damageStat = false
        if (modeValue.equals("ncpdamage")) {
            x = mc.thePlayer.posX
            y = mc.thePlayer.posY
            z = mc.thePlayer.posZ
            if (ncpdInstantValue.get()) {
                balance = 114514
            } else {
                LiquidBounce.hud.addNotification(
                    Notification(
                        name,
                        "Wait for damage...",
                        NotifyType.SUCCESS,
                        jumpYPosArr.size * 4 * 50
                    )
                )
            }
        } else if (modeValue.equals("NewBlocksmc")) {
            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ

            PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(x, y + 4, z, false))
            PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false))
            PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true))
            LiquidBounce.hud.addNotification(Notification(name, "Damage", NotifyType.SUCCESS))
        }
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer ?: return

        if (modeValue.equals("ncpdamage")) {
            if (!damageStat) {
                mc.thePlayer.setPosition(x, y, z)
                if (balance > jumpYPosArr.size * 4) {
                    repeat(4) {
                        jumpYPosArr.forEach {
                            PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(x, y + it, z, false))
                        }
                        PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false))
                    }
                    PacketUtils.sendPacketNoEvent(C03PacketPlayer(true))
                    damageStat = true
                }
            } else {
                MovementUtils.strafe(0.50f * ncpBoostValue.get())
                mc.thePlayer.jump()
                state = false
            }
            return
        }

        if (jumped) {
            val mode = modeValue.get()

            if (!mc.thePlayer.onGround) {
                airTicks++
            } else {
                airTicks = 0
            }

            if (mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying) {
                jumped = false

                if (mode.equals("NCP", ignoreCase = true)) {
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                }
                return
            }
            run {
                when (mode.lowercase()) {
                    "ncp" -> {
                        MovementUtils.strafe(MovementUtils.getSpeed() * if (canBoost) ncpBoostValue.get() else 0.5f)
                        canBoost = false
                    }

                    "newblocksmc" -> {
                        MovementUtils.strafe(1f)
                        mc.thePlayer.motionY += 0.0224514
                        mc.timer.timerSpeed *= 0.989f
                    }

                    "hyt" -> {
                        mc.thePlayer.motionY += 0.031470000997
                        MovementUtils.strafe(MovementUtils.getSpeed() * 1.0114514f)
                        mc.timer.timerSpeed = 1.0114514f
                    }
                }
            }
        }

        if (autoJumpValue.get() && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            jumped = true
            if (hasJumped && autoDisableValue.get()) {
                state = false
                return
            }
            mc.thePlayer.jump()
            hasJumped = true
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            if (modeValue.equals("NCPDamage") && !damageStat) {
                balance++
                event.cancelEvent()
            }
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent) {
        mc.thePlayer ?: return
        val mode = modeValue.get()

        if (mode.equals("ncp", ignoreCase = true) && !MovementUtils.isMoving() && jumped) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
            event.zeroXZ()
        }
    }

    @EventTarget(ignoreCondition = true)
    fun onJump(event: JumpEvent) {
        jumped = true
        canBoost = true
        teleported = false
        timer.reset()
    }

    override val tag: String
        get() = modeValue.get()
}