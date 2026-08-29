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

public class SieveRecipeCategory implements IRecipeCategory<SieveJeiRecipe> {
    public static final RecipeType<SieveJeiRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "sieving", SieveJeiRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "textures/gui/container/sieve.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public SieveRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 10, 14, 156, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.SIEVE.get()));

        this.arrow = guiHelper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<SieveJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.larperthanwolves.sieve");
    }

    @Override
    public int getWidth() {
        return 156;
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
    public void setRecipe(IRecipeLayoutBuilder builder, SieveJeiRecipe recipe, IFocusGroup focuses) {
        // Input slot (Gravel) at relative x = 18 - 10 = 8 + 18 = 26, y = 35 - 14 = 21
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 21).addItemStack(recipe.getInput());

        // Possible outputs on the right 3x3 area
        var outputs = recipe.getPossibleOutputs();
        for (int i = 0; i < outputs.size() && i < 9; i++) {
            int col = i % 3;
            int row = i / 3;
            builder.addSlot(RecipeIngredientRole.OUTPUT, 96 + col * 18, 3 + row * 18).addItemStack(outputs.get(i));
        }
    }

    @Override
    public void draw(SieveJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // Arrow at relative x = 76 - 10 = 66, y = 34 - 14 = 20
        this.arrow.draw(guiGraphics, 66, 20);

        Font font = Minecraft.getInstance().font;
        String timeStr = (recipe.getProcessTime() / 20) + "s";
        guiGraphics.drawString(font, timeStr, 67, 8, 0x808080, false);
    }
}
