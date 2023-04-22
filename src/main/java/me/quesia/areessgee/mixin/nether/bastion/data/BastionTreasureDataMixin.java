package me.quesia.areessgee.mixin.nether.bastion.data;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.structure.BastionTreasureData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BastionTreasureData.class)
public class BastionTreasureDataMixin {
    @ModifyConstant(method = "<clinit>", constant = @Constant(floatValue = 0.3f, ordinal = 3))
    private static float moreGold(float constant) {
        return AreEssGee.TREASURE_BASTION_GOLD_BLOCK_RARITY;
    }
}
