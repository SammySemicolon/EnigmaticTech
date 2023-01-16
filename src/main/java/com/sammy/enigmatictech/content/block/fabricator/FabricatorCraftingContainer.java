package com.sammy.enigmatictech.content.block.fabricator;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.IntStream;

public class FabricatorCraftingContainer extends CraftingContainer {
   public final ItemStackHandler craftingGridInventory;
   public boolean isCrafting;

   public FabricatorCraftingContainer(AbstractContainerMenu pMenu, ItemStackHandler craftingGridInventory) {
      super(pMenu, 3, 3);
      this.craftingGridInventory = craftingGridInventory;
   }

   @Override
   public ItemStack getItem(int slot) {
      return craftingGridInventory.getStackInSlot(slot);
   }

   @Override
   public ItemStack removeItem(int slot, int count) {
      ItemStack stack = craftingGridInventory.extractItem(slot, count, false);
      if (!stack.isEmpty()) {
         updateMenu();
      }
      return stack;
   }

   @Override
   public void setItem(int slot, ItemStack stack) {
      craftingGridInventory.setStackInSlot(slot, stack);
      updateMenu();
   }

   @Override
   public ItemStack removeItemNoUpdate(int index) {
      ItemStack s = getItem(index);
      if (s.isEmpty()) {
         return ItemStack.EMPTY;
      }
      updateMenu();
      setItem(index, ItemStack.EMPTY);
      return s;
   }

   @Override
   public boolean isEmpty() {
      return IntStream.range(0, craftingGridInventory.getSlots()).allMatch(i -> craftingGridInventory.getStackInSlot(i).isEmpty());
   }

   @Override
   public void clearContent() {
   }

   public void setCrafting(boolean isCrafting) {
      this.isCrafting = isCrafting;
   }

   public void updateMenu() {
      if (!isCrafting) {
         this.menu.slotsChanged(this);
      }
   }
}