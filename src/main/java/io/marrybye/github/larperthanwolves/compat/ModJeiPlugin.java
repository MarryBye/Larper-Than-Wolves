package io.marrybye.github.larperthanwolves.compat;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.entity.FuelRegistry;
import io.marrybye.github.larperthanwolves.client.AlloyMixerScreen;
import io.marrybye.github.larperthanwolves.client.BrickFurnaceScreen;
import io.marrybye.github.larperthanwolves.client.OvenScreen;
import io.marrybye.github.larperthanwolves.client.SieveScreen;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.AlloyMixerMenu;
import io.marrybye.github.larperthanwolves.menu.BrickFurnaceMenu;
import io.marrybye.github.larperthanwolves.menu.OvenMenu;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import io.marrybye.github.larperthanwolves.menu.SieveMenu;
import io.marrybye.github.larperthanwolves.recipe.AlloyRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AlloyMixerRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SieveRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ChiselRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SunDryingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new DryingRackRecipeCategory(guiHelper));
        registration.addRecipeCategories(new MachineFuelRecipeCategory(guiHelper));
        registration.addRecipeCategories(new GravelDiggingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        int cookTime = ModConfig.SERVER != null ? ModConfig.SERVER.alloyMixerCookTimeTicks.get() : 600;
        registration.addRecipes(AlloyMixerRecipeCategory.TYPE, AlloyRegistry.getJeiRecipes(cookTime));

        // 1. Sieve recipes
        int sieveTime = ModConfig.SERVER != null ? ModConfig.SERVER.sieveProcessTimeTicks.get() : 100;
        List<ItemStack> regularSoilOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get())
        );

        List<ItemStack> suspGravelOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(Items.EMERALD),
                new ItemStack(Items.WHEAT),
                new ItemStack(Items.BURN_POTTERY_SHERD)
        );

        List<ItemStack> suspSandOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.EMERALD),
                new ItemStack(Items.SNIFFER_EGG),
                new ItemStack(Items.ARCHER_POTTERY_SHERD)
        );

        List<ItemStack> richSoilOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(ModItems.TIN_DUST.get()),
                new ItemStack(ModItems.BRONZE_DUST.get()),
                new ItemStack(ModItems.IRON_DUST.get()),
                new ItemStack(ModItems.GOLD_DUST.get()),
                new ItemStack(ModItems.DIAMOND_DUST.get())
        );

        registration.addRecipes(SieveRecipeCategory.TYPE, List.of(
                new SieveJeiRecipe(new ItemStack(Blocks.GRAVEL), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.SAND), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.RED_SAND), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.DIRT), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.SUSPICIOUS_GRAVEL), suspGravelOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.SUSPICIOUS_SAND), suspSandOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_DIRT.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_GRAVEL.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_SAND.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_RED_SAND.get()), richSoilOutputs, sieveTime)
        ));

        // 2. Chisel In-World Carving (Overworld tree stumps only)
        List<ItemStack> carvableStumps = List.of(
                new ItemStack(ModBlocks.OAK_STUMP.get()),
                new ItemStack(ModBlocks.BIRCH_STUMP.get()),
                new ItemStack(ModBlocks.SPRUCE_STUMP.get()),
                new ItemStack(ModBlocks.JUNGLE_STUMP.get()),
                new ItemStack(ModBlocks.ACACIA_STUMP.get()),
                new ItemStack(ModBlocks.DARK_OAK_STUMP.get()),
                new ItemStack(ModBlocks.MANGROVE_STUMP.get()),
                new ItemStack(ModBlocks.CHERRY_STUMP.get())
        );

        registration.addRecipes(ChiselRecipeCategory.TYPE, List.of(
                new ChiselRecipe(
                        carvableStumps,
                        new ItemStack(ModItems.CHISEL.get()),
                        new ItemStack(ModBlocks.WORK_STUMP.get()),
                        new ItemStack(Items.CRAFTING_TABLE),
                        4
                )
        ));

        // 3. Sun Drying
        registration.addRecipes(SunDryingRecipeCategory.TYPE, List.of(
                new SunDryingRecipe(
                        new ItemStack(ModBlocks.UNFIRED_BRICK.asItem()),
                        new ItemStack(Items.BRICK),
                        2000
                )
        ));

        // 3b. Drying Rack Recipes
        int dryingRackTime = ModConfig.SERVER != null ? ModConfig.SERVER.dryingRackTimeTicks.get() : 1200;
        List<ItemStack> grassInputs = List.of(
                new ItemStack(Items.SHORT_GRASS),
                new ItemStack(Items.TALL_GRASS),
                new ItemStack(Items.FERN),
                new ItemStack(Items.LARGE_FERN),
                new ItemStack(Items.SEAGRASS)
        );
        registration.addRecipes(DryingRackRecipeCategory.TYPE, List.of(
                new DryingRackRecipe(
                        grassInputs,
                        new ItemStack(ModItems.DRY_GRASS.get()),
                        dryingRackTime
                ),
                new DryingRackRecipe(
                        List.of(new ItemStack(Items.LEATHER)),
                        new ItemStack(ModItems.TANNED_LEATHER.get()),
                        dryingRackTime
                )
        ));

        // 4. Machine Fuel & Ignition Recipes
        List<ItemStack> ignitionTools = List.of(
                new ItemStack(ModItems.LIGHTER.get()),
                new ItemStack(Items.FLINT_AND_STEEL)
        );
        List<ItemStack> machines = List.of(
                new ItemStack(ModBlocks.BRICK_FURNACE.get()),
                new ItemStack(ModBlocks.OVEN.get()),
                new ItemStack(ModBlocks.ALLOY_MIXER.get())
        );

        List<ItemStack> allLogs = List.of(
                new ItemStack(Items.OAK_LOG),
                new ItemStack(Items.BIRCH_LOG),
                new ItemStack(Items.SPRUCE_LOG),
                new ItemStack(Items.JUNGLE_LOG),
                new ItemStack(Items.ACACIA_LOG),
                new ItemStack(Items.DARK_OAK_LOG),
                new ItemStack(Items.MANGROVE_LOG),
                new ItemStack(Items.CHERRY_LOG)
        );

        List<ItemStack> allPlanks = List.of(
                new ItemStack(Items.OAK_PLANKS),
                new ItemStack(Items.BIRCH_PLANKS),
                new ItemStack(Items.SPRUCE_PLANKS),
                new ItemStack(Items.JUNGLE_PLANKS),
                new ItemStack(Items.ACACIA_PLANKS),
                new ItemStack(Items.DARK_OAK_PLANKS),
                new ItemStack(Items.MANGROVE_PLANKS),
                new ItemStack(Items.CHERRY_PLANKS)
        );

        List<MachineFuelRecipe> fuelRecipes = new ArrayList<>();

        FuelRegistry.FuelInfo logInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.OAK_LOG));
        if (logInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(allLogs, logInfo.burnDuration, logInfo.cookSpeed, ignitionTools, machines));
        }

        FuelRegistry.FuelInfo plankInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.OAK_PLANKS));
        if (plankInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(allPlanks, plankInfo.burnDuration, plankInfo.cookSpeed, ignitionTools, machines));
        }

        FuelRegistry.FuelInfo coalInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.COAL));
        if (coalInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(Items.COAL), new ItemStack(Items.CHARCOAL)), coalInfo.burnDuration, coalInfo.cookSpeed, ignitionTools, machines));
        }

        FuelRegistry.FuelInfo stickInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.STICK));
        if (stickInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(Items.STICK)), stickInfo.burnDuration, stickInfo.cookSpeed, ignitionTools, machines));
        }

        FuelRegistry.FuelInfo grassInfo = FuelRegistry.getFuelInfo(new ItemStack(ModItems.DRY_GRASS.get()));
        if (grassInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(ModItems.DRY_GRASS.get())), grassInfo.burnDuration, grassInfo.cookSpeed, ignitionTools, machines));
        }

        FuelRegistry.FuelInfo blockInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.COAL_BLOCK));
        if (blockInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(Items.COAL_BLOCK)), blockInfo.burnDuration, blockInfo.cookSpeed, ignitionTools, machines));
        }

        registration.addRecipes(MachineFuelRecipeCategory.TYPE, fuelRecipes);

        // 5. Gravel & Soil Digging
        registration.addRecipes(GravelDiggingRecipeCategory.TYPE, List.of(
                new GravelDiggingRecipe(
                        new ItemStack(Blocks.GRAVEL),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.SILICON_SHARD.get()), "20%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Items.FLINT), "8%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.COPPER_DUST.get()), "2%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.GRAVEL.asItem()), "70%")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(Blocks.DIRT),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.SILICON_SHARD.get()), "20%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Items.FLINT), "8%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.COPPER_DUST.get()), "2%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.DIRT.asItem()), "70%")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(Blocks.SAND),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.SILICON_SHARD.get()), "20%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Items.FLINT), "8%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.COPPER_DUST.get()), "2%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.SAND.asItem()), "70%")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(Blocks.RED_SAND),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.SILICON_SHARD.get()), "20%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Items.FLINT), "8%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModItems.COPPER_DUST.get()), "2%"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.RED_SAND.asItem()), "70%")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(ModBlocks.RICH_DIRT.get()),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModBlocks.RICH_DIRT.get()), "Cu+"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.DIRT), "Other")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(ModBlocks.RICH_GRAVEL.get()),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModBlocks.RICH_GRAVEL.get()), "Cu+"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.GRAVEL), "Other")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(ModBlocks.RICH_SAND.get()),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModBlocks.RICH_SAND.get()), "Cu+"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.SAND), "Other")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(ModBlocks.RICH_RED_SAND.get()),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModBlocks.RICH_RED_SAND.get()), "Cu+"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.RED_SAND), "Other")
                        )
                ),
                new GravelDiggingRecipe(
                        new ItemStack(ModBlocks.RICH_GRASS_BLOCK.get()),
                        List.of(
                                new GravelDiggingRecipe.DropEntry(new ItemStack(ModBlocks.RICH_DIRT.get()), "Cu+"),
                                new GravelDiggingRecipe.DropEntry(new ItemStack(Blocks.DIRT), "Other")
                        )
                )
        ));

        // 6. In-Depth Ingredient Information (JEI Info Pages)
        registration.addIngredientInfo(new ItemStack(ModItems.TWIG.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.twig"));
        registration.addIngredientInfo(new ItemStack(ModItems.SILICON_SHARD.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.silicon_shard"));
        registration.addIngredientInfo(new ItemStack(Items.FLINT), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.flint"));
        registration.addIngredientInfo(new ItemStack(Blocks.GRAVEL), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.gravel"));
        registration.addIngredientInfo(new ItemStack(Blocks.DIRT), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.dirt"));

        List<ItemStack> richSoils = List.of(
                new ItemStack(ModBlocks.RICH_GRASS_BLOCK.get()),
                new ItemStack(ModBlocks.RICH_DIRT.get()),
                new ItemStack(ModBlocks.RICH_GRAVEL.get()),
                new ItemStack(ModBlocks.RICH_SAND.get()),
                new ItemStack(ModBlocks.RICH_RED_SAND.get())
        );
        registration.addIngredientInfo(richSoils, VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.rich_soil"));

        registration.addIngredientInfo(new ItemStack(ModItems.UNBOUND_MESH.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.unbound_mesh"));
        registration.addIngredientInfo(new ItemStack(ModItems.MESH.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.mesh"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.BASKET.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.basket"));
        registration.addIngredientInfo(List.of(new ItemStack(Items.CLAY), new ItemStack(Items.CLAY_BALL)), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.clay"));

        registration.addIngredientInfo(new ItemStack(Items.CRAFTING_TABLE), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.crafting_table"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.WORK_STUMP.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.work_stump"));
        registration.addIngredientInfo(new ItemStack(ModItems.CHISEL.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.chisel"));

        registration.addIngredientInfo(new ItemStack(ModBlocks.UNFIRED_BRICK.asItem()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.unfired_brick"));
        registration.addIngredientInfo(new ItemStack(ModItems.UNFIRED_BRICK.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.unfired_brick"));
        registration.addIngredientInfo(new ItemStack(Items.BRICK), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.brick"));

        registration.addIngredientInfo(new ItemStack(ModBlocks.BRICK_FURNACE.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.brick_furnace"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.OVEN.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.oven"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.ALLOY_MIXER.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.alloy_mixer"));
        registration.addIngredientInfo(new ItemStack(ModItems.LIGHTER.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.lighter"));

        registration.addIngredientInfo(new ItemStack(ModBlocks.DRYING_RACK.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.drying_rack"));
        registration.addIngredientInfo(new ItemStack(ModItems.DRY_GRASS.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.dry_grass"));
        registration.addIngredientInfo(new ItemStack(ModItems.TANNED_LEATHER.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.tanned_leather"));
        registration.addIngredientInfo(new ItemStack(ModItems.SILICON_AXE.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.axe_planks"));

        // Hoe 2-stage tilling & seed harvesting
        List<ItemStack> allHoes = List.of(
                new ItemStack(ModItems.SILICON_HOE.get()),
                new ItemStack(ModItems.COPPER_HOE.get()),
                new ItemStack(ModItems.BRONZE_HOE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_HOE.get()),
                new ItemStack(Items.IRON_HOE),
                new ItemStack(Items.NETHERITE_HOE)
        );
        registration.addIngredientInfo(allHoes, VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.hoe_tilling"));

        // Villager Trading
        List<ItemStack> tradeSamples = List.of(
                new ItemStack(ModItems.BRONZE_SWORD.get()),
                new ItemStack(ModItems.BRONZE_PICKAXE.get()),
                new ItemStack(ModItems.BRONZE_CHESTPLATE.get()),
                new ItemStack(Items.EMERALD)
        );
        registration.addIngredientInfo(tradeSamples, VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.villager_trades"));

        // Iron Golem drops
        registration.addIngredientInfo(new ItemStack(Items.POPPY), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.iron_golem"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.BRICK_FURNACE.get(), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(ModBlocks.BRICK_FURNACE.get(), MachineFuelRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.OVEN.get(), RecipeTypes.SMOKING);
        registration.addRecipeCatalyst(ModBlocks.OVEN.get(), RecipeTypes.CAMPFIRE_COOKING);
        registration.addRecipeCatalyst(ModBlocks.OVEN.get(), MachineFuelRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.ALLOY_MIXER.get(), AlloyMixerRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.ALLOY_MIXER.get(), MachineFuelRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.SIEVE.get(), SieveRecipeCategory.TYPE);

        registration.addRecipeCatalyst(ModItems.CHISEL.get(), ChiselRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.WORK_STUMP.get(), ChiselRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.UNFIRED_BRICK.get(), SunDryingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.DRYING_RACK.get(), DryingRackRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModItems.LIGHTER.get(), MachineFuelRecipeCategory.TYPE);
        registration.addRecipeCatalyst(Blocks.GRAVEL, GravelDiggingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModItems.SILICON_SHARD.get(), GravelDiggingRecipeCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(BrickFurnaceScreen.class, 79, 34, 24, 17, RecipeTypes.SMELTING, MachineFuelRecipeCategory.TYPE);
        registration.addRecipeClickArea(OvenScreen.class, 79, 34, 24, 17, RecipeTypes.SMOKING, RecipeTypes.CAMPFIRE_COOKING, MachineFuelRecipeCategory.TYPE);
        registration.addRecipeClickArea(AlloyMixerScreen.class, 79, 24, 24, 17, AlloyMixerRecipeCategory.TYPE, MachineFuelRecipeCategory.TYPE);
        registration.addRecipeClickArea(SieveScreen.class, 76, 34, 24, 17, SieveRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(BrickFurnaceMenu.class, ModMenuTypes.BRICK_FURNACE.get(), RecipeTypes.SMELTING, 0, 3, 6, 36);
        registration.addRecipeTransferHandler(OvenMenu.class, ModMenuTypes.OVEN.get(), RecipeTypes.SMOKING, 0, 3, 6, 36);
        registration.addRecipeTransferHandler(OvenMenu.class, ModMenuTypes.OVEN.get(), RecipeTypes.CAMPFIRE_COOKING, 0, 3, 6, 36);
        registration.addRecipeTransferHandler(AlloyMixerMenu.class, ModMenuTypes.ALLOY_MIXER.get(), AlloyMixerRecipeCategory.TYPE, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(SieveMenu.class, ModMenuTypes.SIEVE.get(), SieveRecipeCategory.TYPE, 0, 9, 18, 36);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(
                VanillaTypes.ITEM_STACK,
                DisabledItemsHandler.DISABLED_ITEMS.stream()
                        .map(ItemStack::new)
                        .toList()
        );
    }
}
