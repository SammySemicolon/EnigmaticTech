package com.sammy.enigmatictech.content.item;

import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.menu.PortableFabricatorMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemInventory extends ItemStackHandler {
    private final Player player;
    private final String name;
    private final ItemStack stack;

    public ItemInventory(int size, Player player, String name, ItemStack stack) {
        super(size);
        this.player = player;
        this.name = name;
        this.stack = stack;
        deserializeNBT(stack.getOrCreateTag().getCompound(name));
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = super.serializeNBT();
        stack.getOrCreateTag().put(name, tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    protected void onContentsChanged(int slot) {
        serializeNBT();
        if (player.containerMenu instanceof PortableFabricatorMenu portableFabricatorMenu) {
            portableFabricatorMenu.slotsChanged(null);
        }

    }
}