package com.sammy.enigmatictech.setup;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorScreen;
import com.sammy.enigmatictech.content.block.fabricator.menu.BlockEntityFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.menu.PortableFabricatorMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ETMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, EnigmaticTechMod.MODID);

    public static final RegistryObject<MenuType<BlockEntityFabricatorMenu>> FABRICATOR = MENU_TYPES
            .register("fabricator", () -> IForgeMenuType.create(BlockEntityFabricatorMenu::new));
    public static final RegistryObject<MenuType<PortableFabricatorMenu>> FABRICATOR_PORTABLE = MENU_TYPES
            .register("fabricator_portable", () -> IForgeMenuType.create(PortableFabricatorMenu::new));

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnigmaticTechMod.MODID)
    public static class ClientOnly {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> MenuScreens.register(FABRICATOR.get(), FabricatorScreen::new));
            event.enqueueWork(() -> MenuScreens.register(FABRICATOR_PORTABLE.get(), FabricatorScreen::new));
        }
    }
}