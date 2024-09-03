package me.falu.areessgee.mixin.nether;

import me.falu.areessgee.AreEssGee;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(StructureFeature.class)
public abstract class StructureFeatureMixin<C extends FeatureConfig> {
    @Shadow
    public abstract String getName();
    @Shadow
    protected abstract boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, C featureConfig);

    @Unique
    private boolean isAffected() {
        return AreEssGee.AFFECTED_STRUCTURES.contains(this.getName().toLowerCase(Locale.ROOT));
    }

    @Inject(method = "method_27218", at = @At("RETURN"), cancellable = true)
    private void customChunkPos(CallbackInfoReturnable<ChunkPos> cir) {
        if (this.isAffected()) {
            cir.setReturnValue(AreEssGee.calculateChunkPos(cir.getReturnValue().x, cir.getReturnValue().z));
        }
    }

    @Inject(method = "locateStructure", at = @At("HEAD"), cancellable = true)
    private void cancelLocate(CallbackInfoReturnable<BlockPos> cir) {
        if (this.isAffected()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    @Redirect(
            method = "method_28657",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/StructureFeature;shouldStartAt(Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/source/BiomeSource;JLnet/minecraft/world/gen/ChunkRandom;IILnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/gen/feature/FeatureConfig;)Z"
            )
    )
    private boolean onlyOne(StructureFeature<C> instance, ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, C featureConfig) {
        if (this.isAffected()) {
            if (AreEssGee.PLACED_STRUCTURES.contains(instance)) {
                return false;
            }
            AreEssGee.PLACED_STRUCTURES.add(instance);
            AreEssGee.USED_QUADRANTS.add(AreEssGee.calculateQuadrant(i, j));
            AreEssGee.log("Generated " + this.getName() + " at [" + i + ", " + j + "]");
            return true;
        }
        return this.shouldStartAt(chunkGenerator, biomeSource, l, chunkRandom, i, j, biome, chunkPos, featureConfig);
    }
}
