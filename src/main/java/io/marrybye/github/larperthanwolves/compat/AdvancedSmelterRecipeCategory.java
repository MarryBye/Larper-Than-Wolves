package io.marrybye.github.larperthanwolves.compat;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AdvancedSmelterRecipeCategory implements IRecipeCategory<AdvancedSmelterJeiRecipe> {
    public static final RecipeType<AdvancedSmelterJeiRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "advanced_smelter_smelting", AdvancedSmelterJeiRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "textures/gui/container/advanced_smelter.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated flame;
    private final IDrawableAnimated arrow;

    public AdvancedSmelterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 48, 14, 94, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.ADVANCED_SMELTER.get()));

        this.flame = guiHelper.drawableBuilder(TEXTURE, 176, 0, 14, 14)
                .buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);

        this.arrow = guiHelper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<AdvancedSmelterJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.larperthanwolves.category.advanced_smelter");
    }

    @Override
    public int getWidth() {
        return 94;
    }

    @Override
    public int getHeight() {
        return 58;
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
    public void setRecipe(IRecipeLayoutBuilder builder, AdvancedSmelterJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 21).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 21).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(AdvancedSmelterJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.flame.draw(guiGraphics, 36, 40);
        this.arrow.draw(guiGraphics, 31, 20);

        Font font = Minecraft.getInstance().font;
        String timeStr = (recipe.getCookTime() / 20) + "s";
        guiGraphics.drawString(font, timeStr, 34, 4, 0x808080, false);
    }
}
