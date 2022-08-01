/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.utils.ClientUtil;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Text;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.HanaBiColors;
import net.ccbluex.liquidbounce.utils.render.RenderUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.ccbluex.liquidbounce.utils.*;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ModuleInfo(name = "Widget", category = ModuleCategory.RENDER)
public class Widget extends Module {
    public static ListValue WidgetMode = new ListValue("WidgetMode", new String[]{"Widget_1","Widget_2"
            ,"Widget_3","Widget_4","Widget_5","Widget_6","Widget_7","Widget_8","Widget_9","None"}, "Widget_2");
    public final BoolValue potion = new BoolValue("HUD_Potion", true);
    public final BoolValue hotbar = new BoolValue("HUD_Hotbar", true);
    public static final IntegerValue r = new IntegerValue("R", 0, 0, 255);
    public static final IntegerValue g = new IntegerValue("G", 160, 0, 255);
    public static final IntegerValue b = new IntegerValue("B", 255, 0, 255);
    public static final IntegerValue h = new IntegerValue("H", 255, 0, 255);

    public Widget() {
        setState(true);
    }
    @EventTarget
    public void onRender2D(final Render2DEvent event) {

        ScaledResolution sr = new ScaledResolution(mc);
        float width1 = sr.getScaledWidth();
        float height1 = sr.getScaledHeight();
        if (potion.get()) {
            this.renderPotionStatus((int) width1, (int) height1);
        }

        if (mc.currentScreen instanceof GuiHudDesigner)
            return;


        if (hotbar.get() && this.mc.getRenderViewEntity() instanceof EntityPlayer
                && !mc.gameSettings.hideGUI) {
            GameFontRenderer font = Fonts.font35;

            RenderUtil.drawRect(0, height1 - 22, width1, height1, ClientUtil.reAlpha(HanaBiColors.BLACK.c, 0.5f));

            long ping = (mc.getCurrentServerData() != null) ? mc.getCurrentServerData().pingToServer : -1;

            int f1 = (ping <= 100 ? new Color(0x2f74fd).getRGB()
                    : ping <= 250 ? new Color(HanaBiColors.ORANGE.c).darker().getRGB() : new Color(HanaBiColors.RED.c).getRGB());

            RenderUtil.drawFilledCircle1(10, height1 - 12f, 3, f1);

            font.drawString("PING:" + LyEntityUtils.getPing(mc.thePlayer) + "ms     FPS:" + mc.getDebugFPS() + " X:" + Text.Companion.getDECIMAL_FORMAT().format(mc.thePlayer.posX) + " Y:" + Text.Companion.getDECIMAL_FORMAT().format(mc.thePlayer.posY) + " Z:" + Text.Companion.getDECIMAL_FORMAT().format(mc.thePlayer.posZ) + " HP:" + mc.thePlayer.getHealth(), 16f,
                    height1 - 14f, -1);

            String ez = "NewSlience Build " + "2022" + " - " + mc.thePlayer.getName();
            Fonts.font35.drawString(ez, sr.getScaledWidth() - font.getStringWidth(ez) - 5,
                    sr.getScaledHeight() - 14, -1);

            if (mc.thePlayer.inventory.currentItem == 0) {
                RenderUtil.drawRect(width1 / 2 - 91, height1 - 2, (width1 / 2 + 90) - 20 * 8, height1, new Color(47, 116, 253,255).getRGB());
                RenderUtil.drawGradientSideways(width1 / 2 - 91, height1 - 20, (width1 / 2 + 90) - 20 * 8, height1, new Color(47, 116, 253,180).getRGB(), new Color(47, 116, 253,0).getRGB());

            } else {
                RenderUtil.drawRect((width1 / 2) - 91 + mc.thePlayer.inventory.currentItem * 20, height1 - 2, (width1 / 2) + 90 - 20 * (8 - mc.thePlayer.inventory.currentItem), height1, new Color(47, 116, 253,255).getRGB());
                RenderUtil.drawGradientSideways((width1 / 2) - 91 + mc.thePlayer.inventory.currentItem * 20, height1 - 20, (width1 / 2) + 90 - 20 * (8 - mc.thePlayer.inventory.currentItem), height1, new Color(47, 116, 253,180).getRGB(), new Color(47, 116, 253,0).getRGB());
            }

            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                int k = (int) (width1 / 2 - 90 + j * 20 + 2);
                int l = (int) (height1 - 16 - 3);
                this.customRenderHotbarItem(j, k, l, event.getPartialTicks(), mc.thePlayer);
            }

            GlStateManager.disableBlend();
            GlStateManager.color(1, 1, 1);

            RenderHelper.disableStandardItemLighting();

            GL11.glColor4f(1, 1, 1, 1);
        }
        int width = 0;
        int height = 0;
        switch(Widget.WidgetMode.get()) {
            case "Widget_1":
                width = 505;
                height = 512;
                break;
            case "Widget_2":
                width = 505;
                height = 512;
                break;
            case "Widget_3":
                width = 505;
                height = 512;
                break;
            case "Widget_4":
                width = 505;
                height = 512;
                break;
            case "Widget_5":
                width = 505;
                height = 512;
                break;
            case "Widget_6":
                width = 505;
                height = 512;
                break;
            case "Widget_7":
                width = 505;
                height = 512;
                break;
            case "Widget_8":
                width = 505;
                height = 512;
                break;
            case "Widget_9":
                width = 505;
                height = 512;
                break;

        }
        width *= 0.25;
        height *= 0.25;
        RenderUtil.drawCustomImage(RenderUtil.width() / 2 + 300 - width, RenderUtil.height() - height - (mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0),
                width, height, new ResourceLocation("fdpclient/widget/" + WidgetMode.get() + ".png"));
    }


    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (event.getEventState() == EventState.POST) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        }
    }
    public void customRenderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {

        GlStateManager.disableBlend();

        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];

        if (itemstack != null) {
            float f = (float) itemstack.animationsToGo - partialTicks;

            if (f > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
            }

            mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f > 0.0F) {
                GlStateManager.popMatrix();
            }

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemstack, xPos - 1, yPos);
        }
    }

    Map<Potion, Double> timerMap = new HashMap<Potion, Double>();

    private int x;

    public void renderPotionStatus(int width, int height) {
        x = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for (PotionEffect effect : (Collection<PotionEffect>) this.mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName());
            int minutes = -1;
            int seconds = -2;

            try {
                minutes = Integer.parseInt(potion.getDurationString(effect).split(":")[0]);
                seconds = Integer.parseInt(potion.getDurationString(effect).split(":")[1]);
            } catch (Exception ex) {
                minutes = 0;
                seconds = 0;
            }

            double total = (minutes * 60) + seconds;

            if (!timerMap.containsKey(potion)) {
                timerMap.put(potion, total);
            }

            if (timerMap.get(potion) == 0 || total > timerMap.get(potion)) {
                timerMap.replace(potion, total);
            }

            switch (effect.getAmplifier()) {
                case 0:
                    PType = PType + " I";
                    break;
                case 1:
                    PType = PType + " II";
                    break;
                case 2:
                    PType = PType + " III";
                    break;
                case 3:
                    PType = PType + " IV";
                    break;
                case 4:
                    PType = PType + " V";
                    break;
                case 5:
                    PType = PType + " VI";
                    break;
                case 6:
                    PType = PType + " VII";
                    break;
                case 7:
                    PType = PType + " VIII";
                    break;
                case 8:
                    PType = PType + " IX";
                    break;
                case 9:
                    PType = PType + " X";
                    break;
                case 10:
                    PType = PType + " X+";
                    break;
                default:
                    break;
            }

            int color = HanaBiColors.WHITE.c;

            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                color = HanaBiColors.YELLOW.c;
            } else if (effect.getDuration() < 300) {
                color = HanaBiColors.RED.c;
            } else if (effect.getDuration() > 600) {
                color = HanaBiColors.WHITE.c;
            }

            int x1 = (int) ((width - 6) * 1.33f);
            int y1 = (int) ((height - 52 - this.mc.fontRendererObj.FONT_HEIGHT + x + 5) * 1.33F);

            RenderUtils.drawRect(width - 120, height - 60 + x, width - 10, height - 30 + x,
                    ClientUtil.reAlpha(HanaBiColors.BLACK.c, 0.41f));

            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();

                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1, 1, 1, 1);
                int index = potion.getStatusIconIndex();
                ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                mc.getTextureManager().bindTexture(location);
                GlStateManager.scale(0.75, 0.75, 0.75);
                mc.ingameGUI.drawTexturedModalRect(x1 - 138, y1 + 8, 0 + index % 8 * 18, 198 + index / 8 * 18, 18, 18);

                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GlStateManager.popMatrix();
            }

            int y = (height - this.mc.fontRendererObj.FONT_HEIGHT + x) - 38;
            GameFontRenderer font = Fonts.font35;
            font.drawString(PType.replaceAll("\247.", ""), (float) width - 91f,
                    y - this.mc.fontRendererObj.FONT_HEIGHT + 1, potion.getLiquidColor());

            Fonts.font35.drawString(Potion.getDurationString(effect).replaceAll("\247.", ""),
                    width - 91f, y + 4, ClientUtil.reAlpha(-1, 0.8f));

            x -= 35;
        }
    }
    private void onArmor(EntityLivingBase target) {
        ScaledResolution res = new ScaledResolution(this.mc);
        final EntityPlayer player = (EntityPlayer) target;
        final ItemStack[] render = player.inventory.armorInventory;//18 17 18
        ItemStack render1 = null;
        if (mc.thePlayer.getCurrentEquippedItem() != null) {
            render1 = mc.thePlayer.getCurrentEquippedItem();
        }
        RenderUtil.renderItemStack(render[3], (res.getScaledWidth() / 2 - 52) + 60, res.getScaledHeight() - 55);//52
        RenderUtil.renderItemStack(render[2], (res.getScaledWidth() / 2 - 36) + 60, res.getScaledHeight() - 55);//36
        RenderUtil.renderItemStack(render[1], (res.getScaledWidth() / 2 - 20) + 60, res.getScaledHeight() - 55);//20
        RenderUtil.renderItemStack(render[0], (res.getScaledWidth() / 2 - 4) + 60, res.getScaledHeight() - 55);//4
        RenderUtil.renderItemStack(render1, (res.getScaledWidth() / 2 + 12) + 60, res.getScaledHeight() - 55);
        //renderEnchantText(render[3], (res.getScaledWidth() / 2 - 52) + 60, res.getScaledHeight() - 55);
        //renderEnchantText(render[2], (res.getScaledWidth() / 2 - 36) + 60, res.getScaledHeight() - 55);
        //renderEnchantText(render[1], (res.getScaledWidth() / 2 - 20) + 60, res.getScaledHeight() - 55);
        //renderEnchantText(render[0], (res.getScaledWidth() / 2 - 4) + 60, res.getScaledHeight() - 55);
        //    if (render1 != null ) {
        //renderEnchantText(render1, (res.getScaledWidth() / 2 - 12) + 60, res.getScaledHeight() - 55);
        //}
    }

}
