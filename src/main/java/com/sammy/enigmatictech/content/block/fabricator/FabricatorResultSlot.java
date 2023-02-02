package com.sammy.enigmatictech.content.block.fabricator;

import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.setup.ETRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class FabricatorResultSlot extends ResultSlot {
    private final AbstractFabricatorMenu menu;
    public FabricatorResultSlot(Player pPlayer, AbstractFabricatorMenu menu, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pPlayer, menu.craftSlots, pContainer, pSlot, pXPosition, pYPosition);
        this.menu = menu;
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
        Level level = pPlayer.level;
        RecipeManager recipeManager = level.getRecipeManager();
        boolean isFabricating = recipeManager.getRecipeFor(ETRecipeTypes.FABRICATING.get(), menu.craftSlots, level).isPresent();
        final RecipeType<? extends CraftingRecipe> recipeType = isFabricating ? ETRecipeTypes.FABRICATING.get() : RecipeType.CRAFTING;
        NonNullList<ItemStack> nonnulllist = recipeManager.getRemainingItemsFor(recipeType, this.craftSlots, level);


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