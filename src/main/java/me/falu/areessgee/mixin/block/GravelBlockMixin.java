package me.falu.areessgee.mixin.block;

import com.google.common.collect.Lists;
import me.falu.areessgee.AreEssGee;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.GravelBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Random;

@Mixin(GravelBlock.class)
public abstract class GravelBlockMixin extends FallingBlock {
    public GravelBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        List<ItemStack> original = super.getDroppedStacks(state, builder);

        // If there are no original drops, skip any processing.
        // Omitting this check used to cause a crash in the Practice Seed Mod when an explosion broke a gravel block.
        if (original.isEmpty()) {
            return original;
        }

        // If the original drop is a flint, subtract one (1) from the item count.
        // We subtract instead of assuming an item count of 0 because the player might have used fortune, and we want to keep the original result of that.
        int flintAmount = 0;
        if (original.get(0).getItem().equals(Items.FLINT)) {
            flintAmount = original.get(0).getCount() - 1;
        }

        // Increment the amount of flint by one (1) if a random value exceeds or is equal to the configured flint rarity value.
        if (new Random().nextFloat() >= AreEssGee.CONFIG.flintRarity) {
            flintAmount++;
        }

        // At this point we haven't explicitly stated if we're going to drop a gravel or a flint.
        // We just check if our flint amount isn't 0.
        // If that's the case, our little system has decided the player should receive a custom amount of flint.
        // We just override the result of this deprecated function which for some reason is still considered when calculating block drops.
        if (flintAmount != 0) {
            return Lists.newArrayList(new ItemStack(Items.FLINT, flintAmount));
        }

        return original;
    }
}
