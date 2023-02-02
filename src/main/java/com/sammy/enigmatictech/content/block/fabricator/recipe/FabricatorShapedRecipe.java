package com.sammy.enigmatictech.content.block.fabricator.recipe;

import com.google.gson.JsonObject;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorCraftingContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.Map;

public class FabricatorShapedRecipe extends ShapedRecipe implements FabricatorRecipe {

    public static final String NAME = "fabricating_shaped";

    public FabricatorShapedRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, pWidth, pHeight, pRecipeItems, pResult);
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        if (!(pInv instanceof FabricatorCraftingContainer)) {
            return false;
        }
        return super.matches(pInv, pLevel);
    }

    public static class ShapedFabricatingSerializer extends ShapedRecipe.Serializer {
        @Override
        public FabricatorShapedRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            ShapedRecipe old = super.fromJson(pRecipeId, pJson);
            return new FabricatorShapedRecipe(old.getId(), old.getGroup(), old.getWidth(), old.getHeight(), old.getIngredients(), old.getResultItem());
        }

        @Override
        public FabricatorShapedRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ShapedRecipe old = super.fromNetwork(pRecipeId, pBuffer);
            return new FabricatorShapedRecipe(old.getId(), old.getGroup(), old.getWidth(), old.getHeight(), old.getIngredients(), old.getResultItem());
        }
    }
}
