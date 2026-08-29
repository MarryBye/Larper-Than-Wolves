package io.marrybye.github.larperthanwolves.recipe;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SmeltingRegistry {
    private static final Map<Item, Supplier<ItemStack>> CUSTOM_SMELTING = new HashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        CUSTOM_SMELTING.clear();

        // 1. Raw ores -> 1 nugget (iron, copper, gold, tin)
        register(Items.RAW_IRON, () -> new ItemStack(Items.IRON_NUGGET, 1));
        register(Items.RAW_COPPER, () -> new ItemStack(ModItems.COPPER_NUGGET.get(), 1));
        register(Items.RAW_GOLD, () -> new ItemStack(Items.GOLD_NUGGET, 1));
        register(ModItems.RAW_TIN.get(), () -> new ItemStack(ModItems.TIN_NUGGET.get(), 1));

        // 2. Basic blocks & materials (Strictly NO FOOD)
        register(Items.COBBLESTONE, () -> new ItemStack(Items.STONE));
        register(Items.SAND, () -> new ItemStack(Items.GLASS));
        register(ModBlocks.UNFIRED_BRICK.asItem(), () -> new ItemStack(Items.BRICK, 1));
        register(Items.CLAY, () -> new ItemStack(Items.TERRACOTTA));
        register(Items.WET_SPONGE, () -> new ItemStack(Items.SPONGE));
    }

    public static void register(Item input, Supplier<ItemStack> outputSupplier) {
        CUSTOM_SMELTING.put(input, outputSupplier);
    }

    public static boolean isRawZinc(ItemStack input) {
        if (input.isEmpty()) return false;
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(input.getItem());
        if (key != null) {
            if ("create".equals(key.getNamespace()) && key.getPath().equals("raw_zinc")) return true;
            if (key.getPath().contains("raw_zinc")) return true;
        }
        return input.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "raw_materials/zinc")));
    }

    public static ItemStack getZincNugget() {
        Item zincNugget = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_nugget"));
        if (zincNugget != Items.AIR) {
            return new ItemStack(zincNugget, 1);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getSmeltingResult(Level level, ItemStack input) {
        if (input.isEmpty()) return ItemStack.EMPTY;

        // Food CANNOT be smelted in Brick Furnace!
        if (FoodCookingRegistry.isFood(input)) {
            return ItemStack.EMPTY;
        }

        // Zinc ore / raw zinc -> 1 Zinc Nugget (Create compatibility)
        if (isRawZinc(input)) {
            ItemStack nugget = getZincNugget();
            if (!nugget.isEmpty()) return nugget;
        }

        Supplier<ItemStack> custom = CUSTOM_SMELTING.get(input.getItem());
        if (custom != null) {
            return custom.get();
        }

        if (level != null) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(input);
            Optional<RecipeHolder<SmeltingRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeInput, level);
            if (match.isPresent()) {
                ItemStack result = match.get().value().assemble(recipeInput, level.registryAccess());
                // Strictly exclude any smelting recipe that yields food
                if (result.has(DataComponents.FOOD)) {
                    return ItemStack.EMPTY;
                }
                return result;
            }
        }

        return ItemStack.EMPTY;
    }
}
