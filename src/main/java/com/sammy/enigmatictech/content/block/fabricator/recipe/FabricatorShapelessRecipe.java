package com.sammy.enigmatictech.content.block.fabricator.recipe;

import com.google.gson.JsonObject;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorCraftingContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class FabricatorShapelessRecipe extends ShapelessRecipe implements FabricatorRecipe {
    public static final String NAME = "fabricating_shapeless";

    public FabricatorShapelessRecipe(ResourceLocation pId, String pGroup, ItemStack pResult, NonNullList<Ingredient> pIngredients) {
        super(pId, pGroup, pResult, pIngredients);
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        if (!(pInv instanceof FabricatorCraftingContainer)) {
            return false;
        }
        return super.matches(pInv, pLevel);
    }

    public static class ShapelessFabricatingSerializer extends ShapelessRecipe.Serializer {
        @Override
        public FabricatorShapelessRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            ShapelessRecipe old = super.fromJson(pRecipeId, pJson);
            return new FabricatorShapelessRecipe(old.getId(), old.getGroup(), old.getResultItem(), old.getIngredients());
        }

        @Override
        public FabricatorShapelessRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ShapelessRecipe old = super.fromNetwork(pRecipeId, pBuffer);
            return new FabricatorShapelessRecipe(old.getId(), old.getGroup(), old.getResultItem(), old.getIngredients());
        }
    }
}