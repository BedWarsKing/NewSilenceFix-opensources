/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S0BPacketAnimation
import net.minecraft.stats.StatList

@ModuleInfo(name = "Criticals", category = ModuleCategory.COMBAT)
class Criticals : Module() {

    val modeValue = ListValue(
        "Mode",
        arrayOf(
            "NewSilenceFix",
            "vulcanfake",
            "NewPacket",
            "Lite",
            "Super",
            "Hypixel",
            "Hypixel2",
            "Novoline",
            "Blocksmc",
            "Blocksmc2",
            "Verus",
            "AAC4",
            "AAC5",
            "NoGround",
            "Minemora",
            "Motion"
        ),
        "packet"
    )
    private val motionValue =
        ListValue("MotionMode", arrayOf("Hop", "Jump", "LowJump"), "Jump").displayable { modeValue.equals("Motion") }
    val delayValue = IntegerValue("Delay", 0, 0, 500)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val lookValue = BoolValue("SendC06", false)
    private val debugValue = BoolValue("CriticalMessage", false)
    private val debugDelayValue = IntegerValue("DebugDelay", 1000, 500, 5000)

    val msTimer = MSTimer()

    private var target = 0
    private var jState = 0
    private var counter = 0
    private var readyCrit = false

    override fun onEnable() {
        if (modeValue.equals("NoGround")) {
            mc.thePlayer.jump()
        }
        jState = 0
        counter = 0
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            val entity = event.targetEntity
            target = entity.entityId

            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb || mc.thePlayer.isInWater ||
                mc.thePlayer.isInLava || mc.thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                LiquidBounce.moduleManager[Fly::class.java]!!.state || !msTimer.hasTimePassed(delayValue.get().toLong())
            ) {
                return
            }
            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ
            fun sendCriticalPacket(
                xOffset: Double = 0.0,
                yOffset: Double = 0.0,
                zOffset: Double = 0.0,
                ground: Boolean
            ) {
                val x = mc.thePlayer.posX + xOffset
                val y = mc.thePlayer.posY + yOffset
                val z = mc.thePlayer.posZ + zOffset
                if (lookValue.get()) {
                    mc.netHandler.addToSendQueue(
                        C06PacketPlayerPosLook(
                            x,
                            y,
                            z,
                            mc.thePlayer.rotationYaw,
                            mc.thePlayer.rotationPitch,
                            ground
                        )
                    )
                } else {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, ground))
                }
            }

            when (modeValue.get().lowercase()) {
                "vulcanfake" -> {
                    val motionX: Double
                    val motionZ: Double
                    if (MovementUtils.isMoving()) {
                        motionX = mc.thePlayer.motionX
                        motionZ = mc.thePlayer.motionZ
                    } else {
                        motionX = 0.00
                        motionZ = 0.00
                    }
                    mc.thePlayer.triggerAchievement(StatList.jumpStat)
                    sendCriticalPacket(xOffset = motionX / 3, yOffset = 0.20000004768372, zOffset = motionZ / 3, ground = false)
                    sendCriticalPacket(xOffset = motionX / 1.5, yOffset = 0.12160004615784, zOffset = motionZ / 1.5, ground = false)
                }
                "newpacket" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = true)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                }
                "newsiiencefix" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = true)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                }
                "lite" -> {
                    sendCriticalPacket(yOffset = 0.015626, ground = false)
                    sendCriticalPacket(yOffset = 0.00000000343, ground = false)
                }

                "super" -> {
                    sendCriticalPacket(yOffset = 0.11, ground = false)
                    sendCriticalPacket(yOffset = 0.062500013579, ground = false)
                    sendCriticalPacket(yOffset = 0.1100013579, ground = false)
                    sendCriticalPacket(yOffset = -4.33E-7, ground = false)
                }

                "aac4" -> {
                    sendCriticalPacket(yOffset = 0.042487, ground = false)
                    sendCriticalPacket(yOffset = 0.0104649713461000007, ground = false)
                    sendCriticalPacket(yOffset = 0.0014749900000101, ground = false)
                    sendCriticalPacket(yOffset = 0.0000007451816400000, ground = false)
                }

                "aac5" -> {
                    sendCriticalPacket(yOffset = 0.00133545, ground = false)
                    sendCriticalPacket(yOffset = -0.000000433, ground = false)
                }

                "hypixel" -> {
                    sendCriticalPacket(yOffset = 0.0309153423432, ground = false)
                    sendCriticalPacket(yOffset = 0.0270513534534, ground = false)
                    sendCriticalPacket(yOffset = 0.0170517235445, ground = false)
                    sendCriticalPacket(yOffset = 0.0021122543454, ground = false)
                    if (msTimer.hasTimePassed(debugDelayValue.get().toLong()))
                        chat("Trying send packet...")
                }

                "hypixel2" -> {
                    sendCriticalPacket(yOffset=0.05250000001304,ground = false)
                    sendCriticalPacket(yOffset=0.00150000001304,ground = false)
                }

                "novoline" -> {
                    sendCriticalPacket(yOffset = 0.11921599284565, ground = false)
                    sendCriticalPacket(yOffset = 0.00163166800276, ground = false)
                    sendCriticalPacket(yOffset = 0.15919999545217, ground = false)
                    sendCriticalPacket(yOffset = 0.11999999731779, ground = false)
                }

                "blocksmc" -> {
                    sendCriticalPacket(yOffset = 0.0425, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.001400000001304, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "blocksmc2" -> {
                    sendCriticalPacket(yOffset = 0.00625, ground = true)
                    sendCriticalPacket(ground = false)
                    sendCriticalPacket(yOffset = 1.1E-6, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "verus" -> {
                    counter++
                    if (counter == 1) {
                        sendCriticalPacket(yOffset = 0.001, ground = true)
                        sendCriticalPacket(ground = false)
                    }

                    if (counter >= 5) {
                        counter = 0
                    }
                }

                "minemora" -> {
                    sendCriticalPacket(yOffset = 0.0114514, ground = false)
                    sendCriticalPacket(yOffset = 0.0010999999940395355, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.0012016413, ground = false)
                }

                "motion" -> {
                    when (motionValue.get().lowercase()) {
                        "jump" -> mc.thePlayer.motionY = 0.42
                        "lowjump" -> mc.thePlayer.motionY = 0.3425
                        "hop" -> {
                            mc.thePlayer.motionY = 0.1
                            mc.thePlayer.fallDistance = 0.1f
                            mc.thePlayer.onGround = false
                        }
                    }
                }
            }
            readyCrit = true
            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            when (modeValue.get().lowercase()) {
                "noground" -> packet.onGround = false

                "edit" -> {
                    if (readyCrit) {
                        packet.onGround = false
                    }
                    readyCrit = false
                }
            }
        }
        if (packet is S0BPacketAnimation && debugValue.get()) {
            if (packet.animationType == 4 && packet.entityID == target) {
                alert("CRITICAL HIT")
            }
        }
    }

    override val tag: String
        get() = modeValue.get()
}
