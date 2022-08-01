package net.ccbluex.liquidbounce.utils

object PlayerUtils {
    fun randomUnicode(str: String): String {
        val stringBuilder = StringBuilder()
        for (c in str.toCharArray()) {
            if (Math.random() > 0.5 && c.code in 33..128) {
                stringBuilder.append(Character.toChars(c.code + 65248))
            } else {
                stringBuilder.append(c)
            }
        }
        return stringBuilder.toString()
    }

    fun checkVoid(): Boolean {
        var i = (-(MinecraftInstance.mc.thePlayer.posY - 1.4857625)).toInt()
        var dangerous = true
        while (i <= 0) {
            dangerous = MinecraftInstance.mc.theWorld.getCollisionBoxes(
                MinecraftInstance.mc.thePlayer.entityBoundingBox.offset(
                    MinecraftInstance.mc.thePlayer.motionX * 0.5,
                    i.toDouble(),
                    MinecraftInstance.mc.thePlayer.motionZ * 0.5
                )
            ).isEmpty()
            i++
            if (!dangerous) break
        }
        return dangerous
    }
}