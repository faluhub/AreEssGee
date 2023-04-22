package me.quesia.areessgee.mixin.nether;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(StructureFeature.class)
public abstract class StructureFeatureMixin<C extends FeatureConfig> {
    @Shadow public abstract String getName();
    @Shadow protected abstract boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, C featureConfig);

    private boolean isAffected() {
        return AreEssGee.AFFECTED_STRUCTURES.contains(this.getName().toLowerCase(Locale.ROOT));
    }

    @Inject(method = "method_27218", at = @At("RETURN"), cancellable = true)
    private void customChunkPos(StructureConfig structureConfig, long l, ChunkRandom chunkRandom, int i, int j, CallbackInfoReturnable<ChunkPos> cir) {
        if (this.isAffected()) {
            cir.setReturnValue(AreEssGee.calculateChunkPos(cir.getReturnValue().x, cir.getReturnValue().z));
        }
    }

    @Redirect(
            method = "locateStructure",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldView;getChunk(IILnet/minecraft/world/chunk/ChunkStatus;)Lnet/minecraft/world/chunk/Chunk;"
            )
    )
    private Chunk getCustomChunk(WorldView instance, int chunkX, int chunkZ, ChunkStatus status) {
        if (this.isAffected()) { status = Registry.CHUNK_STATUS.get(new Identifier("fake_structure_starts")); }
        return instance.getChunk(chunkX, chunkZ, status);
    }

    @Redirect(
            method = "method_28657",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/StructureFeature;shouldStartAt(Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/source/BiomeSource;JLnet/minecraft/world/gen/ChunkRandom;IILnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/gen/feature/FeatureConfig;)Z"
            )
    )
    private boolean onlyOne(StructureFeature<C> instance, ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, C featureConfig) {
        boolean shouldGenerate = this.shouldStartAt(chunkGenerator, biomeSource, l, chunkRandom, i, j, biome, chunkPos, featureConfig);
        if (this.isAffected()) {
            if (AreEssGee.PLACED_STRUCTURES.contains(instance)) {
                return false;
            }

            if (!shouldGenerate && AreEssGee.AFFECTED_STRUCTURES.size() - AreEssGee.PLACED_STRUCTURES.size() == 1) {
                shouldGenerate = true;
            }

            if (shouldGenerate) {
                AreEssGee.PLACED_STRUCTURES.add(instance);
                AreEssGee.USED_QUADRANTS.add(AreEssGee.calculateQuadrant(i, j));
                AreEssGee.log("Generated " + this.getName() + " at [" + i + ", " + j + "]");
            }
        }
        return shouldGenerate;
    }
}
