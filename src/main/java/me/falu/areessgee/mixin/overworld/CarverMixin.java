package me.falu.areessgee.mixin.overworld;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Carver.class)
public abstract class CarverMixin {
    @Shadow @Final public static Carver<ProbabilityConfig> UNDERWATER_CANYON;
    @Shadow @Final public static Carver<ProbabilityConfig> UNDERWATER_CAVE;

    @Shadow
    public abstract Codec<ConfiguredCarver<ProbabilityConfig>> getCodec();

    @Redirect(method = "carveRegion", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I", ordinal = 1))
    private int deeperRavines(int a, int b) {
        if (
                this.getCodec() == UNDERWATER_CANYON.getCodec() ||
                this.getCodec() == UNDERWATER_CAVE.getCodec()
        ) {
            return 1;
        }
        return Math.max(a, b);
    }
}
