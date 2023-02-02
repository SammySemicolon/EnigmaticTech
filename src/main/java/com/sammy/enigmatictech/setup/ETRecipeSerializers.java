package com.sammy.enigmatictech.setup;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.recipe.FabricatorShapedRecipe;
import com.sammy.enigmatictech.content.block.fabricator.recipe.FabricatorShapelessRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ETRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EnigmaticTechMod.MODID);

    public static final RegistryObject<FabricatorShapedRecipe.ShapedFabricatingSerializer> SHAPED_FABRICATING_SERIALIZER = RECIPE_SERIALIZERS.register(FabricatorShapedRecipe.NAME, FabricatorShapedRecipe.ShapedFabricatingSerializer::new);
    public static final RegistryObject<FabricatorShapelessRecipe.ShapelessFabricatingSerializer> SHAPELESS_FABRICATING_SERIALIZER = RECIPE_SERIALIZERS.register(FabricatorShapelessRecipe.NAME, FabricatorShapelessRecipe.ShapelessFabricatingSerializer::new);

    public static <T extends Recipe<?>> RecipeType<T> registerType(final String pIdentifier, RecipeType<T> recipeType) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(pIdentifier), recipeType);
    }
}
