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

public class SunDryingRecipeCategory implements IRecipeCategory<SunDryingRecipe> {
    public static final RecipeType<SunDryingRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "sun_drying", SunDryingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public SunDryingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(140, 48);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.UNFIRED_BRICK.asItem()));
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<SunDryingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.larperthanwolves.category.sun_drying");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SunDryingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16)
                .addItemStack(recipe.getInput())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 16)
                .addItemStack(recipe.getOutput())
                .setBackground(this.slotBackground, -1, -1);
    }

    @Override
    public void draw(SunDryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component condition = Component.translatable("jei.larperthanwolves.sun_drying.condition");
        guiGraphics.drawString(font, condition, 16, 2, 0x404040, false);
        guiGraphics.drawString(font, "====>", 52, 20, 0x808080, false);
        Component desc = Component.translatable("jei.larperthanwolves.sun_drying.time");
        guiGraphics.drawString(font, desc, 30, 36, 0x808080, false);
    }
}
