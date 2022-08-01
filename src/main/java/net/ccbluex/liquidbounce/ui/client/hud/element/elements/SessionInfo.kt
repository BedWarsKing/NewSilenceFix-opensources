package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.MiLiBlueUtil
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import java.awt.Color
import java.text.DecimalFormat


/**
 * @author ChengFeng
 * CurrentSession
 */
@ElementInfo(name = "SessionInfo", blur = true)
class SessionInfo : Element(200.0, 100.0, 1F, Side(Side.Horizontal.RIGHT, Side.Vertical.UP)) {
    private val shadow = BoolValue("Shadow", true)

    override fun drawElement(partialTicks: Float): Border {

        val font = Fonts.fontSFUI35
        val color = Color.WHITE.rgb
        val fontHeight = Fonts.font40.FONT_HEIGHT
        val format = DecimalFormat("#.##")

        if (shadow.get()) MiLiBlueUtil.drawShadow(0, 0, 150F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 30F)

        RenderUtils.drawRect(0F, 0F, 150F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 30F, ColorUtils.black(100))
        RenderUtils.drawRect(0F, 0F, 150F, 1F, ColorUtils.hslRainbow(1, indexOffset = 100))

        // title
        Fonts.font40.drawString("Session Info", 5F, 3F, color)
        font.drawString("Played Time", 5F, 3F + fontHeight + 5F, color)
        font.drawString("Speed", 5F, 3F + fontHeight + font.FONT_HEIGHT + 10F, color)
        font.drawString("Ping", 5F, 3F + fontHeight + font.FONT_HEIGHT * 2 + 15F, color)
        font.drawString("Kills", 5F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 20F, color)

        // info
        font.drawString(
                LiquidBounce.combatManager.playedTime,
                150 - font.getStringWidth(LiquidBounce.combatManager.playedTime) - 5F,
                3F + fontHeight + 5F,
                color
        )
        font.drawString(
                format.format(MovementUtils.bps),
                150 - font.getStringWidth(format.format(MovementUtils.bps)) - 5F,
                3F + fontHeight + font.FONT_HEIGHT + 10F,
                color
        )
        if (mc.isSingleplayer) {
            font.drawString(
                    "0ms (Singleplayer)",
                    150 - font.getStringWidth("0ms (Singleplayer)") - 5F,
                    3F + fontHeight + font.FONT_HEIGHT * 2 + 15F,
                    color
            )
        } else font.drawString(
                mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID).responseTime.toString(),
                150 - font.getStringWidth(mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID).responseTime.toString()) - 5F,
                3F + fontHeight + font.FONT_HEIGHT * 2 + 15F,
                color
        )
        font.drawString(
                LiquidBounce.combatManager.killedEntities.toString(),
                150 - font.getStringWidth(LiquidBounce.combatManager.killedEntities.toString()) - 5F,
                3F + fontHeight + font.FONT_HEIGHT * 3 + 20F,
                color
        )

        return Border(0F, 0F, 150F, 3F + fontHeight + font.FONT_HEIGHT * 3 + 30F)
    }
}