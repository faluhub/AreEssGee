package me.falu.areessgee.mixin.gui;

import me.falu.areessgee.AreEssGee;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Inject(method = "createLevel", at = @At("HEAD"))
    private void callEvent(CallbackInfo ci) {
        AreEssGee.onWorldCreate();
    }
}
