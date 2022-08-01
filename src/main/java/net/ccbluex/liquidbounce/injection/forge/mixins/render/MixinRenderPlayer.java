package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.PlayerEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Inject(method = "renderLivingAt(Lnet/minecraft/client/entity/AbstractClientPlayer;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;renderLivingAt(Lnet/minecraft/entity/EntityLivingBase;DDD)V", ordinal = 1))
    protected void renderLivingAt(AbstractClientPlayer p_renderLivingAt_1_, double p_renderLivingAt_2_, double p_renderLivingAt_3_, double p_renderLivingAt_4_, CallbackInfo ci) {
        if (LiquidBounce.moduleManager.getModule(PlayerEdit.class).getState()) {
            if (p_renderLivingAt_1_ == Minecraft.getMinecraft().thePlayer) {
                PlayerEdit player = LiquidBounce.moduleManager.getModule(PlayerEdit.class);
                GlStateManager.scale(player.getSize().get(), player.getSize().get(), player.getSize().get());
            }
        }
    }
}
