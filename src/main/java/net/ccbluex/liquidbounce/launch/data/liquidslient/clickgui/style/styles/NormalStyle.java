/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.style.styles;

import net.ccbluex.liquidbounce.launch.data.liquidslient.ClickGUIModule;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.Panel;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.launch.data.liquidslient.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class NormalStyle extends Style {

    private boolean mouseDown;
    private boolean rightMouseDown;

    private final GameFontRenderer font = Fonts.font40;
    private final int bgColor = ColorUtils.black(70);

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawGradientSideways((float) panel.getX() - 5, (float) panel.getY(), (float) panel.getX() + panel.getWidth() + 5, (float) panel.getY() + 19, ClickGUIModule.generateColor().getRGB(), ColorUtils.reAlpha(ClickGUIModule.generateColor(), 50).getRGB());
        if (panel.getFade() > 0)
            RenderUtils.drawRect((float) panel.getX() - 1, (float) panel.getY() + 19, (float) panel.getX() + panel.getWidth() + 1, panel.getY() + 19 + panel.getFade(), bgColor);
        GlStateManager.resetColor();
        float textWidth = font.getStringWidth("§f" + StringUtils.stripControlCodes(panel.getName()));
        font.drawString("§f" + panel.getName(), (int) (panel.getX() - (textWidth - 93.0F) / 2F), panel.getY() + 7, Color.WHITE.getRGB());
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = font.getStringWidth(text);

        RenderUtils.drawRoundedRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + font.FONT_HEIGHT + 3, 2.5F, ClickGUIModule.generateColor().getRGB());
        GlStateManager.resetColor();
        font.drawString(text, mouseX + 12, mouseY + (font.FONT_HEIGHT / 2), Color.WHITE.getRGB());
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        GlStateManager.resetColor();
        font.drawString(buttonElement.getDisplayName(), (int) (buttonElement.getX() - (font.getStringWidth(buttonElement.getDisplayName()) - 100.0f) / 2.0f), buttonElement.getY() + 6, buttonElement.getColor());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        final int guiColor = ClickGUIModule.generateColor().getRGB();
        GlStateManager.resetColor();
        font.drawString(moduleElement.getDisplayName(), moduleElement.getX() + 3, moduleElement.getY() + 7, Color.WHITE.getRGB());
        if (moduleElement.getModule().getState()) {
            RenderUtils.drawRect(moduleElement.getX(), moduleElement.getY(), moduleElement.getWidth() + moduleElement.getX(), moduleElement.getHeight() + moduleElement.getY(), Color.BLACK.getRGB());
        }
        
        final List<Value<?>> moduleValues = moduleElement.getModule().getValues();

        if (!moduleValues.isEmpty()) {
            font.drawString(moduleElement.isShowSettings() ? "-" : "+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + (moduleElement.getHeight() / 2), Color.WHITE.getRGB());

            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;
                for (final Value value : moduleValues) {
                    if (!value.getDisplayable())
                        continue;

                    if (value instanceof BoolValue) {
                        String text = value.getName();
                        float textWidth = font.getStringWidth(text) + 8 + 8;

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, bgColor);
                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 10, yPos + 4, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 2, yPos + 12, bgColor);

                        if (((BoolValue) value).get()) {
                            RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 9, yPos + 5, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 3, yPos + 11, Color.WHITE.getRGB());
                        }

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                            if (Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                final BoolValue boolValue = (BoolValue) value;

                                boolValue.set(!boolValue.get());
                                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                            }
                        }

                        GlStateManager.resetColor();
                        font.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        yPos += 12;
                    } else if (value instanceof ListValue) {
                        ListValue listValue = (ListValue) value;

                        String text = value.getName();
                        float textWidth = font.getStringWidth(text) + 8;

                        if (moduleElement.getSettingsWidth() < textWidth + 16)
                            moduleElement.setSettingsWidth(textWidth + 16);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, bgColor);
                        GlStateManager.resetColor();
                        font.drawString("§c" + text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        font.drawString(listValue.openList ? "-" : "+", (int) (moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - (listValue.openList ? 5 : 6)), yPos + 4, 0xffffff);

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                            if (Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                listValue.openList = !listValue.openList;
                                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                            }
                        }

                        yPos += 12;

                        for (final String valueOfList : listValue.getValues()) {
                            final float textWidth2 = font.getStringWidth(">" + valueOfList);

                            if (moduleElement.getSettingsWidth() < textWidth2 + 12)
                                moduleElement.setSettingsWidth(textWidth2 + 12);

                            if (listValue.openList) {
                                RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, bgColor);

                                if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                                    if (Mouse.isButtonDown(0) && moduleElement.isntPressed()) {
                                        listValue.set(valueOfList);
                                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                                    }
                                }

                                GlStateManager.resetColor();
                                font.drawString(">", moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                                font.drawString(valueOfList, moduleElement.getX() + moduleElement.getWidth() + 14, yPos + 4, listValue.get() != null && listValue.get().equalsIgnoreCase(valueOfList) ? guiColor : Color.WHITE.getRGB());
                                yPos += 12;
                            }
                        }
                    } else if (value instanceof FloatValue) {
                        FloatValue floatValue = (FloatValue) value;
                        String text = value.getName() + "§f: §c" + round(floatValue.get());
                        float textWidth = font.getStringWidth(text) + 8;

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 24, bgColor);
                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 8, yPos + 18, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4, yPos + 19, Color.WHITE.getRGB());
                        float sliderValue = moduleElement.getX() + moduleElement.getWidth() + ((moduleElement.getSettingsWidth() - 12) * (floatValue.get() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum()));
                        RenderUtils.drawRect(8 + sliderValue, yPos + 15, sliderValue + 11, yPos + 21, guiColor);

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4 && mouseY >= yPos + 15 && mouseY <= yPos + 21) {
                            if (Mouse.isButtonDown(0)) {
                                double i = MathHelper.clamp_double((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12), 0, 1);
                                floatValue.set(round((float) (floatValue.getMinimum() + (floatValue.getMaximum() - floatValue.getMinimum()) * i)).floatValue());
                            }
                        }

                        GlStateManager.resetColor();
                        font.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        yPos += 22;
                    } else if (value instanceof IntegerValue) {
                        IntegerValue integerValue = (IntegerValue) value;
                        String text = value.getName() + "§f: §c" + (value instanceof BlockValue ? BlockUtils.getBlockName(integerValue.get()) + " (" + integerValue.get() + ")" : integerValue.get());
                        float textWidth = font.getStringWidth(text) + 8;

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 24, bgColor);
                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 8, yPos + 18, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() - 4, yPos + 19, Color.WHITE.getRGB());
                        float sliderValue = moduleElement.getX() + moduleElement.getWidth() + ((moduleElement.getSettingsWidth() - 12) * (integerValue.get() - integerValue.getMinimum()) / (integerValue.getMaximum() - integerValue.getMinimum()));
                        RenderUtils.drawRect(8 + sliderValue, yPos + 15, sliderValue + 11, yPos + 21, guiColor);

                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21) {
                            if (Mouse.isButtonDown(0)) {
                                double i = MathHelper.clamp_double((mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12), 0, 1);
                                integerValue.set((int) (integerValue.getMinimum() + (integerValue.getMaximum() - integerValue.getMinimum()) * i));
                            }
                        }

                        GlStateManager.resetColor();
                        font.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        yPos += 22;
                    } else if (value instanceof FontValue) {
                        final FontValue fontValue = (FontValue) value;
                        final FontRenderer fontRenderer = fontValue.get();

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, bgColor);

                        String displayString = "Font: Unknown";

                        if (fontRenderer instanceof GameFontRenderer) {
                            final GameFontRenderer liquidFontRenderer = (GameFontRenderer) fontRenderer;

                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == font)
                            displayString = "Font: Minecraft";
                        else {
                            final Object[] objects = Fonts.getFontDetails(fontRenderer);

                            if (objects != null) {
                                displayString = objects[0] + ((int) objects[1] != -1 ? " - " + objects[1] : "");
                            }
                        }

                        font.drawString(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        int stringWidth = font.getStringWidth(displayString);

                        if (moduleElement.getSettingsWidth() < stringWidth + 8)
                            moduleElement.setSettingsWidth(stringWidth + 8);

                        if ((Mouse.isButtonDown(0) && !mouseDown || Mouse.isButtonDown(1) && !rightMouseDown) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && mouseX <= moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth() && mouseY >= yPos + 4 && mouseY <= yPos + 12) {
                            final List<FontRenderer> fonts = Fonts.getFonts();

                            if (Mouse.isButtonDown(0)) {
                                for (int i = 0; i < fonts.size(); i++) {
                                    final FontRenderer font = fonts.get(i);

                                    if (font == fontRenderer) {
                                        i++;

                                        if (i >= fonts.size())
                                            i = 0;

                                        fontValue.set(fonts.get(i));
                                        break;
                                    }
                                }
                            } else {
                                for (int i = fonts.size() - 1; i >= 0; i--) {
                                    final FontRenderer font = fonts.get(i);

                                    if (font == fontRenderer) {
                                        i--;

                                        if (i >= fonts.size())
                                            i = 0;

                                        if (i < 0)
                                            i = fonts.size() - 1;

                                        fontValue.set(fonts.get(i));
                                        break;
                                    }
                                }
                            }
                        }

                        yPos += 11;
                    } else {
                        String text = value.getName() + "§f: §c" + value.get();
                        float textWidth = font.getStringWidth(text) + 8;

                        if (moduleElement.getSettingsWidth() < textWidth + 8)
                            moduleElement.setSettingsWidth(textWidth + 8);

                        RenderUtils.drawRect(moduleElement.getX() + moduleElement.getWidth() + 4, yPos + 2, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 14, bgColor);
                        GlStateManager.resetColor();
                        font.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xffffff);
                        yPos += 12;
                    }
                }

                moduleElement.updatePressed();
                mouseDown = Mouse.isButtonDown(0);
                rightMouseDown = Mouse.isButtonDown(1);

                if (moduleElement.getSettingsWidth() > 0F && yPos > moduleElement.getY() + 4)
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, moduleElement.getX() + moduleElement.getWidth() + moduleElement.getSettingsWidth(), yPos + 2, 1F, bgColor, 0);
            }
        }
    }

    private BigDecimal round(final float f) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }
}

