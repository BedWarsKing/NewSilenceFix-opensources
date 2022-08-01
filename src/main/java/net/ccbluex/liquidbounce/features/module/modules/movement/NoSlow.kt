
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.item.*
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import java.util.*
import kotlin.math.sqrt


@ModuleInfo(name = "NoSlowDown",
    category = ModuleCategory.MOVEMENT)
class NoSlow : Module() {
    private val Timer = MSTimer()
    private val modeValue = ListValue("PacketMode", arrayOf("AntiCheat","Custom","Hypixel","NoCheatPlus","NoPacket","AAC","AAC5", "Matrix", "Vulcan", "Medusa", "WatchDog"), "AntiCheat")
    private val teleportModeValue = ListValue("TeleportMode", arrayOf("Vanilla", "VanillaNoSetback", "Custom", "Decrease"), "Vanilla")
    private val StopSpring = ListValue("StopSprint", arrayOf("Off","Real","Spoof"),"Off")

    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val customOnGround = BoolValue("CustomOnGround", false)
    private val customDelayValue = IntegerValue("CustomDelay",60,10,200)

    private val teleportValue = BoolValue("Teleport", false)
    private val teleportNoApplyValue = BoolValue("TeleportNoApply", false)
    private val teleportCustomSpeedValue = FloatValue("Teleport-CustomSpeed", 0.13f, 0f, 1f)
    private val teleportCustomYValue = BoolValue("Teleport-CustomY", false)
    private val teleportDecreasePercentValue = FloatValue("Teleport-DecreasePercent", 0.13f, 0f, 1f)
    private val testValue = BoolValue("WatchDog-SendPacket", false)

    val soulsandValue = BoolValue("Soulsand", true)
    private val alert1Value = BoolValue("updateAlert", true)

    private val alertTimer = MSTimer()
    private var pendingFlagApplyPacket = false
    private var lastMotionX = 0.0
    private var lastMotionY = 0.0
    private var lastMotionZ = 0.0
    private val msTimer = MSTimer()
    private var sendBuf = false
    private var packetBuf = LinkedList<Packet<INetHandlerPlayServer>>()
    private var nextTemp = false
    private var waitC03 = false
    private var lastBlockingStat = false

    private fun OnPre(event : MotionEvent): Boolean {
        return event.eventState == EventState.PRE
    }

    private fun OnPost(event : MotionEvent): Boolean {
        return event.eventState == EventState.POST
    }

    override fun onDisable() {
        Timer.reset()
        msTimer.reset()
        pendingFlagApplyPacket = false
        sendBuf = false
        packetBuf.clear()
        nextTemp = false
        waitC03 = false
    }

