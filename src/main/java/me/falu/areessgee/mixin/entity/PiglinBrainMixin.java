package me.falu.areessgee.mixin.entity;

import com.google.common.collect.Lists;
import me.falu.areessgee.world.WorldPiglinBarterState;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @Inject(method = "getBarteredItem", at = @At("RETURN"), cancellable = true)
    private static void rankedTrades(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        MinecraftServer server = piglin.getServer();
        if (server == null || cir.getReturnValue().isEmpty()) {
            return;
        }

        ServerWorld overworld = server.getOverworld();
        WorldPiglinBarterState piglinBarterState = overworld.getPersistentStateManager().getOrCreate(WorldPiglinBarterState::new, "piglin_barters");
        cir.setReturnValue(Lists.newArrayList(piglinBarterState.guaranteeItem(piglin, cir.getReturnValue().get(0), piglin.getRandom())));
    }
}
