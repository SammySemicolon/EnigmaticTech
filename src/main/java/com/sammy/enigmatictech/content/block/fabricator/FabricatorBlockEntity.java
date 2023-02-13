package com.sammy.enigmatictech.content.block.fabricator;

import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.menu.BlockEntityFabricatorMenu;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class FabricatorBlockEntity extends LodestoneBlockEntity implements MenuProvider {

    public final ItemStackHandler craftingGridInventory;
    public final ItemStackHandler quickAccessInventory;

    public FabricatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        this.craftingGridInventory = createHandler(9);
        this.quickAccessInventory = createHandler(12);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, this, getBlockPos());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.put("craftingGridInventory", craftingGridInventory.serializeNBT());
        compound.put("quickAccessInventory", quickAccessInventory.serializeNBT());

        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        craftingGridInventory.deserializeNBT(compound.getCompound("craftingGridInventory"));
        quickAccessInventory.deserializeNBT(compound.getCompound("quickAccessInventory"));
        super.load(compound);
    }

    @Override
    public void onBreak(@Nullable Player player) {
        Vec3 pos = BlockHelper.fromBlockPos(getBlockPos()).add(0.5, 0.5, 0.5);
        for (int i = 0; i < craftingGridInventory.getSlots(); i++) {
            ItemStack stack = craftingGridInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.x(), pos.y(), pos.z(), stack));
            }
        }
        for (int i = 0; i < quickAccessInventory.getSlots(); i++) {
            ItemStack stack = quickAccessInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.x(), pos.y(), pos.z(), stack));
            }
        }
    }


    private ItemStackHandler createHandler(int size) {
        return new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockHelper.updateAndNotifyState(level, worldPosition);
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.enigmatic_tech.fabricator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player pPlayer) {
        return new BlockEntityFabricatorMenu(id, inventory, this);
    }
}