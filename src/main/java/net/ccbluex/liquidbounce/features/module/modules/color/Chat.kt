package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "Chat", category = ModuleCategory.COLOR, canEnable = false)
class Chat: Module() {
    val fontChatValue = BoolValue("Font", false)
    val chatRectValue = BoolValue("Rect", true)
    val chatCombineValue = BoolValue("Combine", true)
    val chatAnimValue = BoolValue("Animation", false)
}