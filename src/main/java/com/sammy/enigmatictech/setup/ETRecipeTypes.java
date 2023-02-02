 package com.sammy.enigmatictech.setup;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.recipe.FabricatorRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

 public class ETRecipeTypes {

     public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE.key(), EnigmaticTechMod.MODID);

     public static final String FABRICATING_TYPE_NAME = "fabricating";
     public static final RegistryObject<RecipeType<FabricatorRecipe>> FABRICATING = RECIPE_TYPES.register(FABRICATING_TYPE_NAME, () -> registerRecipeType(FABRICATING_TYPE_NAME));

     public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
         return new RecipeType<>() {
             public String toString() {
                 return EnigmaticTechMod.MODID + ":" + identifier;
             }
         };
     }
 }