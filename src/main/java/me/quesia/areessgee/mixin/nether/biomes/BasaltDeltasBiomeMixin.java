package me.quesia.areessgee.mixin.nether.biomes;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.world.biome.BasaltDeltasBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasaltDeltasBiome.class)
public abstract class BasaltDeltasBiomeMixin extends Biome {
    protected BasaltDeltasBiomeMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addBastion(CallbackInfo ci) {
        if (AreEssGee.ADD_BASALT_BASTIONS.getValue()) {
            this.addStructureFeature(DefaultBiomeFeatures.BASTION_REMNANT);
        }
    }
}
