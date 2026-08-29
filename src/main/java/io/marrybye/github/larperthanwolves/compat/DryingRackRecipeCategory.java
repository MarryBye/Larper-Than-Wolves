package io.marrybye.github.larperthanwolves.compat;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class DryingRackRecipeCategory implements IRecipeCategory<DryingRackRecipe> {
    public static final RecipeType<DryingRackRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "drying_rack", DryingRackRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public DryingRackRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(140, 48);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.DRYING_RACK.get()));
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<DryingRackRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.larperthanwolves.category.drying_rack");
    }

    @Override
    public int getWidth() {
        return 140;
    }

    @Override
    public int getHeight() {
        return 48;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRackRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16)
                .addItemStacks(recipe.getInputs())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 16)
                .addItemStack(recipe.getOutput())
                .setBackground(this.slotBackground, -1, -1);
    }

    @Override
    public void draw(DryingRackRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component condition = Component.translatable("jei.larperthanwolves.drying_rack.condition");
        guiGraphics.drawString(font, condition, 6, 2, 0x404040, false);
        guiGraphics.drawString(font, "====>", 52, 20, 0x808080, false);
        Component desc = Component.translatable("jei.larperthanwolves.drying_rack.time");
        guiGraphics.drawString(font, desc, 25, 36, 0x808080, false);
    }
}
