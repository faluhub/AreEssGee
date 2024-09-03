package me.falu.areessgee.mixin;

import com.google.common.collect.ImmutableList;
import me.falu.areessgee.AreEssGee;
import me.falu.areessgee.IStructurePoolRegistry;
import me.falu.areessgee.mixin.access.RuleStructureProcessorAccessor;
import me.falu.areessgee.mixin.access.SinglePoolElementAccessor;
import me.falu.areessgee.mixin.access.StructurePoolAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePoolRegistry;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(StructurePoolRegistry.class)
public class StructurePoolRegistryMixin implements IStructurePoolRegistry {
    @Shadow @Final private Map<Identifier, StructurePool> pools;

    @Override
    public void areessgee$resetTreasureData() {
        StructurePool treasurePool = this.pools.get(new Identifier("bastion/treasure/ramparts"));
        StructurePoolElement element = ((StructurePoolAccessor) treasurePool).getElements().get(3);
        if (element instanceof SinglePoolElement) {
            SinglePoolElementAccessor poolElement = (SinglePoolElementAccessor) element;
            Optional<Identifier> structure = ((SinglePoolElementAccessor) element).getStructure().left();
            if (structure.isPresent() && structure.get().getPath().equals("bastion/treasure/ramparts/top_wall")) {
                RuleStructureProcessor goldProcessor = (RuleStructureProcessor) poolElement.getProcessors().get(0);
                List<StructureProcessorRule> rules = new ArrayList<>(((RuleStructureProcessorAccessor) goldProcessor).getRules());
                rules.set(0, new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GOLD_BLOCK, AreEssGee.CONFIG.treasureGoldRarity), AlwaysTrueRuleTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState()));
                ((RuleStructureProcessorAccessor) goldProcessor).setRules(ImmutableList.copyOf(rules));
                poolElement.setProcessors(ImmutableList.of(goldProcessor));
            }
        }
    }
}
