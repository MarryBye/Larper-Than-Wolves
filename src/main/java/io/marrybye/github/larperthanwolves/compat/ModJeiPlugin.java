package io.marrybye.github.larperthanwolves.compat;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.entity.FuelRegistry;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import io.marrybye.github.larperthanwolves.item.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
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

        // 1. Automatically register categories for all workstations and machine blocks
        for (var blockEntry : ModBlocks.BLOCKS.getEntries()) {
            if (blockEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiCategories(registration, guiHelper);
            }
        }

        // 2. Global category systems
        registration.addRecipeCategories(new MachineFuelRecipeCategory(guiHelper));
        registration.addRecipeCategories(new GravelDiggingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // 1. Dispatch recipe and documentation registration to all mod blocks
        for (var blockEntry : ModBlocks.BLOCKS.getEntries()) {
            var block = blockEntry.get();
            if (block instanceof IJeiMachineStation station) {
                station.registerJeiRecipes(registration);
            }
            if (block instanceof IJeiDocumentationProvider doc) {
                doc.registerJeiInfo(registration);
            }
        }

        // 2. Dispatch recipe and documentation registration to all mod items
        for (var itemEntry : ModItems.ITEMS.getEntries()) {
            var item = itemEntry.get();
            if (item instanceof IJeiMachineStation station) {
                station.registerJeiRecipes(registration);
            }
            if (item instanceof IJeiDocumentationProvider doc) {
                doc.registerJeiInfo(registration);
            }
        }

        // 3. Register global categories (machine fuels and digging drops)
        registerGlobalFuelAndDiggingRecipes(registration);

        // 4. Register overhauled vanilla item documentation
        registerOverhauledVanillaDocumentation(registration);
    }

    private void registerGlobalFuelAndDiggingRecipes(IRecipeRegistration registration) {
        List<ItemStack> ignitionTools = List.of(
                new ItemStack(ModItems.LIGHTER.get()),
                new ItemStack(Items.FLINT_AND_STEEL)
        );
        List<ItemStack> machines = List.of(
                new ItemStack(ModBlocks.BRICK_FURNACE.get()),
                new ItemStack(ModBlocks.ADVANCED_SMELTER.get()),
                new ItemStack(ModBlocks.MITHRIL_FURNACE.get()),
                new ItemStack(ModBlocks.OVEN.get()),
                new ItemStack(ModBlocks.ALLOY_MIXER.get())
        );

        List<MachineFuelRecipe> fuelRecipes = new ArrayList<>();

        // Tier 1: Foliage & Twigs
        List<ItemStack> foliageFuels = List.of(
                new ItemStack(ModItems.TWIG.get()),
                new ItemStack(ModItems.DRY_GRASS.get()),
                new ItemStack(Items.DEAD_BUSH),
                new ItemStack(Items.OAK_SAPLING),
                new ItemStack(Items.BIRCH_SAPLING),
                new ItemStack(Items.SPRUCE_SAPLING),
                new ItemStack(Items.OAK_LEAVES),
                new ItemStack(Items.BIRCH_LEAVES)
        );
        FuelRegistry.FuelInfo foliageInfo = FuelRegistry.getFuelInfo(new ItemStack(ModItems.TWIG.get()));
        if (foliageInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(foliageFuels, foliageInfo.burnDuration, foliageInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 2: Sticks & Small Wood
        List<ItemStack> stickFuels = List.of(
                new ItemStack(Items.STICK),
                new ItemStack(ModItems.POINTED_STICK.get()),
                new ItemStack(Items.BOWL)
        );
        FuelRegistry.FuelInfo stickInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.STICK));
        if (stickInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(stickFuels, stickInfo.burnDuration, stickInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 3: Wooden Slabs, Stairs & Fences
        List<ItemStack> slabFuels = List.of(
                new ItemStack(Items.OAK_SLAB),
                new ItemStack(Items.BIRCH_SLAB),
                new ItemStack(Items.SPRUCE_SLAB),
                new ItemStack(Items.OAK_STAIRS),
                new ItemStack(Items.BIRCH_STAIRS),
                new ItemStack(Items.OAK_TRAPDOOR),
                new ItemStack(Items.OAK_FENCE),
                new ItemStack(Items.OAK_FENCE_GATE)
        );
        FuelRegistry.FuelInfo slabInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.OAK_SLAB));
        if (slabInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(slabFuels, slabInfo.burnDuration, slabInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 4: Planks & Wooden Blocks
        List<ItemStack> plankFuels = List.of(
                new ItemStack(Items.OAK_PLANKS),
                new ItemStack(Items.BIRCH_PLANKS),
                new ItemStack(Items.SPRUCE_PLANKS),
                new ItemStack(Items.JUNGLE_PLANKS),
                new ItemStack(Items.ACACIA_PLANKS),
                new ItemStack(Items.DARK_OAK_PLANKS),
                new ItemStack(Items.MANGROVE_PLANKS),
                new ItemStack(Items.CHERRY_PLANKS),
                new ItemStack(Items.BAMBOO_PLANKS),
                new ItemStack(Items.OAK_DOOR),
                new ItemStack(Items.OAK_BOAT),
                new ItemStack(Items.OAK_SIGN)
        );
        FuelRegistry.FuelInfo plankInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.OAK_PLANKS));
        if (plankInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(plankFuels, plankInfo.burnDuration, plankInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 5: Logs, Wood & Tree Stumps
        List<ItemStack> logFuels = List.of(
                new ItemStack(Items.OAK_LOG),
                new ItemStack(Items.BIRCH_LOG),
                new ItemStack(Items.SPRUCE_LOG),
                new ItemStack(Items.JUNGLE_LOG),
                new ItemStack(Items.ACACIA_LOG),
                new ItemStack(Items.DARK_OAK_LOG),
                new ItemStack(Items.MANGROVE_LOG),
                new ItemStack(Items.CHERRY_LOG),
                new ItemStack(Items.STRIPPED_OAK_LOG),
                new ItemStack(Items.OAK_WOOD),
                new ItemStack(ModBlocks.OAK_STUMP.get()),
                new ItemStack(ModBlocks.BIRCH_STUMP.get())
        );
        FuelRegistry.FuelInfo logInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.OAK_LOG));
        if (logInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(logFuels, logInfo.burnDuration, logInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 6: Charcoal
        FuelRegistry.FuelInfo charcoalInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.CHARCOAL));
        if (charcoalInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(Items.CHARCOAL)), charcoalInfo.burnDuration, charcoalInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 7: Mineral Coal
        FuelRegistry.FuelInfo coalInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.COAL));
        if (coalInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(Items.COAL)), coalInfo.burnDuration, coalInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 8: Blaze Rod
        FuelRegistry.FuelInfo blazeInfo = FuelRegistry.getFuelInfo(new ItemStack(Items.BLAZE_ROD));
        if (blazeInfo != null) {
            fuelRecipes.add(new MachineFuelRecipe(List.of(new ItemStack(Items.BLAZE_ROD)), blazeInfo.burnDuration, blazeInfo.cookSpeed, ignitionTools, machines));
        }

        // Tier 9: Coal Block
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
    }

    private void registerOverhauledVanillaDocumentation(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(Items.FLINT), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.flint"));
        registration.addIngredientInfo(new ItemStack(Blocks.GRAVEL), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.gravel"));
        registration.addIngredientInfo(new ItemStack(Blocks.DIRT), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.dirt"));

        // Clay block & clay ball
        registration.addIngredientInfo(List.of(
                new ItemStack(Blocks.CLAY),
                new ItemStack(Items.CLAY_BALL)
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.clay"));

        // Bone & bone meal
        registration.addIngredientInfo(List.of(
                new ItemStack(Items.BONE),
                new ItemStack(Items.BONE_MEAL)
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.bone_meal"));

        // Crafting table chisel carving
        registration.addIngredientInfo(new ItemStack(Blocks.CRAFTING_TABLE), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.crafting_table"));

        // Brick
        registration.addIngredientInfo(new ItemStack(Items.BRICK), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.brick"));

        // Silicon gear
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.SILICON_PICKAXE.get()),
                new ItemStack(ModItems.SILICON_AXE.get()),
                new ItemStack(ModItems.SILICON_SHOVEL.get()),
                new ItemStack(ModItems.SILICON_HOE.get()),
                new ItemStack(ModItems.SILICON_SHEARS.get()),
                new ItemStack(ModItems.SILICON_SPEAR.get())
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.silicon_gear"));

        // Copper gear
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.COPPER_PICKAXE.get()),
                new ItemStack(ModItems.COPPER_AXE.get()),
                new ItemStack(ModItems.COPPER_SHOVEL.get()),
                new ItemStack(ModItems.COPPER_HOE.get()),
                new ItemStack(ModItems.COPPER_SWORD.get()),
                new ItemStack(ModItems.COPPER_HELMET.get()),
                new ItemStack(ModItems.COPPER_CHESTPLATE.get()),
                new ItemStack(ModItems.COPPER_LEGGINGS.get()),
                new ItemStack(ModItems.COPPER_BOOTS.get())
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.copper_gear"));

        // Bronze gear
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.BRONZE_PICKAXE.get()),
                new ItemStack(ModItems.BRONZE_AXE.get()),
                new ItemStack(ModItems.BRONZE_SHOVEL.get()),
                new ItemStack(ModItems.BRONZE_HOE.get()),
                new ItemStack(ModItems.BRONZE_SWORD.get()),
                new ItemStack(ModItems.BRONZE_HELMET.get()),
                new ItemStack(ModItems.BRONZE_CHESTPLATE.get()),
                new ItemStack(ModItems.BRONZE_LEGGINGS.get()),
                new ItemStack(ModItems.BRONZE_BOOTS.get()),
                new ItemStack(ModItems.BRONZE_KNITTING_NEEDLES.get())
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.bronze_gear"));

        // Reinforced iron gear
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.REINFORCED_IRON_PICKAXE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_AXE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_SHOVEL.get()),
                new ItemStack(ModItems.REINFORCED_IRON_HOE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_SWORD.get()),
                new ItemStack(ModItems.REINFORCED_IRON_HELMET.get()),
                new ItemStack(ModItems.REINFORCED_IRON_CHESTPLATE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_LEGGINGS.get()),
                new ItemStack(ModItems.REINFORCED_IRON_BOOTS.get()),
                new ItemStack(ModItems.IRON_KNITTING_NEEDLES.get())
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.reinforced_iron_gear"));

        // Mithril gear
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.MITHRIL_PICKAXE.get()),
                new ItemStack(ModItems.MITHRIL_AXE.get()),
                new ItemStack(ModItems.MITHRIL_SHOVEL.get()),
                new ItemStack(ModItems.MITHRIL_HOE.get()),
                new ItemStack(ModItems.MITHRIL_SWORD.get()),
                new ItemStack(ModItems.MITHRIL_HELMET.get()),
                new ItemStack(ModItems.MITHRIL_CHESTPLATE.get()),
                new ItemStack(ModItems.MITHRIL_LEGGINGS.get()),
                new ItemStack(ModItems.MITHRIL_BOOTS.get())
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.mithril_gear"));

        // Hoe 2-stage tilling
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.SILICON_HOE.get()),
                new ItemStack(ModItems.COPPER_HOE.get()),
                new ItemStack(ModItems.BRONZE_HOE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_HOE.get()),
                new ItemStack(ModItems.MITHRIL_HOE.get()),
                new ItemStack(Items.IRON_HOE),
                new ItemStack(Items.DIAMOND_HOE),
                new ItemStack(Items.NETHERITE_HOE)
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.hoe_tilling"));

        // Axe plank crafting rule
        registration.addIngredientInfo(List.of(
                new ItemStack(ModItems.SILICON_AXE.get()),
                new ItemStack(ModItems.COPPER_AXE.get()),
                new ItemStack(ModItems.BRONZE_AXE.get()),
                new ItemStack(ModItems.REINFORCED_IRON_AXE.get()),
                new ItemStack(ModItems.MITHRIL_AXE.get()),
                new ItemStack(Items.IRON_AXE),
                new ItemStack(Items.DIAMOND_AXE),
                new ItemStack(Items.NETHERITE_AXE)
        ), VanillaTypes.ITEM_STACK, Component.translatable("jei.larperthanwolves.info.axe_planks"));

        // Villager trades & Iron golem balance
        registration.addIngredientInfo(new ItemStack(Items.EMERALD), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.villager_trades"));
        registration.addIngredientInfo(new ItemStack(Items.POPPY), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.iron_golem"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Automatically register catalysts for all workstations
        for (var blockEntry : ModBlocks.BLOCKS.getEntries()) {
            if (blockEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiCatalysts(registration);
            }
        }
        for (var itemEntry : ModItems.ITEMS.getEntries()) {
            if (itemEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiCatalysts(registration);
            }
        }

        // Global catalysts
        registration.addRecipeCatalyst(new ItemStack(ModItems.LIGHTER.get()), MachineFuelRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(Blocks.GRAVEL), GravelDiggingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.SILICON_SHARD.get()), GravelDiggingRecipeCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        for (var blockEntry : ModBlocks.BLOCKS.getEntries()) {
            if (blockEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiGuiHandlers(registration);
            }
        }
        for (var itemEntry : ModItems.ITEMS.getEntries()) {
            if (itemEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiGuiHandlers(registration);
            }
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        for (var blockEntry : ModBlocks.BLOCKS.getEntries()) {
            if (blockEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiRecipeTransferHandlers(registration);
            }
        }
        for (var itemEntry : ModItems.ITEMS.getEntries()) {
            if (itemEntry.get() instanceof IJeiMachineStation station) {
                station.registerJeiRecipeTransferHandlers(registration);
            }
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        List<ItemStack> toHide = new ArrayList<>(
                DisabledItemsHandler.DISABLED_ITEMS.stream()
                        .map(ItemStack::new)
                        .toList()
        );
        toHide.add(new ItemStack(Items.BARRIER));
        toHide.add(new ItemStack(Items.STRUCTURE_VOID));
        toHide.add(new ItemStack(Items.STRUCTURE_BLOCK));
        toHide.add(new ItemStack(Items.JIGSAW));

        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(
                VanillaTypes.ITEM_STACK,
                toHide
        );
    }
}
