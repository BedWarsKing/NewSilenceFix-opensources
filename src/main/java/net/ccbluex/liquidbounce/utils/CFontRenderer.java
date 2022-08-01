/*
 * Decompiled with CFR 0_132.
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.i18n.LanguageManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer
extends CFont {
    protected CFont.CharData[] boldChars = new CFont.CharData[256];
    protected CFont.CharData[] italicChars = new CFont.CharData[256];
    protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }
    public static boolean isChinese(char c) {
        String s = String.valueOf(c);
        if(!"1234567890abcdefghijklmnopqrstuvwxyz!<>@#$%^&*()-_=+[]{}|\\/'\",.~`".contains(s.toLowerCase()))
            return true;
        else{
            return false;
        }
    }
    public int DisplayFontWidths(CFontRenderer font,String str) {
        return DisplayFontWidths(str,font);
    }
    public int DisplayFontWidths(String str, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        int x=0;
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                x += (float)font.getStringWidth(s);
            } else {
                x += (float) Fonts.font35.getStringWidth(s);
            }
        }
        return x+5;
    }
    public String trimStringToWidthPassword(String text,int lol,boolean wangbanxian){
        text = text.replaceAll("\u00c3\u201a","");
        return (text + "shabi").replaceAll("shabi","");
    }
    public static float DisplayFonts(CFontRenderer font,String str, float x, float y, int color) {
        return DisplayFont(str,x,y,color,font);
    }
    public static float DisplayFont(CFontRenderer font,String str, float x, float y, int color) {
        return DisplayFont(str,x,y,color,font);
    }


    public float DisplayFont2(CFontRenderer font,String str, float x, float y, int color,boolean shadow) {
        if(shadow)
            return DisplayFont(str,x,y,color,shadow,font);
        else{
            return DisplayFont(str,x,y,color,font);
        }
    }
    public static float DisplayFont(String str, float x, float y, int color,boolean shadow, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str=" "+str;
        //ClientUtils.INSTANCE.displayAlert(str);
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                color = getColor(String.valueOf(str.toCharArray()[iF + 1]));
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                font.drawString(s, x+0.5f, y+1.5f, new Color(0,0,0,100).getRGB());
                font.drawString(s, x-0.5f, y+0.5f, color);
                x += (float)font.getStringWidth(s);
            } else {
                Fonts.font35.drawString(s, x+1.5f, y+2, new Color(0,0,0,50).getRGB());
                Fonts.font35.drawString(s, x+0.5f, y+1, color);
                x += (float)Fonts.font35.getStringWidth(s);
            }
        }
        return x;
        //return font.drawString(str, x, y, color);
    }

    public static float DisplayFont(String str, float x, float y, int color, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str=" "+str;
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                color = getColor(String.valueOf(str.toCharArray()[iF + 1]));
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                font.drawString(s, x-0.5f, y+1, color);
                x += (float)font.getStringWidth(s);
            } else{
                Fonts.font35.drawString(s, x+0.5f, y+1, color);
                x += (float)Fonts.font35.getStringWidth(s);
            }
        }
        return x;
    }
    public float DisplayFonts(String str, float x, float y, int color, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str=" "+str;
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                color = getColor(String.valueOf(str.toCharArray()[iF + 1]));
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                font.drawString(s, x-0.5f, y+1, color);
                x += (float)font.getStringWidth(s);
            } else{
                Fonts.font35.drawString(s, x+0.5f, y+1, color);
                x += (float)Fonts.font35.getStringWidth(s);
            }
        }
        return x;
    }
    public int drawPassword(String wangbanxian,double x,float y,int color){
        return (int) this.drawString(wangbanxian.replaceAll(".","."),x,y,color,false);
    }
    public static int getColor(String str) {
        switch(str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    return (new Color(0, 0, 0)).getRGB();
                }
                break;
            case 49:
                if (str.equals("1")) {
                    return (new Color(0, 0, 189)).getRGB();
                }
                break;
            case 50:
                if (str.equals("2")) {
                    return (new Color(0, 192, 0)).getRGB();
                }
                break;
            case 51:
                if (str.equals("3")) {
                    return (new Color(0, 190, 190)).getRGB();
                }
                break;
            case 52:
                if (str.equals("4")) {
                    return (new Color(190, 0, 0)).getRGB();
                }
                break;
            case 53:
                if (str.equals("5")) {
                    return (new Color(189, 0, 188)).getRGB();
                }
                break;
            case 54:
                if (str.equals("6")) {
                    return (new Color(218, 163, 47)).getRGB();
                }
                break;
            case 55:
                if (str.equals("7")) {
                    return (new Color(190, 190, 190)).getRGB();
                }
                break;
            case 56:
                if (str.equals("8")) {
                    return (new Color(63, 63, 63)).getRGB();
                }
                break;
            case 57:
                if (str.equals("9")) {
                    return (new Color(63, 64, 253)).getRGB();
                }
                break;
            case 97:
                if (str.equals("a")) {
                    return (new Color(63, 254, 63)).getRGB();
                }
                break;
            case 98:
                if (str.equals("b")) {
                    return (new Color(62, 255, 254)).getRGB();
                }
                break;
            case 99:
                if (str.equals("c")) {
                    return (new Color(254, 61, 62)).getRGB();
                }
                break;
            case 100:
                if (str.equals("d")) {
                    return (new Color(255, 64, 255)).getRGB();
                }
                break;
            case 101:
                if (str.equals("e")) {
                    return (new Color(254, 254, 62)).getRGB();
                }
                break;
            case 102:
                if (str.equals("f")) {
                    return (new Color(255, 255, 255)).getRGB();
                }
        }

        return (new Color(255, 255, 255)).getRGB();
    }
    /* JADX WARN: Type inference failed for: r0v21, types: [double] */
    /* JADX WARN: Type inference failed for: r0v55, types: [double] */
    public int getPasswordWidth(String wangbanxian){
        return 0;
    }

    public String trimStringToWidth(String text,int lol,boolean wangbanxian){
        text = text.replaceAll("\u00c3\u201a","");
        return (text + "shabi").replaceAll("shabi","");
    }

    public String trimStringToWidth(String text,int wangbanxian){
        text = text.replaceAll("\u00c3\u201a","");
        return (text + "shabi").replaceAll("shabi","");
    }
    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawStringWithShadowNew(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }
    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
        return this.drawStringWithShadow(text, x - (double)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        x -= 1.0;
        if (text == null) {
            return 0.0f;
        }
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & -67108864) == 0) {
            color |= -16777216;
        }
        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }
        CFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        if (render) {
            GL11.glPushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
            int size = text.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture((int)3553, (int)this.tex.getGlTextureId());
            int i = 0;
            while (i < size) {
                char character = text.charAt(i);
                if (character == '\u00a7' && i < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                        if (colorIndex < 0 || colorIndex > 15) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((float)(colorcode >> 16 & 255) / 255.0f, (float)(colorcode >> 8 & 255) / 255.0f, (float)(colorcode & 255) / 255.0f, alpha);
                    } else if (colorIndex == 16) {
                        randomCase = true;
                    } else if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());
                            currentData = this.boldChars;
                        }
                    } else if (colorIndex == 18) {
                        strikethrough = true;
                    } else if (colorIndex == 19) {
                        underline = true;
                    } else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        } else {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                            currentData = this.italicChars;
                        }
                    } else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color((float)(color >> 16 & 255) / 255.0f, (float)(color >> 8 & 255) / 255.0f, (float)(color & 255) / 255.0f, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                    ++i;
                } else if (character < currentData.length && character >= '\u0000') {
                    GL11.glBegin((int)4);
                    this.drawChar(currentData, character, (float)x, (float)y);
                    GL11.glEnd();
                    if (strikethrough) {
                        this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
                    }
                    if (underline) {
                        this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
                    }
                    x += (double)(currentData[character].width - 8 + this.charOffset);
                }
                ++i;
            }
            GL11.glHint((int)3155, (int)4352);
            GL11.glPopMatrix();
        }
        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        int i = 0;
        while (i < size) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
            } else if (character < currentData.length && character >= '\u0000') {
                width += currentData[character].width - 8 + this.charOffset;
            }
            ++i;
        }
        return width / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            int lastColorCode = 65535;
            String[] arrstring = words;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String word = arrstring[n2];
                int i = 0;
                while (i < word.toCharArray().length) {
                    char c = word.toCharArray()[i];
                    if (c == '\u00a7' && i < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[i + 1];
                    }
                    ++i;
                }
                if ((double)this.getStringWidth(String.valueOf(currentWord) + word + " ") < width) {
                    currentWord = String.valueOf(currentWord) + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = String.valueOf(167 + lastColorCode) + word + " ";
                }
                ++n2;
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add(String.valueOf(167 + lastColorCode) + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        int lastColorCode = 65535;
        char[] chars = string.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c == '\u00a7' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            if ((double)this.getStringWidth(String.valueOf(currentWord) + c) < width) {
                currentWord = String.valueOf(currentWord) + c;
            } else {
                finalWords.add(currentWord);
                currentWord = String.valueOf(167 + lastColorCode) + String.valueOf(c);
            }
            ++i;
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        int index = 0;
        while (index < 32) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
            ++index;
        }
    }

    public float Ex(String text, double x2, double y2, int color) {
		float shadowWidth = this.drawString(text, x2 + 0.7, y2 + 0.6, color, true);
		return Math.max(shadowWidth, this.drawString(text, x2, y2, color, false));
	}

    public void drawStringWithOutline(String text, float x, float y, int color) {
        drawString(text, x - .5f, y, 0x000000);
        drawString(text, x + .5f, y, 0x000000);
        drawString(text, x, y - .5f, 0x000000);
        drawString(text, x, y + .5f, 0x000000);
        drawString(text, x, y, color);
    }

}

