package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "Rainbow", category = ModuleCategory.COLOR, canEnable = false)
object Rainbow: Module() {
    val rainbowStart = FloatValue("Start", 0.1f, 0f, 1f)
    val rainbowStop = FloatValue("Stop", 0.2f, 0f, 1f)
    val rainbowSaturation = FloatValue("Saturation", 0.7f, 0f, 1f)
    val rainbowBrightness = FloatValue("Brightness", 1f, 0f, 1f)
    val rainbowSpeed = IntegerValue("Speed", 1500, 500, 7000)
}