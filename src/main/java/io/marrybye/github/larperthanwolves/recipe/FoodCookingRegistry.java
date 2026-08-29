package io.marrybye.github.larperthanwolves.recipe;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class FoodCookingRegistry {
    private static final Map<Item, Supplier<ItemStack>> CUSTOM_COOKING = new HashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        CUSTOM_COOKING.clear();

        register(Items.BEEF, () -> new ItemStack(Items.COOKED_BEEF));
        register(Items.PORKCHOP, () -> new ItemStack(Items.COOKED_PORKCHOP));
        register(Items.MUTTON, () -> new ItemStack(Items.COOKED_MUTTON));
        register(Items.CHICKEN, () -> new ItemStack(Items.COOKED_CHICKEN));
        register(Items.RABBIT, () -> new ItemStack(Items.COOKED_RABBIT));
        register(Items.COD, () -> new ItemStack(Items.COOKED_COD));
        register(Items.SALMON, () -> new ItemStack(Items.COOKED_SALMON));
        register(Items.POTATO, () -> new ItemStack(Items.BAKED_POTATO));
        register(Items.KELP, () -> new ItemStack(Items.DRIED_KELP));
    }

    public static void register(Item input, Supplier<ItemStack> outputSupplier) {
        CUSTOM_COOKING.put(input, outputSupplier);
    }

    public static boolean isFood(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.has(DataComponents.FOOD)) return true;
        return CUSTOM_COOKING.containsKey(stack.getItem());
    }

    public static ItemStack getCookingResult(Level level, ItemStack input) {
        if (input.isEmpty()) return ItemStack.EMPTY;

        Supplier<ItemStack> custom = CUSTOM_COOKING.get(input.getItem());
        if (custom != null) {
            return custom.get();
        }

        if (level != null) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(input);

            // 1. Check Smoking recipes first
            Optional<RecipeHolder<SmokingRecipe>> smokingMatch = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMOKING, recipeInput, level);
            if (smokingMatch.isPresent()) {
                return smokingMatch.get().value().assemble(recipeInput, level.registryAccess());
            }

            // 2. Check Campfire cooking recipes
            Optional<RecipeHolder<CampfireCookingRecipe>> campfireMatch = level.getRecipeManager()
                    .getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeInput, level);
            if (campfireMatch.isPresent()) {
                ItemStack res = campfireMatch.get().value().assemble(recipeInput, level.registryAccess());
                if (res.has(DataComponents.FOOD)) {
                    return res;
                }
            }

            // 3. Check Smelting recipes if output is food
            Optional<RecipeHolder<SmeltingRecipe>> smeltingMatch = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeInput, level);
            if (smeltingMatch.isPresent()) {
                ItemStack res = smeltingMatch.get().value().assemble(recipeInput, level.registryAccess());
                if (res.has(DataComponents.FOOD)) {
                    return res;
                }
            }
        }

        return ItemStack.EMPTY;
    }
}
