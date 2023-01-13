package com.sammy.enigmatictech.content.block.fabricator;

import java.util.Objects;
import java.util.Optional;

import com.sammy.enigmatictech.setup.ETBlocks;
import com.sammy.enigmatictech.setup.ETMenuTypes;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FabricatorCraftingMenu extends RecipeBookMenu<FabricatorCraftingContainer> {
   public static final int RESULT_SLOT = 0;
   private static final int CRAFT_SLOT_START = 1;
   private static final int CRAFT_SLOT_END = 10;
   private static final int INV_SLOT_START = 10;
   private static final int INV_SLOT_END = 37;
   private static final int USE_ROW_SLOT_START = 37;
   private static final int USE_ROW_SLOT_END = 46;

   private final ResultContainer resultSlots = new ResultContainer();

   private final Player player;

   private final FabricatorBlockEntity fabricatorBlockEntity;
   private final ContainerLevelAccess access;
   private final ItemStackHandler craftingGridInventory;
   private final ItemStackHandler quickAccessInventory;
   private final FabricatorCraftingContainer craftSlots;

   public FabricatorCraftingMenu(final int containerId, final Inventory playerInventory, FabricatorBlockEntity fabricatorBlockEntity) {
      super(ETMenuTypes.FABRICATOR.get(), containerId);
      this.player = playerInventory.player;
      this.fabricatorBlockEntity = fabricatorBlockEntity;
      this.access = ContainerLevelAccess.create(fabricatorBlockEntity.getLevel(), fabricatorBlockEntity.getBlockPos());
      this.craftingGridInventory = fabricatorBlockEntity.craftingGridInventory;
      this.quickAccessInventory = fabricatorBlockEntity.quickAccessInventory;
      this.craftSlots = new FabricatorCraftingContainer(this, craftingGridInventory);
      this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));
      slotsChanged(craftSlots);
      
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j) {
            this.addSlot(new Slot(craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
         }
      }

      //player inventory
      for (int k = 0; k < 3; ++k) {
         for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
         }
      }

      //hotbar
      for (int l = 0; l < 9; ++l) {
         this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
      }



   }
   public FabricatorCraftingMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
      this(windowId, playerInventory, getTileEntity(playerInventory, data));
   }

   private static FabricatorBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
      Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
      Objects.requireNonNull(data, "data cannot be null");
      final BlockEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());
      if (tileAtPos instanceof FabricatorBlockEntity fabricatorBlockEntity) {
         return fabricatorBlockEntity;
      }
      throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
   }


   protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, FabricatorCraftingContainer fabricatorCraftingContainer, ResultContainer resultContainer) {
      if (!level.isClientSide) {
         ServerPlayer serverplayer = (ServerPlayer) player;
         ItemStack itemstack = ItemStack.EMPTY;
         Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, fabricatorCraftingContainer, level);
         if (optional.isPresent()) {
            CraftingRecipe craftingrecipe = optional.get();
            if (resultContainer.setRecipeUsed(level, serverplayer, craftingrecipe)) {
               itemstack = craftingrecipe.assemble(fabricatorCraftingContainer);
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
            this.access.execute((p_39378_, p_39379_) -> {
               itemStack1.getItem().onCraftedBy(itemStack1, p_39378_, pPlayer);
            });
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
      return 10;
   }

   public RecipeBookType getRecipeBookType() {
      return RecipeBookType.CRAFTING;
   }

   public boolean shouldMoveToInventory(int pSlotIndex) {
      return pSlotIndex != this.getResultSlotIndex();
   }
}