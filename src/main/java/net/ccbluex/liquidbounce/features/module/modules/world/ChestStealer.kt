/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.player.InvManager
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S30PacketWindowItems
import net.minecraft.util.ResourceLocation
import kotlin.random.Random

@ModuleInfo(name = "ChestStealer", category = ModuleCategory.WORLD)
class ChestStealer : Module() {
    /**
     * OPTIONS
     */

    private val delayValue = IntegerValue("Delay", 10, 0, 1000)
    private val chestValue = IntegerValue("FirstDelay", 300, 0, 1000)
    private val autoCloseDelay = IntegerValue("AutoCloseDelay", 0, 0, 1000)

    private val takeRandomizedValue = BoolValue("RandomTake", false)
    private val onlyItemsValue = BoolValue("OnlyItems", false)
    private val noCompassValue = BoolValue("NoCompass", false)
    private val autoCloseValue = BoolValue("AutoClose", true)
    val silentValue = BoolValue("Silent", true)
    val silentTitleValue = BoolValue("SilentTitle", true)

    private val closeOnFullValue = BoolValue("CloseOnFull", true).displayable { autoCloseValue.get() }
    val chestTitleValue = BoolValue("ChestTitle", false)

    /**
     * VALUES
     */
    private val delayTimer = MSTimer()
    private val chestTimer = MSTimer()
    private var nextDelay = delayValue.get().toLong()

    private val autoCloseTimer = MSTimer()
    private var nextCloseDelay = autoCloseDelay.get().toLong()

    private var contentReceived = 0

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (!chestTimer.hasTimePassed(chestValue.get().toLong())) {
            return
        }

        val screen = mc.currentScreen

        if (screen !is GuiChest || !delayTimer.hasTimePassed(nextDelay)) {
            autoCloseTimer.reset()
            return
        }

        // No Compass
        if (noCompassValue.get() && mc.thePlayer.inventory.getCurrentItem()?.item?.unlocalizedName == "item.compass") {
            return
        }

        // Chest title
        if (chestTitleValue.get() && (screen.lowerChestInventory == null || !screen.lowerChestInventory.name.contains(
                ItemStack(Item.itemRegistry.getObject(ResourceLocation("minecraft:chest"))).displayName
            ))
        ) {
            return
        }

        // inventory cleaner
        val inventoryCleaner = LiquidBounce.moduleManager[InvManager::class.java]!!

        // Is empty?
        if (!isEmpty(screen) && !(closeOnFullValue.get() && fullInventory)) {
            autoCloseTimer.reset()

            // Randomized
            if (takeRandomizedValue.get()) {
                do {
                    val items = mutableListOf<Slot>()

                    for (slotIndex in 0 until screen.inventoryRows * 9) {
                        val slot = screen.inventorySlots.inventorySlots[slotIndex]

                        if (slot.stack != null && (!onlyItemsValue.get() || slot.stack.item !is ItemBlock) && (!inventoryCleaner.state || inventoryCleaner.isUseful(
                                slot.stack,
                                -1
                            ))
                        ) {
                            items.add(slot)
                        }
                    }

                    val randomSlot = Random.nextInt(items.size)
                    val slot = items[randomSlot]

                    move(screen, slot)
                } while (delayTimer.hasTimePassed(nextDelay) && items.isNotEmpty())
                return
            }

            // Non randomized
            for (slotIndex in 0 until screen.inventoryRows * 9) {
                val slot = screen.inventorySlots.inventorySlots[slotIndex]

                if (delayTimer.hasTimePassed(nextDelay) && slot.stack != null &&
                    (!onlyItemsValue.get() || slot.stack.item !is ItemBlock) && (!inventoryCleaner.state || inventoryCleaner.isUseful(
                        slot.stack,
                        -1
                    ))
                ) {
                    move(screen, slot)
                }
            }
        } else if (autoCloseValue.get() && screen.inventorySlots.windowId == contentReceived && autoCloseTimer.hasTimePassed(
                nextCloseDelay
            )
        ) {
            mc.thePlayer.closeScreen()
            nextCloseDelay = autoCloseDelay.get().toLong()
        }
    }

    @EventTarget
    private fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S30PacketWindowItems) {
            contentReceived = packet.func_148911_c()
        }

        if (packet is S2DPacketOpenWindow) {
            chestTimer.reset()
        }
    }

    private fun move(screen: GuiChest, slot: Slot) {
        screen.handleMouseClick(slot, slot.slotNumber, 0, 1)
        delayTimer.reset()
        nextDelay = delayValue.get().toLong()
    }

    private fun isEmpty(chest: GuiChest): Boolean {
        val inventoryCleaner = LiquidBounce.moduleManager[InvManager::class.java]!!

        for (i in 0 until chest.inventoryRows * 9) {
            val slot = chest.inventorySlots.inventorySlots[i]

            if (slot.stack != null && (!onlyItemsValue.get() || slot.stack.item !is ItemBlock) && (!inventoryCleaner.state || inventoryCleaner.isUseful(
                    slot.stack,
                    -1
                ))
            ) {
                return false
            }
        }

        return true
    }

    private val fullInventory: Boolean
        get() = mc.thePlayer.inventory.mainInventory.none { it == null }
}
