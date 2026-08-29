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

public class MachineFuelRecipeCategory implements IRecipeCategory<MachineFuelRecipe> {
    public static final RecipeType<MachineFuelRecipe> TYPE = RecipeType.create(LarperThanWolves.MODID, "machine_fuel", MachineFuelRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public MachineFuelRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 48);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.LIGHTER.get()));
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<MachineFuelRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.larperthanwolves.category.machine_fuel");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MachineFuelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 16)
                .addItemStacks(recipe.getFuels())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.CATALYST, 54, 16)
                .addItemStacks(recipe.getIgnitionTools())
                .setBackground(this.slotBackground, -1, -1);

        builder.addSlot(RecipeIngredientRole.CATALYST, 114, 16)
                .addItemStacks(recipe.getMachines())
                .setBackground(this.slotBackground, -1, -1);
    }

    @Override
    public void draw(MachineFuelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        String burnSec = (recipe.getBurnDuration() / 20) + "s";
        Component header = Component.translatable("jei.larperthanwolves.machine_fuel.info", burnSec, recipe.getCookSpeed());
        guiGraphics.drawString(font, header, 8, 2, 0x404040, false);
        guiGraphics.drawString(font, "+", 36, 20, 0x808080, false);
        guiGraphics.drawString(font, "->", 86, 20, 0x808080, false);
        Component igniteNote = Component.translatable("jei.larperthanwolves.machine_fuel.ignite");
        guiGraphics.drawString(font, igniteNote, 8, 36, 0x808080, false);
    }
}
