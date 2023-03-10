package com.sammy.enigmatictech.content.block.fabricator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.menu.AbstractFabricatorMenu;
import com.sammy.enigmatictech.content.block.fabricator.menu.PortableFabricatorMenu;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FabricatorScreen extends AbstractContainerScreen<AbstractFabricatorMenu> implements RecipeUpdateListener {
   private static final ResourceLocation CRAFTING_TABLE_LOCATION = EnigmaticTechMod.path("textures/gui/fabricator.png");
   private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
   private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();
   private boolean widthTooNarrow;

   public FabricatorScreen(AbstractFabricatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
      super(pMenu, pPlayerInventory, new TranslatableComponent("block.enigmatic_tech.fabricator"));
      this.imageHeight = 212;
      this.inventoryLabelY = this.imageHeight - 94;
   }

   protected void init() {
      super.init();
      this.widthTooNarrow = this.width < 379;
      this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
      this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
      this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 69, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_98484_) -> {
         this.recipeBookComponent.toggleVisibility();
         this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
         ((ImageButton)p_98484_).setPosition(this.leftPos + 5, this.height / 2 - 69);
      }));
      this.addWidget(this.recipeBookComponent);
      this.setInitialFocus(this.recipeBookComponent);
      this.titleLabelY = 6;
      this.titleLabelX = 30;
   }

   public void containerTick() {
      super.containerTick();
      this.recipeBookComponent.tick();
   }

   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
      this.renderBackground(pPoseStack);
      if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
         this.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
         this.recipeBookComponent.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
      } else {
         this.recipeBookComponent.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
         super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
         this.recipeBookComponent.renderGhostRecipe(pPoseStack, this.leftPos, this.topPos, true, pPartialTick);
      }

      this.renderTooltip(pPoseStack, pMouseX, pMouseY);
      this.recipeBookComponent.renderTooltip(pPoseStack, this.leftPos, this.topPos, pMouseX, pMouseY);
   }

   protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, CRAFTING_TABLE_LOCATION);
      int i = this.leftPos;
      int j = (this.height - this.imageHeight) / 2;
      this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
   }

   @Override
   protected void renderLabels(PoseStack pPoseStack, int pX, int pY) {
      this.font.draw(pPoseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 5263460);
      this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
   }

   protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
      return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(pX, pY, pWidth, pHeight, pMouseX, pMouseY);
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (this.recipeBookComponent.mouseClicked(pMouseX, pMouseY, pButton)) {
         this.setFocused(this.recipeBookComponent);
         return true;
      } else {
         return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(pMouseX, pMouseY, pButton);
      }
   }

   protected boolean hasClickedOutside(double pMouseX, double pMouseY, int pGuiLeft, int pGuiTop, int pMouseButton) {
      boolean flag = pMouseX < (double)pGuiLeft || pMouseY < (double)pGuiTop || pMouseX >= (double)(pGuiLeft + this.imageWidth) || pMouseY >= (double)(pGuiTop + this.imageHeight);
      return this.recipeBookComponent.hasClickedOutside(pMouseX, pMouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, pMouseButton) && flag;
   }

   protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
      super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
      this.recipeBookComponent.slotClicked(pSlot);
   }

   public void recipesUpdated() {
      this.recipeBookComponent.recipesUpdated();
   }

   public void removed() {
      this.recipeBookComponent.removed();
      super.removed();
   }

   public RecipeBookComponent getRecipeBookComponent() {
      return this.recipeBookComponent;
   }
}