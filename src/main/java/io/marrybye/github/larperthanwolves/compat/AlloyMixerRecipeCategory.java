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

public class AlloyMixerRecipeCategory implements IRecipeCategory<AlloyMixerRecipe> {
    public static final RecipeType<AlloyMixerRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "alloy_mixing", AlloyMixerRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "textures/gui/container/alloy_mixer.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated flame;
    private final IDrawableAnimated arrow;

    public AlloyMixerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 36, 14, 118, 58);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.ALLOY_MIXER.get()));

        this.flame = guiHelper.drawableBuilder(TEXTURE, 176, 0, 14, 14)
                .buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);

        this.arrow = guiHelper.drawableBuilder(TEXTURE, 176, 14, 24, 17)
                .buildAnimated(600, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<AlloyMixerRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.larperthanwolves.alloy_mixer");
    }

    @Override
    public int getWidth() {
        return 118;
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
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyMixerRecipe recipe, IFocusGroup focuses) {
        int[] yPositions = new int[]{3, 21, 39};
        for (int i = 0; i < 3; i++) {
            var slot = builder.addSlot(RecipeIngredientRole.INPUT, 8, yPositions[i]);
            if (i < recipe.getInputs().size()) {
                slot.addItemStack(recipe.getInputs().get(i));
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 21).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(AlloyMixerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.flame.draw(guiGraphics, 48, 40);
        this.arrow.draw(guiGraphics, 43, 20);

        Font font = Minecraft.getInstance().font;
        String timeStr = (recipe.getCookTime() / 20) + "s";
        guiGraphics.drawString(font, timeStr, 44, 2, 0x808080, false);
    }
}
