package net.ccbluex.liquidbounce.features.special

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import kotlin.math.min

class CombatManager : Listenable, MinecraftInstance() {
    private val lastAttackTimer = MSTimer()

    var inCombat = false
        private set
    var target: EntityLivingBase? = null
        private set
    val attackedEntityList = mutableListOf<EntityLivingBase>()
    val focusedPlayerList = mutableListOf<EntityPlayer>()
    var killedEntities = 0

    //time
    var playedTime = "0h 0m 0s"
    var ticks = 0
    var seconds = 0
    var minutes = 0
    var hours = 0

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        // time
        ticks += 1
        if (ticks == 20) {
            seconds += 1
            ticks = 0
        }

        if (seconds == 60) {
            minutes += 1
            seconds = 0
        }

        if (minutes == 60) {
            hours += 1
            minutes = 0
        }

        playedTime = hours.toString() + "h " + minutes.toString() + "m " + seconds.toString() + "s"


        //others
        if (mc.thePlayer == null) return
        MovementUtils.updateBlocksPerSecond()

        inCombat = false

        if (!lastAttackTimer.hasTimePassed(1000)) {
            inCombat = true
            return
        }

        if (target != null) {
            if (mc.thePlayer.getDistanceToEntity(target) > 7 || !inCombat || target!!.isDead) {
                target = null
            } else {
                inCombat = true
            }
        }

        // bypass java.util.ConcurrentModificationException
        attackedEntityList.map { it }.forEach {
            if (it.isDead) {
                LiquidBounce.eventManager.callEvent(EntityKilledEvent(it))
                attackedEntityList.remove(it)
            }
        }
    }

    @EventTarget
    fun onKilled(event: EntityKilledEvent) {
        val target = event.targetEntity

        if (target !is EntityPlayer) {
            return
        }

        killedEntities += 1
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val target = event.targetEntity

        if (target is EntityLivingBase && EntityUtils.isSelected(target, true)) {
            this.target = target
            if (!attackedEntityList.contains(target)) {
                attackedEntityList.add(target)
            }
        }
        lastAttackTimer.reset()
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        inCombat = false
        target = null
        attackedEntityList.clear()
        focusedPlayerList.clear()
    }

    fun getNearByEntity(radius: Float): EntityLivingBase? {
        return try {
            mc.theWorld.loadedEntityList
                .filter { mc.thePlayer.getDistanceToEntity(it) < radius && EntityUtils.isSelected(it, true) }
                .sortedBy { it.getDistanceToEntity(mc.thePlayer) }[0] as EntityLivingBase?
        } catch (e: Exception) {
            null
        }
    }

    fun isFocusEntity(entity: EntityPlayer): Boolean {
        if (focusedPlayerList.isEmpty()) {
            return true // no need 2 focus
        }

        return focusedPlayerList.contains(entity)
    }

    override fun handleEvents() = true
}