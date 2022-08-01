
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.awt.*;
import java.lang.reflect.Field;
import java.security.PublicKey;
import java.util.ArrayList;
@SideOnly(Side.CLIENT)
public final class ClientUtil extends MinecraftInstance {
    public static ArrayList notifications = new ArrayList();
    public static float Yaw;

    public static Minecraft mc = Minecraft.getMinecraft();

    private static final Logger logger = LogManager.getLogger("FDPClient");

    private static Field fastRenderField;

    static {
        try {
            fastRenderField = GameSettings.class.getDeclaredField("ofFastRender");

            if(!fastRenderField.isAccessible())
                fastRenderField.setAccessible(true);
        }catch(final NoSuchFieldException ignored) {
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void RenderRotate(float yaw) {
        final Minecraft mc = KillAura.mc;
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
        final Minecraft mc2 = KillAura.mc;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;

    }

    public static void disableFastRender() {
        try {
            if(fastRenderField != null) {
                if(!fastRenderField.isAccessible())
                    fastRenderField.setAccessible(true);

                fastRenderField.setBoolean(mc.gameSettings, false);
            }
        }catch(final IllegalAccessException ignored) {
        }
    }

    public static void sendEncryption(final NetworkManager networkManager, final SecretKey secretKey, final PublicKey publicKey, final S01PacketEncryptionRequest encryptionRequest) {
        networkManager.sendPacket(new C01PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.getVerifyToken()), p_operationComplete_1_ -> networkManager.enableEncryption(secretKey));
    }

    public static void drawNotifications() {
        ScaledResolution res = new ScaledResolution(mc);
        double startY = (double) (res.getScaledHeight() - 25);
        double lastY = startY;

        for (int i = 0; i < notifications.size(); ++i) {
            Notifications not = (Notifications) notifications.get(i);

        }
    }
    public static void displayChatMessage(final String message) {
        if (mc.thePlayer == null) {
            getLogger().info("(MCChat)" + message);
            return;
        }

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);

        mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static String removeColorCode(String displayString) {
        return displayString.replaceAll("\247.", "");
    }

}