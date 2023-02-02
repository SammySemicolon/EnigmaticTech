package com.sammy.enigmatictech.content.block.fabricator.recipe;

import com.sammy.enigmatictech.setup.ETRecipeTypes;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface FabricatorRecipe extends CraftingRecipe {
    @Override
    default RecipeType<?> getType() {
        return ETRecipeTypes.FABRICATING.get();
    }
}
