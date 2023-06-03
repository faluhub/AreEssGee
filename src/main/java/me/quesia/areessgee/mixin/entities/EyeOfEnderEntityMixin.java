package me.quesia.areessgee.mixin.entities;

import me.quesia.areessgee.AreEssGee;
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
        if (AreEssGee.GUARANTEE_EYE_DROPS.getValue()) {
            this.dropsItem = true;
        }
    }
}
