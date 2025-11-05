package me.falu.areessgee.world;

import com.google.common.collect.Lists;
import me.falu.areessgee.mixin.access.PiglinBrainAccessor;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author RedLime
 */
public class WorldPiglinBarterState extends PersistentState {
    private static final int MAX_GUARANTEE = 72;
    private static final int MAX_PEARL_COUNT = 3;
    private static final int MAX_OBSIDIAN_COUNT = 6;
    private final List<Integer> pearlTradeIndexes = Lists.newArrayList();
    private final List<Integer> obsidianTradeIndexes = Lists.newArrayList();
    private int currentTrades = Integer.MAX_VALUE;
    private boolean rolling = false;

    public WorldPiglinBarterState() {
        super("piglin_barters");
    }

    public void refreshTradeIndexes(Random random) {
        this.currentTrades = 0;

        List<Integer> numbers = Lists.newArrayList();
        for (int i = 0; i < MAX_GUARANTEE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, random);

        for (int i = 0; i < MAX_PEARL_COUNT; i++) {
            this.pearlTradeIndexes.add(numbers.remove(0));
        }

        for (int i = 0; i < MAX_OBSIDIAN_COUNT; i++) {
            this.obsidianTradeIndexes.add(numbers.remove(0));
        }
    }

    public ItemStack guaranteeItem(PiglinEntity piglin, ItemStack itemStack, Random random) {
        ItemStack newItem = this.guaranteeItem2(piglin, itemStack, random);

        if (!this.rolling) {
            this.currentTrades++;
        }

        return newItem;
    }

    private ItemStack guaranteeItem2(PiglinEntity piglin, ItemStack itemStack, Random random) {
        if (this.currentTrades >= MAX_GUARANTEE) {
            this.refreshTradeIndexes(random);
        }

        if (itemStack.getItem() == Items.ENDER_PEARL) {
            if (this.pearlTradeIndexes.isEmpty()) {
                this.rolling = true;
                List<ItemStack> newBarterItem = PiglinBrainAccessor.getBarteredItem(piglin);
                this.rolling = false;

                return newBarterItem.get(0);
            }

            this.pearlTradeIndexes.remove(0);
            return itemStack;
        }

        if (itemStack.getItem() == Items.OBSIDIAN) {
            if (!this.obsidianTradeIndexes.isEmpty()) {
                this.obsidianTradeIndexes.remove(0);
            }

            return itemStack;
        }

        int pearlIndex = this.pearlTradeIndexes.indexOf(this.currentTrades);
        if (pearlIndex != -1) {
            if (!this.rolling) {
                this.pearlTradeIndexes.remove(pearlIndex);
            }
            return new ItemStack(Items.ENDER_PEARL, random.nextInt(5) + 4);
        }

        int obbyIndex = this.obsidianTradeIndexes.indexOf(this.currentTrades);
        if (obbyIndex != -1) {
            if (!this.rolling) {
                this.obsidianTradeIndexes.remove(obbyIndex);
            }
            return new ItemStack(Items.OBSIDIAN);
        }

        return itemStack;
    }

    public void fromTag(CompoundTag tag) {
        if (!tag.contains("obsidianIndexes")) {
            return;
        }

        for (int index : tag.getIntArray("obsidianIndexes")) {
            this.obsidianTradeIndexes.add(index);
        }

        for (int index : tag.getIntArray("pearlIndexes")) {
            this.pearlTradeIndexes.add(index);
        }

        this.currentTrades = tag.getInt("currentGuarantee");
        this.rolling = tag.getBoolean("preventIncrease");
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putIntArray("obsidianIndexes", this.obsidianTradeIndexes);
        tag.putIntArray("pearlIndexes", this.pearlTradeIndexes);
        tag.putInt("currentGuarantee", this.currentTrades);
        tag.putBoolean("preventIncrease", this.rolling);
        return tag;
    }
}