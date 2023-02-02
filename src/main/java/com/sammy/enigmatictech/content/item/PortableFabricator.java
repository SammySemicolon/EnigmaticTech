package com.sammy.enigmatictech.content.item;

import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.menu.PortableFabricatorMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class PortableFabricator extends Item {
    public PortableFabricator(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        if (!level.isClientSide) {
            ItemStack stack = playerIn.getItemInHand(handIn);
            MenuProvider container =
                    new SimpleMenuProvider((w, p, pl) -> new PortableFabricatorMenu(w, p, stack), stack.getHoverName());
            NetworkHooks.openGui((ServerPlayer) playerIn, container, b -> b.writeItem(stack));
            playerIn.level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1, 1);
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }
}
