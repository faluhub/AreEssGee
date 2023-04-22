package me.quesia.areessgee.mixin.nether.biomes;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin {
    @Inject(method = "getBiomeForNoiseGen", at = @At("RETURN"), cancellable = true)
    private void removeBasaltRegion(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> cir) {
        if (cir.getReturnValue().equals(Biomes.BASALT_DELTAS)) {
            int limit = AreEssGee.ANTI_BASALT_REGION_SIZE;
            if ((biomeX <= 0 && biomeX > -limit) || (biomeX >= 0 && biomeX < limit) || (biomeZ <= 0 && biomeZ > -limit) || (biomeZ >= 0 && biomeZ < limit)) {
                String biomeName = AreEssGee.ANTI_BASALT_REPLACEMENT;
                Biome biome = Registry.BIOME.get(new Identifier(biomeName.toLowerCase(Locale.ROOT)));
                cir.setReturnValue(biome);
            }
        }
    }
}
