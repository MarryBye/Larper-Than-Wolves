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
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.BRICK_FURNACE.get(), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(ModBlocks.BRICK_FURNACE.get(), RecipeTypes.FUELING);
        registration.addRecipeCatalyst(ModBlocks.ALLOY_MIXER.get(), RecipeTypes.FUELING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(BrickFurnaceScreen.class, 79, 34, 24, 17, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(io.marrybye.github.larperthanwolves.client.AlloyMixerScreen.class, 79, 24, 24, 17, RecipeTypes.SMELTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(BrickFurnaceMenu.class, ModMenuTypes.BRICK_FURNACE.get(), RecipeTypes.SMELTING, 0, 3, 6, 36);
        registration.addRecipeTransferHandler(io.marrybye.github.larperthanwolves.menu.AlloyMixerMenu.class, ModMenuTypes.ALLOY_MIXER.get(), RecipeTypes.SMELTING, 0, 3, 4, 36);
    }

    @Override
    public void onRuntimeAvailable(mezz.jei.api.runtime.IJeiRuntime jeiRuntime) {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(
                mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                java.util.List.of(
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.FURNACE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_PICKAXE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_AXE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_SHOVEL),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_SWORD),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_HOE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_AXE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_SHOVEL),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_SWORD),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_HOE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.CHAINMAIL_HELMET),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.CHAINMAIL_CHESTPLATE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.CHAINMAIL_LEGGINGS),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.CHAINMAIL_BOOTS),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_SWORD),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_PICKAXE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_AXE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_SHOVEL),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_HOE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_HELMET),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_CHESTPLATE),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_LEGGINGS),
                        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_BOOTS)
                )
        );
    }
}
