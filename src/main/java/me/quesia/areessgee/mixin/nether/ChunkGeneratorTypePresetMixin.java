package me.quesia.areessgee.mixin.nether;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;
import java.util.Optional;

@Mixin(ChunkGeneratorType.Preset.class)
public class ChunkGeneratorTypePresetMixin {
    /**
     * @author falu
     * @reason More open nether terrain.
     */
    @Overwrite
    private static ChunkGeneratorType createCavesType(StructuresConfig config, BlockState defaultBlock, BlockState defaultFluid, ChunkGeneratorType.Preset preset) {
        Map<StructureFeature<?>, StructureConfig> map = Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES);
        map.put(StructureFeature.RUINED_PORTAL, new StructureConfig(25, 10, 34222645));
        return new ChunkGeneratorType(new StructuresConfig(Optional.ofNullable(config.getStronghold()), map), new NoiseConfig(128, new NoiseSamplingConfig(1.0D, 3.0D, 80.0D, 40.0D), new SlideConfig(120, 3, 0), new SlideConfig(320, 4, -1), 2, 2, 0.0D, 0.0D, false, false, false, false), defaultBlock, defaultFluid, 0, 0, 32, false, Optional.of(preset));
    }
}
