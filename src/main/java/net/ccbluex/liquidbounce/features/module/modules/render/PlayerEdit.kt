package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "PlayerEdit", category = ModuleCategory.RENDER)
class PlayerEdit : Module() {
    val size = FloatValue("PlayerSize", 0.5f, 0.01f, 5.0f)
}