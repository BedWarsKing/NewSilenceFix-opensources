/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.launch.data.liquidslient;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.ClickGui;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.style.styles.LiquidBounceStyle;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.style.styles.NormalStyle;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.style.styles.NullStyle;
import net.ccbluex.liquidbounce.launch.options.LegacyUiLaunchOption;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUIModule extends Module {
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private static final IntegerValue colorRedValue = (IntegerValue) new IntegerValue("R", 0, 0, 255).displayable(() -> !colorRainbow.get());
    private static final IntegerValue colorGreenValue = (IntegerValue) new IntegerValue("G", 160, 0, 255).displayable(() -> !colorRainbow.get());
    private static final IntegerValue colorBlueValue = (IntegerValue) new IntegerValue("B", 255, 0, 255).displayable(() -> !colorRainbow.get());    private final ListValue styleValue = new ListValue("Style", new String[]{"Normal", "LiquidBounce", "Null"}, "Null") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };
    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    private void updateStyle() {
        switch (styleValue.get().toLowerCase()) {
            case "liquidbounce":
                LegacyUiLaunchOption.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LegacyUiLaunchOption.clickGui.style = new NullStyle();
                break;
            case "normal":
                LegacyUiLaunchOption.clickGui.style = new NormalStyle();
                break;
        }
    }

    @Override
    public void onEnable() {
        updateStyle();
        mc.displayGuiScreen(LegacyUiLaunchOption.clickGui);
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ClickGui) {
            event.cancelEvent();
        }
    }




}