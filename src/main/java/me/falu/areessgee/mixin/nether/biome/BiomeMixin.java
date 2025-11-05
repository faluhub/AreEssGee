package me.falu.areessgee.mixin.nether.biome;

import me.falu.areessgee.owner.StructureFeatureOwner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Biome.class)
public class BiomeMixin implements StructureFeatureOwner {
    @Shadow @Final private Map<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> structureFeatures;

    @Override
    public void areessgee$removeStructureFeature(ConfiguredStructureFeature<?, ?> configuredStructureFeature) {
        this.structureFeatures.remove(configuredStructureFeature.field_24835);
    }
}
