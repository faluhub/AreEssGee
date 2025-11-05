package me.falu.areessgee.mixin.overworld.stronghold;

import me.falu.areessgee.AreEssGee;
import net.minecraft.structure.StrongholdGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StrongholdGenerator.PieceSetting.class)
public class PieceSettingMixin {
    @Mutable @Shadow @Final public int limit;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyMaxRooms(Class<?> class_, int i, int j, CallbackInfo ci) {
        if (AreEssGee.CONFIG.maxStrongholdDepth <= 0) {
            return;
        }

        this.limit = Math.min(this.limit, AreEssGee.CONFIG.maxStrongholdDepth);
    }
}
