/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.client.button.AbstractButtonRenderer
import net.ccbluex.liquidbounce.features.module.modules.client.button.FLineButtonRenderer
import net.ccbluex.liquidbounce.features.module.modules.client.button.RiseButtonRenderer
import net.ccbluex.liquidbounce.features.module.modules.client.button.RoundedButtonRenderer
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.SystemUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import java.awt.Color


@ModuleInfo(name = "HUD", category = ModuleCategory.COLOR, array = false, defaultOn = true)
object HUD : Module() {
    private val buttonValue = ListValue("Button", arrayOf("FLine", "Rounded", "Rise", "Vanilla"), "FLine")

    private val waterMarkValue = BoolValue("WaterMark", true)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    private val noAchievementsValue = BoolValue("NoAchievements", true)
    private val noBobValue = BoolValue("NoBob", false)
    private val blurValue = BoolValue("Blur", false)
    private val healValue = BoolValue("Health", true)

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (mc.currentScreen is GuiHudDesigner) return
        LiquidBounce.hud.render(false, event.partialTicks)

        if (healValue.get()) {
            if (mc.thePlayer.health >= 0.0f && mc.thePlayer.health < 10.0f) {
                width = 3
            }
            if (mc.thePlayer.health >= 10.0f && mc.thePlayer.health < 100.0f) {
                width = 5
            }
            mc.fontRendererObj.drawStringWithShadow(
                "" + MathHelper.ceiling_float_int(mc.thePlayer.health),
                (ScaledResolution(mc).scaledWidth / 2 - width).toFloat(),
                (ScaledResolution(mc).scaledHeight / 2 - 5).toFloat() - 5,
                if (mc.thePlayer.health <= 10.0f) Color(255, 0, 0).rgb else Color(0, 255, 0).rgb
            )
        }

        if (waterMarkValue.get()) {
            Fonts.fontSFUI40.drawString(LiquidBounce.CLIENT_NAME + SystemUtils.getTime(), 8F, 8F, Color.WHITE.rgb, true)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        LiquidBounce.hud.update()

        if (noBobValue.get()) {
            mc.thePlayer.distanceWalkedModified = 0f
        }
    }

    @EventTarget
    fun onTick(event: TickEvent) {
        if (noAchievementsValue.get()) {
            mc.guiAchievement.clearAchievements()
        }
    }

    @EventTarget
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) {
            return
        }

        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive && event.guiScreen != null && !(event.guiScreen is GuiChat || event.guiScreen is GuiHudDesigner)) {
            mc.entityRenderer.loadShader(ResourceLocation("fdpclient/blur.json"))
        } else if (mc.entityRenderer.shaderGroup != null && mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("fdpclient/blur.json")) {
            mc.entityRenderer.stopUseShader()
        }
    }

    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    fun getButtonRenderer(button: GuiButton): AbstractButtonRenderer? {
        return when (buttonValue.get().lowercase()) {
            "fline" -> FLineButtonRenderer(button)
            "rounded" -> RoundedButtonRenderer(button)
            "rise" -> RiseButtonRenderer(button)
            else -> null // vanilla or unknown
        }
    }
}