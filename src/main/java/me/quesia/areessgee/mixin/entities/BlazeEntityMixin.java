package me.quesia.areessgee.mixin.entities;

import me.quesia.areessgee.AreEssGee;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(BlazeEntity.class)
public abstract class BlazeEntityMixin extends HostileEntity {
    protected BlazeEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    private void dropFakeStack(ItemStack stack) {
        int amount = stack.getCount();
        if (amount != 0) { amount--; }
        if (new Random().nextFloat() >= AreEssGee.ROD_MINIMUM_VALUE.getValue()) { amount++; }
        this.dropStack(new ItemStack(Items.BLAZE_ROD, amount));
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        if (this.world.getServer() == null) { return; }
        Identifier identifier = this.getLootTable();
        LootTable lootTable = this.world.getServer().getLootManager().getTable(identifier);
        LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
        lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), this::dropFakeStack);
    }
}
