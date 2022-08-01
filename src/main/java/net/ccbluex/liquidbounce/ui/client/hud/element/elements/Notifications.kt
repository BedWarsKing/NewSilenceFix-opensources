
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.BlurUtils
import net.ccbluex.liquidbounce.utils.render.EaseUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", blur = true)
class Notifications(
    x: Double = 0.0,
    y: Double = 0.0,
    scale: Float = 1F,
    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)
) : Element(x, y, scale, side) {

    private val backGroundAlphaValue = IntegerValue("BackGroundAlpha", 170, 0, 255)

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Notification", "This is an example notification.", NotifyType.INFO)

    /**
     * Draw element
     */
    override fun drawElement(partialTicks: Float): Border? {
        // bypass java.util.ConcurrentModificationException
        LiquidBounce.hud.notifications.map { it }.forEachIndexed { index, notify ->
            GL11.glPushMatrix()

            if (notify.drawNotification(index, backGroundAlphaValue.get(), blurValue.get(), this.renderX.toFloat(), this.renderY.toFloat(), scale)) {
                LiquidBounce.hud.notifications.remove(notify)
            }

            GL11.glPopMatrix()
        }

        if (mc.currentScreen is GuiHudDesigner) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification)) {
                LiquidBounce.hud.addNotification(exampleNotification)
            }

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
//            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(), 0F, 0F)
        }

        return null
    }

    override fun drawBoarderBlur(blurRadius: Float) {}
}

class Notification(
    val title: String,
    val content: String,
    val type: NotifyType,
    val time: Int = 1000,
    private val animeTime: Int = 500
) {

    val width=30 + Fonts.font35.getStringWidth("${this.title} ${this.content}")
    val height=20

    var fadeState = FadeState.IN
    private var nowY = -height
    var displayTime = System.currentTimeMillis()
    private var animeXTime = System.currentTimeMillis()
    private var animeYTime = System.currentTimeMillis()

    /**
     * Draw notification
     */
    fun drawNotification(index: Int, alpha: Int, blurRadius: Float, x: Float, y: Float, scale: Float): Boolean {
        val realY = -(index + 1) * height
        val nowTime = System.currentTimeMillis()
        var transY = nowY.toDouble()

        // Y-Axis Animation
        if (nowY != realY) {
            var pct = (nowTime - animeYTime) / animeTime.toDouble()
            if (pct> 1) {
                nowY = realY
                pct = 1.0
            } else {
                pct = EaseUtils.easeOutExpo(pct)
            }
            transY += (realY - nowY) * pct - 1
        } else {
            animeYTime = nowTime
        }

        // X-Axis Animation
        var pct = (nowTime - animeXTime) / animeTime.toDouble()
        when (fadeState) {
            FadeState.IN -> {
                if (pct> 1) {
                    fadeState = FadeState.STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = EaseUtils.easeOutExpo(pct)
            }

            FadeState.STAY -> {
                pct = 1.0
                if ((nowTime - animeXTime)> time) {
                    fadeState = FadeState.OUT
                    animeXTime = nowTime
                }
            }

            FadeState.OUT -> {
                if (pct> 1) {
                    fadeState = FadeState.END
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = 1 - EaseUtils.easeInExpo(pct)
            }

            FadeState.END -> {
                return true
            }
        }
        val transX = width - (width * pct) - width

        GL11.glTranslated(transX, transY, 0.0)

        if (blurRadius != 0f) {
            BlurUtils.draw((x + transX).toFloat() * scale, (y + transY).toFloat() * scale, width * scale, height * scale, blurRadius)
        }

        val rl = ResourceLocation(when(type.name) {
            "SUCCESS" -> "fdpclient/notification/success.png"
            "ERROR" -> "fdpclient/notification/error.png"
            "WARNING" -> "fdpclient/notification/warning.png"
            else -> "fdpclient/notification/info.png"
        })

        val ColorTxet = when(type.name) {
            "SUCCESS","ERROR" -> Color(155,155,155).rgb
            else -> Color(230,230,230).rgb
        }
        RenderUtils.drawRect(0F,0F, width.toFloat(),height.toFloat(),Color(0,0,0, alpha).rgb)
        RenderUtils.drawRect(0F,height-0.5F, max(width*((nowTime-displayTime)/(animeTime*2F+time)),0F),height.toFloat(),type.renderColor.rgb)
        RenderUtils.drawShadow(0, 0, width.toFloat().toInt(), height.toFloat().toInt(), )
        RenderUtils.drawImage(rl,6,6,10,10)

        Fonts.font35.drawString(title, 22F, height / 2F - 1F, Color.WHITE.rgb, false)
        Fonts.font35.drawString(content, 24F + Fonts.font35.getStringWidth(title), height / 2F - 1F, ColorTxet, false)



        return false
    }
}

enum class NotifyType(var renderColor: Color) {
    SUCCESS(Color(155,165,155)),
    ERROR(Color(165,155,155)),
    WARNING(Color(255,255,50)),
    INFO(Color(35,111,255));
}

enum class FadeState { IN, STAY, OUT, END }
