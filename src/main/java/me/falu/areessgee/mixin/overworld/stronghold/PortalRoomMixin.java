package me.falu.areessgee.mixin.overworld.stronghold;

import me.falu.areessgee.AreEssGee;
import net.minecraft.structure.StrongholdGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StrongholdGenerator.PortalRoom.class)
public class PortalRoomMixin {
    @ModifyConstant(method = "generate", constant = @Constant(floatValue = 0.9F, ordinal = 0))
    private float higherEyeChance(float constant) {
        return AreEssGee.CONFIG.portalEyeRarity;
    }
}
