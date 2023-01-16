package com.sammy.enigmatictech.content.block.fabricator;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.IntStream;

public class FabricatorQuickAccessContainer implements Container, StackedContentsCompatible {
   public final ItemStackHandler inventory;
   public final AbstractContainerMenu menu;

   public FabricatorQuickAccessContainer(AbstractContainerMenu pMenu, ItemStackHandler inventory) {
      this.inventory = inventory;
      this.menu = pMenu;
   }

   @Override
   public int getContainerSize() {
      return 12;
   }

   @Override
   public ItemStack getItem(int slot) {
      return inventory.getStackInSlot(slot);
   }

   @Override
   public ItemStack removeItem(int slot, int count) {
      ItemStack stack = inventory.extractItem(slot, count, false);
      if (!stack.isEmpty()) {
         updateMenu();
      }
      return stack;
   }

   @Override
   public void setItem(int slot, ItemStack stack) {
      inventory.setStackInSlot(slot, stack);
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
      return IntStream.range(0, inventory.getSlots()).allMatch(i -> inventory.getStackInSlot(i).isEmpty());
   }

   @Override
   public void fillStackedContents(StackedContents pHelper) {

   }

   @Override
   public void setChanged() {

   }

   @Override
   public boolean stillValid(Player pPlayer) {
      return true;
   }

   @Override
   public void clearContent() {
   }

   public void updateMenu() {
      this.menu.slotsChanged(this);
   }
}