package com.sammy.enigmatictech.content.block.fabricator.menu;

import com.sammy.enigmatictech.content.block.fabricator.FabricatorBlockEntity;
import com.sammy.enigmatictech.setup.ETMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Objects;

public class BlockEntityFabricatorMenu extends AbstractFabricatorMenu {
    public BlockEntityFabricatorMenu(int containerId, Inventory playerInventory, FabricatorBlockEntity tileEntity) {
        super(ETMenuTypes.FABRICATOR.get(), containerId, playerInventory, ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), tileEntity.craftingGridInventory, tileEntity.quickAccessInventory);
    }

    public BlockEntityFabricatorMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
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
}