package io.marrybye.github.larperthanwolves.compat;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.client.BrickFurnaceScreen;
import io.marrybye.github.larperthanwolves.menu.BrickFurnaceMenu;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(mezz.jei.api.registration.IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AlloyMixerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SieveRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(mezz.jei.api.registration.IRecipeRegistration registration) {
        int cookTime = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ?
                io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.alloyMixerCookTimeTicks.get() : 600;

        registration.addRecipes(AlloyMixerRecipeCategory.TYPE, java.util.List.of(
                new AlloyMixerRecipe(
                        java.util.List.of(
                                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND),
                                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_INGOT),
                                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.COPPER_INGOT)
                        ),
                        new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.DIAMOND_INGOT.get()),
                        cookTime
                ),
                new AlloyMixerRecipe(
                        java.util.List.of(
                                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.COPPER_INGOT, 2),
                                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.TIN_INGOT.get())
                        ),
                        new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.BRONZE_INGOT.get()),
                        cookTime
                )
        ));

        int sieveTime = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ?
                io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.sieveProcessTimeTicks.get() : 100;

        java.util.List<net.minecraft.world.item.ItemStack> gravelOutputs = java.util.List.of(
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.COPPER_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.TIN_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.IRON_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.GOLD_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.SILICON_SHARD.get()),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.FLINT)
        );

        java.util.List<net.minecraft.world.item.ItemStack> sandOutputs = java.util.List.of(
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.COPPER_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.TIN_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.IRON_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.GOLD_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.SILICON_SHARD.get()),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.FLINT)
        );

        java.util.List<net.minecraft.world.item.ItemStack> suspGravelOutputs = java.util.List.of(
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.COPPER_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.TIN_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.IRON_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.GOLD_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.SILICON_SHARD.get()),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.FLINT),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WHEAT),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.BURN_POTTERY_SHERD)
        );

        java.util.List<net.minecraft.world.item.ItemStack> suspSandOutputs = java.util.List.of(
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.COPPER_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.TIN_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.IRON_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.GOLD_DUST.get()),
                new net.minecraft.world.item.ItemStack(io.marrybye.github.larperthanwolves.item.ModItems.SILICON_SHARD.get()),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.FLINT),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.EMERALD),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.SNIFFER_EGG),
                new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ARCHER_POTTERY_SHERD)
        );

        registration.addRecipes(SieveRecipeCategory.TYPE, java.util.List.of(
                new SieveJeiRecipe(new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.GRAVEL), gravelOutputs, sieveTime),
                new SieveJeiRecipe(new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.SAND), sandOutputs, sieveTime),
                new SieveJeiRecipe(new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.RED_SAND), sandOutputs, sieveTime),
                new SieveJeiRecipe(new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.SUSPICIOUS_GRAVEL), suspGravelOutputs, sieveTime),
                new SieveJeiRecipe(new net.minecraft.world.item.ItemStack(net.minecraft.world.level.block.Blocks.SUSPICIOUS_SAND), suspSandOutputs, sieveTime)
        ));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.BRICK_FURNACE.get(), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(ModBlocks.BRICK_FURNACE.get(), RecipeTypes.FUELING);
        registration.addRecipeCatalyst(ModBlocks.ALLOY_MIXER.get(), AlloyMixerRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.ALLOY_MIXER.get(), RecipeTypes.FUELING);
        registration.addRecipeCatalyst(ModBlocks.SIEVE.get(), SieveRecipeCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(BrickFurnaceScreen.class, 79, 34, 24, 17, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(io.marrybye.github.larperthanwolves.client.AlloyMixerScreen.class, 79, 24, 24, 17, AlloyMixerRecipeCategory.TYPE);
        registration.addRecipeClickArea(io.marrybye.github.larperthanwolves.client.SieveScreen.class, 76, 34, 24, 17, SieveRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(BrickFurnaceMenu.class, ModMenuTypes.BRICK_FURNACE.get(), RecipeTypes.SMELTING, 0, 3, 6, 36);
        registration.addRecipeTransferHandler(io.marrybye.github.larperthanwolves.menu.AlloyMixerMenu.class, ModMenuTypes.ALLOY_MIXER.get(), AlloyMixerRecipeCategory.TYPE, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(io.marrybye.github.larperthanwolves.menu.SieveMenu.class, ModMenuTypes.SIEVE.get(), SieveRecipeCategory.TYPE, 0, 9, 18, 36);
    }

    @Override
    public void onRuntimeAvailable(mezz.jei.api.runtime.IJeiRuntime jeiRuntime) {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(
                mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                io.marrybye.github.larperthanwolves.event.DisabledItemsHandler.DISABLED_ITEMS.stream()
                        .map(net.minecraft.world.item.ItemStack::new)
                        .toList()
        );
    }
}
