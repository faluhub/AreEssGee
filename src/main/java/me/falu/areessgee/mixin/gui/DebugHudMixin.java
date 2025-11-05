package me.falu.areessgee.mixin.gui;

import me.contaria.speedrunapi.config.api.SpeedrunOption;
import me.falu.areessgee.AreEssGee;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Inject(method = "getRightText", at = @At("RETURN"), cancellable = true)
    private void addDebugText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();

        list.add("");
        list.add("CHEATING! v" + AreEssGee.MOD_CONTAINER.getMetadata().getVersion().getFriendlyString());

        boolean illegal = false;
        for (SpeedrunOption<?> option : AreEssGee.CONFIG.container.getOptions()) {
            if (option.hasDefault() && !option.getDefault().equals(option.get())) {
                illegal = true;
                break;
            }
        }
        list.add("Illegal: " + illegal);

        cir.setReturnValue(list);
    }
}
