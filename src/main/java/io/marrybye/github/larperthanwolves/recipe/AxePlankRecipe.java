package io.marrybye.github.larperthanwolves.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AxePlankRecipe extends CustomRecipe {
    public AxePlankRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 2) return false;
        ItemStack axe = ItemStack.EMPTY;
        ItemStack wood = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (WoodToPlanksHelper.isAxe(stack) && axe.isEmpty()) {
                    axe = stack;
                } else if (WoodToPlanksHelper.getPlanksForWood(stack) != null && wood.isEmpty()) {
                    wood = stack;
                } else {
                    return false;
                }
            }
        }
        return !axe.isEmpty() && !wood.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                ItemStack planks = WoodToPlanksHelper.getPlanksForWood(stack);
                if (planks != null) {
                    return planks.copyWithCount(2);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && WoodToPlanksHelper.isAxe(stack)) {
                ItemStack damagedAxe = stack.copy();
                damagedAxe.setDamageValue(damagedAxe.getDamageValue() + 1);
                if (damagedAxe.getDamageValue() < damagedAxe.getMaxDamage()) {
                    remaining.set(i, damagedAxe);
                }
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.AXE_PLANKS.get();
    }
}
