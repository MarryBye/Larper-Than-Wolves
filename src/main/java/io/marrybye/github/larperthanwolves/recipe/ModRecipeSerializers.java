package io.marrybye.github.larperthanwolves.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "larperthanwolves");

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<AxePlankRecipe>> AXE_PLANKS =
            RECIPE_SERIALIZERS.register("axe_planks", () -> new SimpleCraftingRecipeSerializer<>(AxePlankRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<WorkbenchPlankRecipe>> WORKBENCH_PLANKS =
            RECIPE_SERIALIZERS.register("workbench_planks", () -> new SimpleCraftingRecipeSerializer<>(WorkbenchPlankRecipe::new));
}
