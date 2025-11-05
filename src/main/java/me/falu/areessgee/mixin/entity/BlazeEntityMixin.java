package me.falu.areessgee.mixin.entity;

import me.falu.areessgee.AreEssGee;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlazeEntity.class)
public abstract class BlazeEntityMixin extends HostileEntity {
    protected BlazeEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private void dropFakeStack(ItemStack stack) {
        int amount = stack.getCount();
        if (amount != 0) {
            amount--;
        }

        if (this.random.nextFloat() >= AreEssGee.CONFIG.rodRarity) {
            amount++;
        }

        stack.setCount(amount);
        this.dropStack(stack);
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        if (this.world.isClient || this.world.getServer() == null) {
            return;
        }

        Identifier identifier = this.getLootTable();
        LootTable lootTable = this.world.getServer().getLootManager().getTable(identifier);
        LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
        lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), this::dropFakeStack);
    }
}
