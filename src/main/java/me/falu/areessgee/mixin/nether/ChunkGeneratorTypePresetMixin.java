package me.falu.areessgee.mixin.nether;

import com.google.common.collect.Maps;
import me.falu.areessgee.AreEssGee;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Mixin(ChunkGeneratorType.Preset.class)
public class ChunkGeneratorTypePresetMixin {
    @Unique private Function<ChunkGeneratorType.Preset, ChunkGeneratorType> generatorTypeGetter;

    @Inject(method = "createCavesType", at = @At("HEAD"), cancellable = true)
    private static void createCavesType(StructuresConfig config, BlockState defaultBlock, BlockState defaultFluid, ChunkGeneratorType.Preset preset, CallbackInfoReturnable<ChunkGeneratorType> cir) {
        if (AreEssGee.CONFIG.openNetherTerrain) {
            Map<StructureFeature<?>, StructureConfig> map = Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES);
            map.put(StructureFeature.RUINED_PORTAL, new StructureConfig(25, 10, 34222645));
            cir.setReturnValue(new ChunkGeneratorType(new StructuresConfig(Optional.ofNullable(config.getStronghold()), map), new NoiseConfig(128, new NoiseSamplingConfig(1.0D, 3.0D, 80.0D, 40.0D), new SlideConfig(120, 3, 0), new SlideConfig(320, 4, -1), 2, 2, 0.0D, 0.0D, false, false, false, false), defaultBlock, defaultFluid, 0, 0, 32, false, Optional.of(preset)));
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setGeneratorTypeGetter(String id, Function<ChunkGeneratorType.Preset, ChunkGeneratorType> generatorTypeGetter, CallbackInfo ci) {
        this.generatorTypeGetter = generatorTypeGetter;
    }

    @Inject(method = "getChunkGeneratorType", at = @At("RETURN"), cancellable = true)
    private void alwaysCreateType(CallbackInfoReturnable<ChunkGeneratorType> cir) {
        cir.setReturnValue(this.generatorTypeGetter.apply((ChunkGeneratorType.Preset) (Object) this));
    }
}
