package me.falu.areessgee.mixin.overworld;

import me.falu.areessgee.AreEssGee;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.RavineCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

@Mixin(RavineCarver.class)
public class RavineCarverMixin {
    @Unique private long seed = -1L;

    @Redirect(method = "shouldCarve(Ljava/util/Random;IILnet/minecraft/world/gen/ProbabilityConfig;)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/ProbabilityConfig;probability:F"))
    private float customProbability(ProbabilityConfig instance) {
        return AreEssGee.CONFIG.oceanRavineRarity;
    }

    @Inject(method = "carveRavine", at = @At("HEAD"))
    private void captureSeed(Chunk chunk, Function<BlockPos, Biome> posToBiome, long seed, int seaLevel, int mainChunkX, int mainChunkZ, double x, double y, double z, float width, float yaw, float pitch, int branchStartIndex, int branchCount, double yawPitchRatio, BitSet carvingMask, CallbackInfo ci) {
        this.seed = seed;
    }

    @ModifyVariable(method = "carveRavine", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private float modifyWidth(float value) {
        return 4.5F + (new Random(this.seed).nextFloat(1.0F));
    }
}
