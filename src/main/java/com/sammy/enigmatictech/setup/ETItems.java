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

    public static final ItemEntry<PortableFabricator> MILK_CUP = setupItem("milk_cup", PortableFabricator::new).properties(p -> p.stacksTo(1)).register();

    public static <T extends Item> ItemBuilder<T, Registrate> setupItem(String name, FoodProperties foodProperties, NonNullFunction<Item.Properties, T> factory) {
        return setupItem(name, factory).properties(p -> p.food(foodProperties));
    }
    public static ItemBuilder<Item, Registrate> setupItem(String name, FoodProperties foodProperties) {
        return setupItem(name, Item::new).properties(p -> p.food(foodProperties));
    }

    public static <T extends Item> ItemBuilder<T, Registrate> setupItem(String name, NonNullFunction<Item.Properties, T> factory) {
        return ITEM_REGISTRATE.item(name, factory);
    }

    public static void register() {
    }
}