    private fun sendPacket(Event : MotionEvent,SendC07 : Boolean, SendC08 : Boolean,Delay : Boolean,DelayValue : Long,onGround : Boolean,Hypixel : Boolean = false) {
        val aura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1,-1,-1), EnumFacing.DOWN)
        val blockPlace = C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem())
        val blockMent = C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f)
        if(onGround && !mc.thePlayer.onGround) {
            return
        }
        if(SendC07 && OnPre(Event)) {
            if(Delay && Timer.hasTimePassed(DelayValue)) {
                mc.netHandler.addToSendQueue(digging)
            } else if(!Delay) {
                mc.netHandler.addToSendQueue(digging)
            }
        }
        if(SendC08 && OnPost(Event)) {
            if(Delay && Timer.hasTimePassed(DelayValue) && !Hypixel) {
                mc.netHandler.addToSendQueue(blockPlace)
                Timer.reset()
            } else if(!Delay && !Hypixel) {
                mc.netHandler.addToSendQueue(blockPlace)
            } else if(Hypixel) {
                mc.netHandler.addToSendQueue(blockMent)
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (alertTimer.hasTimePassed(10000) && alert1Value.get() && (modeValue.equals("Matrix") || modeValue.equals("Vulcan"))) {
            alertTimer.reset()
            ClientUtils.displayChatMessage("§8[§c§lNoSlow§8]§aPlease notice that Vulcan/Matrix NoSlow §cDO NOT §asupport FakeLag Disabler!")
            ClientUtils.displayChatMessage("§8[§c§lNoSlow§8]§aType .noslow updateAlert1 to disable this notice!")
        }

        if (!MovementUtils.isMoving())
            return

        val heldItem = mc.thePlayer.heldItem
        val killAura = LiquidBounce.moduleManager[KillAura::class.java]!! as KillAura

        if (modeValue.get().equals("watchdog", true)) {
            if (testValue.get() && (!killAura.state || !killAura.blockingStatus)
                && event.eventState == EventState.PRE
                && mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse().getItem() != null) {
                val item = mc.thePlayer.getItemInUse().getItem()
                if (mc.thePlayer.isUsingItem && (item is ItemFood || item is ItemBucketMilk || item is ItemPotion) && mc.thePlayer.getItemInUseCount() >= 1)
                    PacketUtils.sendPacketNoEvent(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
            }
        }

        if(mc.thePlayer.isUsingItem && heldItem != null && heldItem.item !is ItemSword){
            when(modeValue.get().toLowerCase()) {
                "aac5" -> {
                    if (OnPost(event)) {
                        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                    }
                }
            }
        }else{
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            if (!mc.thePlayer.isBlocking && !killAura.blockingStatus) {
                return
            }

            when(modeValue.get().toLowerCase()) {
                "anticheat" -> {
                    this.sendPacket(event,true,false,false,0,false,false)
                    if(mc.thePlayer.ticksExisted % 2 == 0) {
                        this.sendPacket(event,false,true,false,0,false)
                    }
                }

                "aac" -> {
                    if(mc.thePlayer.ticksExisted % 3 == 0) {
                        sendPacket(event, true , false, false, 0, false)
                    } else {
                        sendPacket(event, false , true, false, 0, false)
                    }
                }

                "custom" -> {
                    sendPacket(event,true,true,true,customDelayValue.get().toLong(),customOnGround.get())
                }

                "nocheatplus" -> {
                    sendPacket(event,true,true,false,0,false)
                }

                "aac5" -> {
                    sendPacket(event,true,true,true,200,false)
                }
            }
        }
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer.heldItem?.item

        when (StopSpring.get()) {
            "Real" -> {
                mc.thePlayer.isSprinting = false
            }
        }

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)

    }

    private fun getMultiplier(item: Item?, isForward: Boolean) = when (item) {
        is ItemFood, is ItemPotion, is ItemBucketMilk -> {
            if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
        }
        is ItemSword -> {
            if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
        }
        is ItemBow -> {
            if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
        }
        else -> 0.2F
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if((modeValue.equals("Matrix") || modeValue.equals("Vulcan")) && (lastBlockingStat || isBlocking)) {
            if(msTimer.hasTimePassed(230) && nextTemp) {
                nextTemp = false
                PacketUtils.sendPacketNoEvent(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN))
                if(packetBuf.isNotEmpty()) {
                    var canAttack = false
                    for(packet in packetBuf) {
                        if(packet is C03PacketPlayer) {
                            canAttack = true
                        }
                        if(!((packet is C02PacketUseEntity || packet is C0APacketAnimation) && !canAttack)) {
                            PacketUtils.sendPacketNoEvent(packet)
                        }
                    }
                    packetBuf.clear()
                }
            }
            if(!nextTemp) {
                lastBlockingStat = isBlocking
                if (!isBlocking) {
                    return
                }
                PacketUtils.sendPacketNoEvent(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
                nextTemp = true
                waitC03 = modeValue.equals("Vulcan")
                msTimer.reset()
            }
        }
        if(modeValue.get().equals("Hypixel", true)){
            if(isBlocking){
                PacketUtils.sendPacketNoEvent(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
            }
        }

    }

    private val isBlocking: Boolean
        get() = (mc.thePlayer.isUsingItem || (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).blockingStatus) && mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (modeValue.get() == "Medusa" || StopSpring.get().equals("Spoof")) {
            if (mc.thePlayer.isUsingItem || mc.thePlayer.isBlocking) {
                PacketUtils.sendPacketNoEvent(C0BPacketEntityAction(mc.thePlayer,C0BPacketEntityAction.Action.STOP_SPRINTING))
            }
        }

        if((modeValue.equals("Matrix") || modeValue.equals("Vulcan")) && nextTemp) {
            if((packet is C07PacketPlayerDigging || packet is C08PacketPlayerBlockPlacement) && isBlocking) {
                event.cancelEvent()
            }else if (packet is C03PacketPlayer || packet is C0APacketAnimation || packet is C0BPacketEntityAction || packet is C02PacketUseEntity || packet is C07PacketPlayerDigging || packet is C08PacketPlayerBlockPlacement) {
                if (modeValue.equals("Vulcan") && waitC03 && packet is C03PacketPlayer) {
                    waitC03 = false
                    return
                }
                packetBuf.add(packet as Packet<INetHandlerPlayServer>)
                event.cancelEvent()
            }
        } else if (teleportValue.get() && packet is S08PacketPlayerPosLook) {
            pendingFlagApplyPacket = true
            lastMotionX = mc.thePlayer.motionX
            lastMotionY = mc.thePlayer.motionY
            lastMotionZ = mc.thePlayer.motionZ
            when (teleportModeValue.get().toLowerCase()) {
                "vanillanosetback" -> {
                    val x = packet.x - mc.thePlayer.posX
                    val y = packet.y - mc.thePlayer.posY
                    val z = packet.z - mc.thePlayer.posZ
                    val diff = sqrt(x * x + y * y + z * z)
                    if (diff <= 8) {
                        event.cancelEvent()
                        pendingFlagApplyPacket = false
                        PacketUtils.sendPacketNoEvent(
                            C03PacketPlayer.C06PacketPlayerPosLook(
                                packet.x,
                                packet.y,
                                packet.z,
                                packet.getYaw(),
                                packet.getPitch(),
                                mc.thePlayer.onGround
                            )
                        )
                    }
                }
            }
        } else if (pendingFlagApplyPacket && packet is C03PacketPlayer.C06PacketPlayerPosLook) {
            pendingFlagApplyPacket = false
            if (teleportNoApplyValue.get()) {
                event.cancelEvent()
            }
            when (teleportModeValue.get().toLowerCase()) {
                "vanilla", "vanillanosetback" -> {
                    mc.thePlayer.motionX = lastMotionX
                    mc.thePlayer.motionY = lastMotionY
                    mc.thePlayer.motionZ = lastMotionZ
                }
                "custom" -> {
                    if (MovementUtils.isMoving()) {
                        MovementUtils.strafe(teleportCustomSpeedValue.get())
                    }

                    if (teleportCustomYValue.get()) {
                        if (lastMotionY> 0) {
                            mc.thePlayer.motionY = teleportCustomSpeedValue.get().toDouble()
                        } else {
                            mc.thePlayer.motionY = -teleportCustomSpeedValue.get().toDouble()
                        }
                    }
                }
                "decrease" -> {
                    mc.thePlayer.motionX = lastMotionX * teleportDecreasePercentValue.get()
                    mc.thePlayer.motionY = lastMotionY * teleportDecreasePercentValue.get()
                    mc.thePlayer.motionZ = lastMotionZ * teleportDecreasePercentValue.get()
                }
            }
        }
    }

    override val tag: String
        get() = modeValue.get()

}
