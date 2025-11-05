package me.falu.areessgee.mixin.nether;

import me.falu.areessgee.AreEssGee;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(MobSpawnerLogic.class)
public class MobSpawnerLogicMixin {
    @Redirect(method = "updateSpawns", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int applySpawnDelayFactor(Random instance, int bound) {
        return (int) (instance.nextInt(bound) * AreEssGee.CONFIG.spawnerDelayFactor);
    }
}
