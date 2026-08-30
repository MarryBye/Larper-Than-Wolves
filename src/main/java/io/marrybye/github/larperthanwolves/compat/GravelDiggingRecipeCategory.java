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

public class GravelDiggingRecipeCategory implements IRecipeCategory<GravelDiggingRecipe> {
    public static final RecipeType<GravelDiggingRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "gravel_digging", GravelDiggingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public GravelDiggingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(160, 52);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.SILICON_SHARD.get()));
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<GravelDiggingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.larperthanwolves.category.gravel_digging");
    }

    @Override
    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 52;
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
    public void setRecipe(IRecipeLayoutBuilder builder, GravelDiggingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 16)
                .addItemStack(recipe.getBlock())
                .setBackground(this.slotBackground, -1, -1);

        int[] xPositions = new int[]{50, 76, 102, 128};
        for (int i = 0; i < recipe.getDrops().size() && i < 4; i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, xPositions[i], 16)
                    .addItemStack(recipe.getDrops().get(i).stack())
                    .setBackground(this.slotBackground, -1, -1);
        }
    }

    @Override
    public void draw(GravelDiggingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component title = Component.translatable("jei.larperthanwolves.gravel_digging.title");
        guiGraphics.drawString(font, title, 6, 2, 0x404040, false);
        guiGraphics.drawString(font, "->", 32, 20, 0x808080, false);

        int[] xPositions = new int[]{50, 76, 102, 128};
        for (int i = 0; i < recipe.getDrops().size() && i < 4; i++) {
            String chance = recipe.getDrops().get(i).chanceText();
            guiGraphics.drawString(font, chance, xPositions[i] + 1, 36, 0x808080, false);
        }
    }
}
