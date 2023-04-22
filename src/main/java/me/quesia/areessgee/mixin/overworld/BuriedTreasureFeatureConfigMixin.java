package me.quesia.areessgee.mixin.overworld;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.world.gen.feature.BuriedTreasureFeatureConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuriedTreasureFeatureConfig.class)
public class BuriedTreasureFeatureConfigMixin {
    @Mutable @Shadow @Final public float probability;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void moreBt(float probability, CallbackInfo ci) {
        this.probability = AreEssGee.BURIED_TREASURE_RARITY;
    }
}
