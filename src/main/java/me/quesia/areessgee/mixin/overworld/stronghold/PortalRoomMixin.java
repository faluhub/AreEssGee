package me.quesia.areessgee.mixin.overworld.stronghold;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.structure.StrongholdGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StrongholdGenerator.PortalRoom.class)
public class PortalRoomMixin {
    @ModifyConstant(method = "generate", constant = @Constant(floatValue = 0.9F, ordinal = 0))
    private float higherEyeChance(float constant) {
        return AreEssGee.EYE_ODDS;
    }
}
