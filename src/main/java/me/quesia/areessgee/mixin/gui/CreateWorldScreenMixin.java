package me.quesia.areessgee.mixin.gui;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Shadow @Final public MoreOptionsDialog moreOptionsDialog;
    @Shadow public boolean hardcore;

    @Inject(method = "createLevel", at = @At("HEAD"))
    private void callEvent(CallbackInfo ci) {
        AreEssGee.onWorldCreate(this.moreOptionsDialog.getGeneratorOptions(this.hardcore).getSeed());
    }
}
