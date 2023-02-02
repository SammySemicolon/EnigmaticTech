package com.sammy.enigmatictech.content.block.fabricator.menu;

import com.sammy.enigmatictech.content.block.fabricator.FabricatorBlockEntity;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorCraftingContainer;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorQuickAccessContainer;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorResultSlot;
import com.sammy.enigmatictech.content.item.ItemInventory;
import com.sammy.enigmatictech.setup.ETMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class PortableFabricatorMenu extends AbstractFabricatorMenu {

    public PortableFabricatorMenu(int containerId, Inventory playerInventory, ItemStack stack) {
        super(ETMenuTypes.FABRICATOR_PORTABLE.get(), containerId, playerInventory, ContainerLevelAccess.NULL,
                new ItemInventory(9, playerInventory.player, "crafting_inventory", stack),
                new ItemInventory(12, playerInventory.player, "quick_access_inventory", stack));
    }

    public PortableFabricatorMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, playerInventory, data.readItem());
    }

    @Override
    public void slotsChanged(Container pInventory) {
        slotChangedCraftingGrid(this, player.level, player, craftSlots, resultSlots);
    }
}