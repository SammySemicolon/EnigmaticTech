package com.sammy.enigmatictech.content.block.fabricator;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class FabricatorResultSlot extends ResultSlot {
    private final FabricatorCraftingMenu menu;
    public FabricatorResultSlot(Player pPlayer, FabricatorCraftingMenu menu, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pPlayer, menu.craftSlots, pContainer, pSlot, pXPosition, pYPosition);
        this.menu = menu;
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
        NonNullList<ItemStack> nonnulllist = pPlayer.level.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, pPlayer.level);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        menu.craftSlots.setCrafting(true);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftSlots.getItem(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.craftSlots.removeItem(i, 1);
                itemstack = this.craftSlots.getItem(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftSlots.setItem(i, itemstack1);
                } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftSlots.setItem(i, itemstack1);
                } else if (!pPlayer.getInventory().add(itemstack1)) {
                    pPlayer.drop(itemstack1, false);
                }
            }
        }
        menu.craftSlots.setCrafting(false);
        menu.slotsChanged(menu.craftSlots);

    }
}