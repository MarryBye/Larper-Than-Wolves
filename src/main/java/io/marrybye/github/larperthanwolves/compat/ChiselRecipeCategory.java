package io.marrybye.github.larperthanwolves.compat;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.item.ModItems;
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

public class ChiselRecipeCategory implements IRecipeCategory<ChiselRecipe> {
    public static final RecipeType<ChiselRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "chisel_crafting", ChiselRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public ChiselRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 48);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.CHISEL.get()));
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<ChiselRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.larperthanwolves.category.chisel");
    }

    @Override
    public int getWidth() {
        return 150;
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
    public void setRecipe(IRecipeLayoutBuilder builder, ChiselRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 16)
                .addItemStacks(recipe.getInputs())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.CATALYST, 44, 16)
                .addItemStack(recipe.getTool())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 82, 16)
                .addItemStack(recipe.getIntermediary())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 16)
                .addItemStack(recipe.getOutput())
                .setBackground(this.slotBackground, -1, -1);
    }

    @Override
    public void draw(ChiselRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component text = Component.translatable("jei.larperthanwolves.chisel.steps", recipe.getClicks());
        guiGraphics.drawString(font, text, 6, 2, 0x404040, false);
        guiGraphics.drawString(font, "+", 30, 20, 0x808080, false);
        guiGraphics.drawString(font, "->", 68, 20, 0x808080, false);
        guiGraphics.drawString(font, "->", 106, 20, 0x808080, false);
    }
}
