package me.falu.areessgee.mixin.access;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {
    @Accessor("field_24015")
    Either<Identifier, Structure> getStructure();

    @Accessor("processors")
    ImmutableList<StructureProcessor> getProcessors();

    @Mutable
    @Accessor("processors")
    void setProcessors(ImmutableList<StructureProcessor> processors);
}
