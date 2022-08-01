package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.movement.InvMove;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.player.InvManager;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtil;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

import java.awt.*;

@ElementInfo(name = "KeyBinds", blur = true)
public class KeyBinds extends Element {
    private IntegerValue xValue = new IntegerValue("X", 125, 0, 1000);
    private IntegerValue yValue = new IntegerValue("Y", 125, 0, 1000);
    private IntegerValue OnX = new IntegerValue("OnX", 76, 0, 100);
    private IntegerValue OnY = new IntegerValue("OnY", 12, 0, 100);
    private IntegerValue BindsY = new IntegerValue("BindsY", 1, 0, 100);
    private IntegerValue BindsX = new IntegerValue("BindsX", 17, 0, 100);
    private IntegerValue redValue = new IntegerValue("Red", 100, 0, 255);
    private IntegerValue greenValue = new IntegerValue("Green", 100, 0, 255);
    private IntegerValue blueValue = new IntegerValue("Blue", 255, 0, 255);
    private IntegerValue XianValue = new IntegerValue("XianValue", 1, 0, 10);
    private FloatValue XianWidth = new FloatValue("XianWidth", 2.68F, 0, 10);




    public Border drawElement(float partialTicks) {
        RenderUtil.drawBorderedRect((float)xValue.get(), (float)yValue.get(), (float)xValue.get() + 85, (float)yValue.get() - XianValue.get(),XianWidth.get(), new Color(redValue.get(), greenValue.get(), blueValue.get(), 255).getRGB(), new Color(redValue.get(), greenValue.get(), blueValue.get(), 255).getRGB());
        //  RenderUtil.drawBorderedRect((float)xValue.get(), (float)yValue.get(), (float)xValue.get() + 85, (float)yValue.get() + 110, XianWidth.get(), new Color(20,20,20, 220).getRGB(), new Color(20,20,20, 220).getRGB());
        RenderUtil.drawBorderedRect((float)xValue.get(), (float)yValue.get(), (float)xValue.get() + 85, (float)yValue.get() + 10, XianWidth.get(), new Color(31,43,33, 174).getRGB(), new Color(31,43,33, 182).getRGB());
        RenderUtil.drawBorderedRect((float) xValue.get(), (float) yValue.get(), (float)xValue.get() + 85, (float) yValue.get() + 110, 1f, new Color(0,0,0, 124).getRGB(), new Color(0,0,0, 128).getRGB());
        Fonts.font35.drawStringWithShadow(" Binds",xValue.get()+BindsX.get()+25,yValue.get()+BindsY.get()+1,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" Aura",xValue.get()+10,yValue.get()+12,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" Scaffold",xValue.get()+17,yValue.get()+23,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" Velocity",xValue.get()+16,yValue.get()+34,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" Fucker",xValue.get()+14,yValue.get()+45,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" LongJump",xValue.get()+22,yValue.get()+56,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" Teams",xValue.get()+13,yValue.get()+67,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" Critcals",xValue.get()+16,yValue.get()+78,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" InvMove",xValue.get()+18,yValue.get()+89,new Color(255,255,255).getRGB());
        Fonts.font35.drawStringWithShadow(" InvClear",xValue.get()+18,yValue.get()+100,new Color(255,255,255).getRGB());
        if (LiquidBounce.moduleManager.getModule(KillAura.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+OnY.get(),new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+OnY.get(),new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(Scaffold.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+76,yValue.get()+23,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+76,yValue.get()+23,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(Velocity.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+34,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+34,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(Fucker.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+45,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+45,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(LongJump.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+56,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+56,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(Teams.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+67,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+67,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(Criticals.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+78,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+78,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(InvMove.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+89,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+89,new Color(255,255,255).getRGB());

        if (LiquidBounce.moduleManager.getModule(InvManager.class).getState()) Fonts.font35.drawStringWithShadow("on",xValue.get()+OnX.get(),yValue.get()+100,new Color(255,255,255).getRGB());else Fonts.font35.drawStringWithShadow("off",xValue.get()+OnX.get(),yValue.get()+100,new Color(255,255,255).getRGB());

        return new Border((float)xValue.get(), (float)yValue.get(), (float)xValue.get() + 85, (float)yValue.get() + 120);
    }
    }
