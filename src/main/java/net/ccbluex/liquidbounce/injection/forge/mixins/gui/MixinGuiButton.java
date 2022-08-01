
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.color.HUD;
import net.ccbluex.liquidbounce.features.module.modules.client.button.AbstractButtonRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {

    @Shadow
    public int xPosition;

    @Shadow
    public int yPosition;

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow
    public boolean hovered;

    @Shadow
    public boolean enabled;

    @Shadow
    public boolean visible;

    @Shadow
    protected static ResourceLocation buttonTextures;

    @Shadow
    public abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

    private AbstractButtonRenderer buttonRenderer = LiquidBounce.moduleManager.getModule(HUD.class).getButtonRenderer((GuiButton)(Object)this);

    /**
     * @author liuli
     */
    @Inject(method = "drawButton", at = @At("HEAD"), cancellable = true)
    public void drawButton(Minecraft mc, int mouseX, int mouseY, CallbackInfo ci) {
        if(this.buttonRenderer != null) {
            if(!visible) {
                return;
            }
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.mouseDragged(mc, mouseX, mouseY);
            buttonRenderer.render(mouseX, mouseY, mc);
            buttonRenderer.drawButtonText(mc);
            ci.cancel();
        }
    }
}