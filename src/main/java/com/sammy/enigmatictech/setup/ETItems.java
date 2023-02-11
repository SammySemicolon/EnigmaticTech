package com.sammy.enigmatictech.setup;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.item.PortableFabricator;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static com.sammy.enigmatictech.EnigmaticTechMod.MODID;

public class ETItems {
    public static final Registrate ITEM_REGISTRATE = EnigmaticTechMod.registrate();

    public static final ItemEntry<PortableFabricator> PORTABLE_FABRICATOR = setupItem("portable_fabricator", PortableFabricator::new).properties(p -> p.stacksTo(1)).register();

    public static <T extends Item> ItemBuilder<T, Registrate> setupItem(String name, NonNullFunction<Item.Properties, T> factory) {
        return ITEM_REGISTRATE.item(name, factory);
    }

    public static void register() {
    }
}