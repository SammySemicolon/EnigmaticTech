package com.sammy.enigmatictech.jei;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorScreen;
import com.sammy.enigmatictech.setup.ETBlocks;
import com.sammy.enigmatictech.setup.ETRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;


@JeiPlugin
public class JEIHandler implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(EnigmaticTechMod.MODID, "main");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new FabricatingCategory(guiHelper));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            registry.addRecipes(FabricatingCategory.FABRICATING, level.getRecipeManager().getAllRecipesFor(ETRecipeTypes.FABRICATING.get()));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ETBlocks.FABRICATOR.get()), FabricatingCategory.FABRICATING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FabricatorScreen.class, 88, 32, 28, 23, FabricatingCategory.FABRICATING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AbstractFabricatorMenu.class, FabricatingCategory.FABRICATING, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(AbstractFabricatorMenu.class, RecipeTypes.CRAFTING, 1, 9, 10, 36);


        registration.addRecipeTransferHandler(CraftingMenu.class, FabricatingCategory.FABRICATING, 1, 9, 10, 36);
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
