package me.quesia.areessgee.mixin.entities.ai;

import com.google.common.collect.Lists;
import me.quesia.areessgee.AreEssGee;
import me.quesia.areessgee.PiglinBarterState;
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
import java.util.Random;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @Inject(method = "getBarteredItem", at = @At("RETURN"), cancellable = true)
    private static void rankedTrades(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (AreEssGee.RANKED_TRADES) {
            MinecraftServer server = piglin.getServer();
            if (server == null || cir.getReturnValue().size() == 0) {
                return;
            }
            ServerWorld overworld = server.getOverworld();
            PiglinBarterState piglinBarterState = overworld.getPersistentStateManager().getOrCreate(PiglinBarterState::new, "piglin_barters");
            cir.setReturnValue(Lists.newArrayList(piglinBarterState.guaranteeItem(piglin, cir.getReturnValue().get(0), new Random())));
        }
    }
}
