package io.marrybye.github.betterthangamers.datagen;

import io.marrybye.github.betterthangamers.block.ModBlocks;
import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput pOutput) {
        super(pOutput, CompletableFuture.completedFuture(HolderLookup.Provider.create(java.util.stream.Stream.empty())));
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        // --- 1. Basic Items & Nuggets Conversion ---

        // 4 Silicon shards -> 1 Flint
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.FLINT)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.SILICON_SHARD.get())
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput, "flint_from_silicon_shards");

        // 4 Stone nuggets -> 1 Cobblestone
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.STONE_NUGGET.get())
                .unlockedBy("has_stone_nugget", has(ModItems.STONE_NUGGET.get()))
                .save(pRecipeOutput, "cobblestone_from_stone_nuggets");

        // 4 Diorite nuggets -> 1 Diorite
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DIORITE)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.DIORITE_NUGGET.get())
                .unlockedBy("has_diorite_nugget", has(ModItems.DIORITE_NUGGET.get()))
                .save(pRecipeOutput, "diorite_from_nuggets");

        // 4 Granite nuggets -> 1 Granite
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.GRANITE)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.GRANITE_NUGGET.get())
                .unlockedBy("has_granite_nugget", has(ModItems.GRANITE_NUGGET.get()))
                .save(pRecipeOutput, "granite_from_nuggets");

        // 4 Andesite nuggets -> 1 Andesite
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.ANDESITE)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.ANDESITE_NUGGET.get())
                .unlockedBy("has_andesite_nugget", has(ModItems.ANDESITE_NUGGET.get()))
                .save(pRecipeOutput, "andesite_from_nuggets");

        // 4 Tuff nuggets -> 1 Tuff
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.TUFF_NUGGET.get())
                .unlockedBy("has_tuff_nugget", has(ModItems.TUFF_NUGGET.get()))
                .save(pRecipeOutput, "tuff_from_nuggets");

        // --- 2. Ore Dust to Raw Ore (2 dusts = 1 raw ore) ---

        // 2 Iron dust -> 1 Raw Iron
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RAW_IRON)
                .requires(ModItems.IRON_DUST.get(), 2)
                .unlockedBy("has_iron_dust", has(ModItems.IRON_DUST.get()))
                .save(pRecipeOutput, "raw_iron_from_dust");

        // 2 Copper dust -> 1 Raw Copper
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RAW_COPPER)
                .requires(ModItems.COPPER_DUST.get(), 2)
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput, "raw_copper_from_dust");

        // 2 Gold dust -> 1 Raw Gold
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RAW_GOLD)
                .requires(ModItems.GOLD_DUST.get(), 2)
                .unlockedBy("has_gold_dust", has(ModItems.GOLD_DUST.get()))
                .save(pRecipeOutput, "raw_gold_from_dust");

        // --- 3. Nuggets & Dusts to Ingots (4 nuggets = 1 ingot) ---

        // 4 Iron nuggets -> 1 Iron Ingot
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.IRON_INGOT)
                .pattern("##")
                .pattern("##")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(pRecipeOutput, "iron_ingot_from_nuggets");

        // 4 Gold nuggets -> 1 Gold Ingot
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GOLD_INGOT)
                .pattern("##")
                .pattern("##")
                .define('#', Items.GOLD_NUGGET)
                .unlockedBy("has_gold_nugget", has(Items.GOLD_NUGGET))
                .save(pRecipeOutput, "gold_ingot_from_nuggets");

        // 4 Copper dust -> 1 Copper Ingot
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.COPPER_DUST.get())
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput, "copper_ingot_from_dust");

        // --- 4. Survival Utilities (Rope & Lighter) ---

        // Rope from leather + shears
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ROPE.get(), 2)
                .requires(Items.LEATHER)
                .requires(ModItems.SILICON_SHEARS.get())
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(pRecipeOutput, "rope_from_leather");

        // Rope from dry grass
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ROPE.get(), 1)
                .requires(ModItems.DRY_GRASS.get(), 3)
                .unlockedBy("has_dry_grass", has(ModItems.DRY_GRASS.get()))
                .save(pRecipeOutput, "rope_from_dry_grass");

        // Lighter (2 sticks)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.LIGHTER.get())
                .pattern("#")
                .pattern("#")
                .define('#', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(pRecipeOutput, "lighter_from_sticks");

        // --- 5. Silicon Tools ---

        // Silicon Shears (2 silicon shards crossed)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_SHEARS.get())
                .pattern("# ")
                .pattern(" #")
                .define('#', ModItems.SILICON_SHARD.get())
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput, "silicon_shears");

        // Silicon Spear (1 silicon shard, 1 rope, 1 stick)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILICON_SPEAR.get())
                .pattern("  #")
                .pattern(" R ")
                .pattern("X  ")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('R', ModItems.ROPE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput, "silicon_spear");

        // Silicon Axe (1 silicon shard, 2 sticks, 1 rope)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_AXE.get())
                .pattern("#R")
                .pattern("X#")
                .pattern("X ")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('R', ModItems.ROPE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput, "silicon_axe");

        // Silicon Pickaxe (3 silicon shards, 2 sticks, 1 rope) - requires crafting table
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_PICKAXE.get())
                .pattern("###")
                .pattern("XR ")
                .pattern("X  ")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('R', ModItems.ROPE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput, "silicon_pickaxe");

        // Silicon Shovel (1 silicon shard, 2 sticks, 1 rope)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_SHOVEL.get())
                .pattern("#")
                .pattern("R")
                .pattern("X")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('R', ModItems.ROPE.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput, "silicon_shovel");

        // --- 6. Copper Tools & Armor ---

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_SWORD.get())
                .pattern("#")
                .pattern("#")
                .pattern("X")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_sword");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
                .pattern("###")
                .pattern(" X ")
                .pattern(" X ")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_pickaxe");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
                .pattern("##")
                .pattern("X#")
                .pattern("X ")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_axe");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
                .pattern("#")
                .pattern("X")
                .pattern("X")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_shovel");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
                .pattern("##")
                .pattern(" X")
                .pattern(" X")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_hoe");

        // Copper Armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
                .pattern("###")
                .pattern("# #")
                .define('#', Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_helmet");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_chestplate");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_leggings");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(pRecipeOutput, "copper_boots");

        // --- 7. Brick Slab & Brick Furnace (Craftable in 2x2 player inventory!) ---

        // 2 Bricks (blocks) -> 4 Brick Slabs (2x1 horizontal in 2x2 grid)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.BRICK_SLAB, 4)
                .pattern("##")
                .define('#', Blocks.BRICKS)
                .unlockedBy("has_bricks", has(Blocks.BRICKS))
                .save(pRecipeOutput, "brick_slab_from_bricks");

        // 2 Brick (items) -> 2 Brick Slabs (2x1 horizontal in 2x2 grid)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.BRICK_SLAB, 2)
                .pattern("##")
                .define('#', Items.BRICK)
                .unlockedBy("has_brick_item", has(Items.BRICK))
                .save(pRecipeOutput, "brick_slab_from_brick_items");

        // 4 Brick Slabs -> 1 Brick Furnace (2x2 grid)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BRICK_FURNACE.get())
                .pattern("##")
                .pattern("##")
                .define('#', Items.BRICK_SLAB)
                .unlockedBy("has_brick_slab", has(Items.BRICK_SLAB))
                .save(pRecipeOutput, "brick_furnace_from_slabs");

        // --- 8. Vanilla Smelting Overrides ---
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(Items.RAW_IRON),
                RecipeCategory.MISC,
                Items.IRON_NUGGET,
                0.7f,
                200)
                .unlockedBy("has_raw_iron", has(Items.RAW_IRON))
                .save(pRecipeOutput, "iron_nugget_from_smelting");

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(Items.RAW_COPPER),
                RecipeCategory.MISC,
                ModItems.COPPER_DUST.get(),
                0.7f,
                200)
                .unlockedBy("has_raw_copper", has(Items.RAW_COPPER))
                .save(pRecipeOutput, "copper_dust_from_smelting");

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(Items.RAW_GOLD),
                RecipeCategory.MISC,
                Items.GOLD_NUGGET,
                1.0f,
                200)
                .unlockedBy("has_raw_gold", has(Items.RAW_GOLD))
                .save(pRecipeOutput, "gold_nugget_from_smelting");

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ModItems.IRON_DUST.get()),
                RecipeCategory.MISC,
                Items.IRON_NUGGET,
                0.7f,
                200)
                .unlockedBy("has_iron_dust", has(ModItems.IRON_DUST.get()))
                .save(pRecipeOutput, "iron_nugget_from_dust_smelting");

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ModItems.GOLD_DUST.get()),
                RecipeCategory.MISC,
                Items.GOLD_NUGGET,
                1.0f,
                200)
                .unlockedBy("has_gold_dust", has(ModItems.GOLD_DUST.get()))
                .save(pRecipeOutput, "gold_nugget_from_dust_smelting");
    }
}

