package me.falu.areessgee.mixin.gui;

import me.falu.areessgee.AreEssGee;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.Pair;
import org.mcsr.speedrunapi.config.api.SpeedrunOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @SuppressWarnings("all")
    @Unique private static final Pair<String, ?>[] CHECKS = new Pair[] {
            new Pair<>("randomOffset", 3),
            new Pair<>("randomBound", 4),
            new Pair<>("buriedTreasureRarity", 0.07F),
            new Pair<>("oceanRavineRarity", 0.04F),
            new Pair<>("treasureGoldRarity", 0.2F),
            new Pair<>("portalEyeRarity", 0.9F),
            new Pair<>("maxStrongholdDepth", 8)
    };

    @Inject(method = "getRightText", at = @At("RETURN"), cancellable = true)
    private void addDebugText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();
        list.add("");
        list.add("CHEATING! v" + AreEssGee.MOD_CONTAINER.getMetadata().getVersion().getFriendlyString());
        for (SpeedrunOption<?> option : AreEssGee.CONFIG.container.getOptions()) {
            Optional<Pair<String, ?>> check = Arrays.stream(CHECKS).filter(v -> v.getLeft().equals(option.getID())).findAny();
            if (check.isPresent() && !check.get().getRight().equals(option.get())) {
                list.add("Illegal: true");
                cir.setReturnValue(list);
                return;
            }
        }
        list.add("Illegal: false");
        cir.setReturnValue(list);
    }
}
