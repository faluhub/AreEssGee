package me.falu.areessgee.mixin.overworld;

import me.falu.areessgee.AreEssGee;
import net.minecraft.world.gen.feature.BuriedTreasureFeature;
import net.minecraft.world.gen.feature.BuriedTreasureFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuriedTreasureFeature.class)
public class BuriedTreasureFeatureMixin {
    @Redirect(
            method = "shouldStartAt(Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/source/BiomeSource;JLnet/minecraft/world/gen/ChunkRandom;IILnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/gen/feature/BuriedTreasureFeatureConfig;)Z",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/gen/feature/BuriedTreasureFeatureConfig;probability:F"
            )
    )
    private float customProbability(BuriedTreasureFeatureConfig instance) {
        return AreEssGee.CONFIG.buriedTreasureRarity;
    }
}
