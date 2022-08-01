/*
 * This code belongs to WYSI-Foundation. Please give credits when using this in your repository.
 */
package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.BlendUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import java.awt.Color

@ModuleInfo(name = "ColorMixer", category = ModuleCategory.COLOR, canEnable = false)
object ColorMixer : Module() {
    var col1RedValue = IntegerValue("Color1-Red", 255, 0, 255)
    var col1GreenValue = IntegerValue("Color1-Green", 255, 0, 255)
    var col1BlueValue = IntegerValue("Color1-Blue", 255, 0, 255)
    var col2RedValue = IntegerValue("Color2-Red", 255, 0, 255)
    var col2GreenValue = IntegerValue("Color2-Green", 255, 0, 255)
    var col2BlueValue = IntegerValue("Color2-Blue", 255, 0, 255)
    fun getMixedColor(index: Int, seconds: Int): Color {
        val col1 = Color(col1RedValue.get(), col1GreenValue.get(), col1BlueValue.get())
        val col2 = Color(col2RedValue.get(), col2GreenValue.get(), col2BlueValue.get())
        return BlendUtils.blendColors(
            floatArrayOf(0f, 0.5f, 1f),
            arrayOf(col1, col2, col1),
            (System.currentTimeMillis() + index) % (seconds * 1000) / (seconds * 1000).toFloat()
        )
    }
}