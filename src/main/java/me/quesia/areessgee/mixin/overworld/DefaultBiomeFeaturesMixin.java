package me.quesia.areessgee.mixin.overworld;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
    @Redirect(
            method = "addOceanCarvers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/biome/Biome;addCarver(Lnet/minecraft/world/gen/GenerationStep$Carver;Lnet/minecraft/world/gen/carver/ConfiguredCarver;)V",
                    ordinal = 2
            )
    )
    private static void moreOceanRavines(Biome instance, GenerationStep.Carver step, ConfiguredCarver<?> configuredCarver) {
        instance.addCarver(step, Biome.configureCarver(Carver.UNDERWATER_CANYON, new ProbabilityConfig(AreEssGee.OCEAN_CANYON_RARITY.getValue())));
    }
}
