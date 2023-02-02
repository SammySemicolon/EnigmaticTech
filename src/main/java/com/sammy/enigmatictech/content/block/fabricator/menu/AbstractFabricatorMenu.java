package com.sammy.enigmatictech.content.block.fabricator.menu;

import java.util.Objects;
import java.util.Optional;

import com.sammy.enigmatictech.content.block.fabricator.FabricatorBlockEntity;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorCraftingContainer;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorQuickAccessContainer;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorResultSlot;
import com.sammy.enigmatictech.content.block.fabricator.recipe.FabricatorRecipe;
import com.sammy.enigmatictech.setup.ETBlocks;
import com.sammy.enigmatictech.setup.ETMenuTypes;
import com.sammy.enigmatictech.setup.ETRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AbstractFabricatorMenu extends RecipeBookMenu<FabricatorCraftingContainer> {

   protected final ResultContainer resultSlots = new ResultContainer();

   protected final Player player;
   protected final ContainerLevelAccess access;
   public final FabricatorCraftingContainer craftSlots;

   public AbstractFabricatorMenu(MenuType<?> pMenuType, final int containerId, final Inventory playerInventory, ContainerLevelAccess access, ItemStackHandler craftingGridInventory, ItemStackHandler quickAccessInventory) {
      super(pMenuType, containerId);
      this.player = playerInventory.player;
      this.access = access;
      this.craftSlots = new FabricatorCraftingContainer(this, craftingGridInventory);
      FabricatorQuickAccessContainer quickAccessSlots = new FabricatorQuickAccessContainer(this, quickAccessInventory);
      this.addSlot(new FabricatorResultSlot(playerInventory.player, this, this.resultSlots, 0, 124, 35));

      slotsChanged(craftSlots);

      //crafting grid
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j) {
            this.addSlot(new Slot(craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
         }
      }

      //player inventory
      for (int k = 0; k < 3; ++k) {
         for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 130 + k * 18));
         }
      }

      //hotbar
      for (int l = 0; l < 9; ++l) {
         this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 188));
      }

      //quick access inventory
      for (int i = 0; i < 2; ++i) {
         for (int j = 0; j < 6; ++j) {
            this.addSlot(new Slot(quickAccessSlots, j + i * 6, 35 + j * 18, 75 + i * 18));
         }
      }
   }


   protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, FabricatorCraftingContainer fabricatorCraftingContainer, ResultContainer resultContainer) {
      if (!level.isClientSide) {
         ServerPlayer serverplayer = (ServerPlayer) player;
         ItemStack itemstack = ItemStack.EMPTY;

         Optional<FabricatorRecipe> fabricatorOptional = level.getServer().getRecipeManager().getRecipeFor(ETRecipeTypes.FABRICATING.get(), fabricatorCraftingContainer, level);
         if (fabricatorOptional.isPresent()) {
            FabricatorRecipe fabricatingRecipe = fabricatorOptional.get();
            if (resultContainer.setRecipeUsed(level, serverplayer, fabricatingRecipe)) {
               itemstack = fabricatingRecipe.assemble(fabricatorCraftingContainer);
            }
         }
         else {
            Optional<CraftingRecipe> craftingOptional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, fabricatorCraftingContainer, level);
            if (craftingOptional.isPresent()) {
               CraftingRecipe craftingRecipe = craftingOptional.get();
               if (resultContainer.setRecipeUsed(level, serverplayer, craftingRecipe)) {
                  itemstack = craftingRecipe.assemble(fabricatorCraftingContainer);
               }
            }
         }

         resultContainer.setItem(0, itemstack);
         menu.setRemoteSlot(0, itemstack);
         serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
      }
   }

   public void slotsChanged(Container pInventory) {
      this.access.execute((level, pos) -> slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots));
   }

   public void fillCraftSlotsStackedContents(StackedContents pItemHelper) {
      this.craftSlots.fillStackedContents(pItemHelper);
   }

   public void clearCraftingContent() {
      this.craftSlots.clearContent();
      this.resultSlots.clearContent();
   }

   public boolean recipeMatches(Recipe<? super FabricatorCraftingContainer> pRecipe) {
      return pRecipe.matches(this.craftSlots, this.player.level);
   }

   public boolean stillValid(Player pPlayer) {
      return stillValid(this.access, pPlayer, ETBlocks.FABRICATOR.get());
   }

   public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
      ItemStack itemStack = ItemStack.EMPTY;
      Slot slot = this.slots.get(pIndex);
      if (slot != null && slot.hasItem()) {
         ItemStack itemStack1 = slot.getItem();
         itemStack = itemStack1.copy();
         if (pIndex == 0) {
            this.access.execute((p_39378_, p_39379_) -> itemStack1.getItem().onCraftedBy(itemStack1, p_39378_, pPlayer));
            if (!this.moveItemStackTo(itemStack1, 10, 46, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(itemStack1, itemStack);
         } else if (pIndex >= 10 && pIndex < 46) {
            if (!this.moveItemStackTo(itemStack1, 1, 10, false)) {
               if (pIndex < 37) {
                  if (!this.moveItemStackTo(itemStack1, 37, 46, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (!this.moveItemStackTo(itemStack1, 10, 37, false)) {
                  return ItemStack.EMPTY;
               }
            }
         } else if (!this.moveItemStackTo(itemStack1, 10, 46, false)) {
            return ItemStack.EMPTY;
         }

         if (itemStack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (itemStack1.getCount() == itemStack.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(pPlayer, itemStack1);
         if (pIndex == 0) {
            pPlayer.drop(itemStack1, false);
         }
      }

      return itemStack;
   }

   public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
      return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
   }

   public int getResultSlotIndex() {
      return 0;
   }

   public int getGridWidth() {
      return this.craftSlots.getWidth();
   }

   public int getGridHeight() {
      return this.craftSlots.getHeight();
   }

   public int getSize() {
      return 22;
   }

   public RecipeBookType getRecipeBookType() {
      return RecipeBookType.CRAFTING;
   }

   public boolean shouldMoveToInventory(int pSlotIndex) {
      return pSlotIndex != this.getResultSlotIndex();
   }
}