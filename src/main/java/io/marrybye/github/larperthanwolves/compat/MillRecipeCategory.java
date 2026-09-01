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

import java.util.List;

public class MillRecipeCategory implements IRecipeCategory<MillJeiRecipe> {
    public static final RecipeType<MillJeiRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "milling", MillJeiRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "textures/gui/container/mill.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public MillRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 36, 14, 132, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.MILL.get()));
        this.arrow = guiHelper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<MillJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.larperthanwolves.mill");
    }

    @Override
    public int getWidth() {
        return 132;
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
    public void setRecipe(IRecipeLayoutBuilder builder, MillJeiRecipe recipe, IFocusGroup focuses) {
        // Input slot at x=8, y=21
        builder.addSlot(RecipeIngredientRole.INPUT, 9, 21).addItemStack(recipe.getInput());

        // Output slots at x=71, 91, 111, y=21
        List<ItemStack> outputs = recipe.getOutputs();
        for (int i = 0; i < 3; i++) {
            var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 71 + i * 20, 21);
            if (i < outputs.size()) {
                slot.addItemStack(outputs.get(i));
            }
        }

        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
                .addItemStacks(List.of(new ItemStack(ModBlocks.MILL.get()), new ItemStack(ModBlocks.MILL_CRANK.get())));
    }

    @Override
    public void draw(MillJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 38, 20);

        Font font = Minecraft.getInstance().font;
        String turnsStr = "20x (0.5s)";
        guiGraphics.drawString(font, turnsStr, 34, 6, 0x808080, false);
    }
}
