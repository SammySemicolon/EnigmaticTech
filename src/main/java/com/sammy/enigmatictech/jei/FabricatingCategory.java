package com.sammy.enigmatictech.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.recipe.FabricatorRecipe;
import com.sammy.enigmatictech.content.block.fabricator.recipe.FabricatorShapedRecipe;
import com.sammy.enigmatictech.setup.ETBlocks;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.config.Constants;
import mezz.jei.gui.recipes.builder.RecipeLayoutBuilder;
import mezz.jei.ingredients.Ingredients;
import mezz.jei.recipes.ExtendableRecipeCategoryHelper;
import mezz.jei.util.ErrorUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FabricatingCategory implements IRecipeCategory<FabricatorRecipe> {

    //this sucks, but oh well

    public static final RecipeType<FabricatorRecipe> FABRICATING =
            RecipeType.create(EnigmaticTechMod.MODID, "fabricating", FabricatorRecipe.class);


    private static final int craftInputSlot1 = 1;

    public static final int width = 116;
    public static final int height = 54;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final ICraftingGridHelper craftingGridHelper;

    public FabricatingCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
        background = guiHelper.createDrawable(location, 0, 60, width, height);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ETBlocks.FABRICATOR.get()));
        localizedName = new TranslatableComponent("gui.enigmatic_tech.category.fabricator");
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1);
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends FabricatorRecipe> getRecipeClass() {
        return getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<FabricatorRecipe> getRecipeType() {
        return FABRICATING;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FabricatorRecipe recipe, IFocusGroup focuses) {
        // temporary hack to detect if the plugin needs legacy support
        if (builder instanceof RecipeLayoutBuilder b && b.isUsed()) {
            return;
        }
        int width = 0;
        int height = 0;
        if (recipe instanceof FabricatorShapedRecipe shapedRecipe) {
            width = shapedRecipe.getRecipeWidth();
            height = shapedRecipe.getRecipeHeight();
        }

        List<List<ItemStack>> inputs = recipe.getIngredients().stream().map(i -> List.of(i.getItems())).collect(Collectors.toList());
        List<ItemStack> output = List.of(recipe.getResultItem());

        craftingGridHelper.setOutputs(builder, VanillaTypes.ITEM_STACK, output);
        craftingGridHelper.setInputs(builder, VanillaTypes.ITEM_STACK, inputs, width, height);
    }
}
