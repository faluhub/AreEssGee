package me.falu.areessgee.mixin.access;

import com.google.common.collect.ImmutableList;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RuleStructureProcessor.class)
public interface RuleStructureProcessorAccessor {
    @Accessor("rules")
    ImmutableList<StructureProcessorRule> getRules();

    @Mutable
    @Accessor("rules")
    void setRules(ImmutableList<StructureProcessorRule> rules);
}
