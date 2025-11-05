package me.falu.areessgee.mixin.nether.biome;

import me.falu.areessgee.AreEssGee;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin {
    @Inject(method = "getBiomeForNoiseGen", at = @At("RETURN"), cancellable = true)
    private void removeBasaltRegion(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
        if (!cir.getReturnValue().equals(Biomes.BASALT_DELTAS)) {
            return;
        }

        int limit = AreEssGee.CONFIG.basaltReplacementRange;
        boolean inRangeX = (biomeX <= 0 && biomeX > -limit) || (biomeX >= 0 && biomeX < limit);
        boolean inRangeZ = (biomeZ <= 0 && biomeZ > -limit) || (biomeZ >= 0 && biomeZ < limit);
        if (!inRangeX && !inRangeZ) {
            return;
        }

        Biome biome = Registry.BIOME.get(new Identifier(AreEssGee.CONFIG.basaltReplacement.toLowerCase()));
        cir.setReturnValue(biome == null ? Biomes.NETHER_WASTES : biome);
    }
}
