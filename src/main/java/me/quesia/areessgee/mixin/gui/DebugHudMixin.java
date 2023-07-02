package me.quesia.areessgee.mixin.gui;

import com.google.common.collect.Lists;
import me.quesia.areessgee.AreEssGee;
import me.quesia.areessgee.config.ConfigValue;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    private static final List<ConfigValue<?>> RNG_VALUES = Lists.newArrayList(AreEssGee.FLINT_MINIMUM_VALUE, AreEssGee.ROD_MINIMUM_VALUE, AreEssGee.GUARANTEE_EYE_DROPS);
    private static boolean HAS_MODIFIED_OTHER = false;

    static {
        for (Field field : AreEssGee.class.getFields()) {
            if (field.getType().equals(ConfigValue.class)) {
                try {
                    ConfigValue<?> value = (ConfigValue<?>) field.get(null);
                    if (!RNG_VALUES.contains(value) && value.hasChanged()) {
                        HAS_MODIFIED_OTHER = true;
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }
    }

    @Inject(method = "getRightText", at = @At("RETURN"), cancellable = true)
    private void addDebugText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();
        list.add("");
        list.add("CHEATING! v" + AreEssGee.MOD_CONTAINER.getMetadata().getVersion().getFriendlyString());
        list.add(Boolean.toString(HAS_MODIFIED_OTHER));
        cir.setReturnValue(list);
    }
}
