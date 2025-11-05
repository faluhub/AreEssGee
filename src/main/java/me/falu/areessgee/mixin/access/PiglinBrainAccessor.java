package me.falu.areessgee.mixin.access;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(PiglinBrain.class)
public interface PiglinBrainAccessor {
    @Invoker("getBarteredItem")
    static List<ItemStack> getBarteredItem(PiglinEntity ignored) {
        throw new ArithmeticException();
    }
}
