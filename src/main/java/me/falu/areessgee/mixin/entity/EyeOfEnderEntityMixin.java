package me.falu.areessgee.mixin.entity;

import me.falu.areessgee.AreEssGee;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnderEntity.class)
public class EyeOfEnderEntityMixin {
    @Shadow private boolean dropsItem;

    @Inject(method = "moveTowards", at = @At("TAIL"))
    private void neverBreak(BlockPos pos, CallbackInfo ci) {
        if (AreEssGee.CONFIG.guaranteeEyeDrops) {
            // We set this explicitly to true instead of the value of the config option,
            // because that would mean if the user has this option disabled it would never drop an eye,
            // instead we want to preserve vanilla behavior if it's disabled.
            this.dropsItem = true;
        }
    }
}
