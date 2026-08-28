package io.marrybye.github.betterthangamers.datagen;

import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput pOutput) {
        super(pOutput, CompletableFuture.completedFuture(HolderLookup.Provider.create(java.util.stream.Stream.empty())));
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        // Silicon shards to flint (4 shards = 1 flint)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.FLINT)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.SILICON_SHARD.get())
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput);

        // Stone nuggets to cobblestone (4 nuggets = 1 cobblestone)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE)
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.STONE_NUGGET.get())
                .unlockedBy("has_stone_nugget", has(ModItems.STONE_NUGGET.get()))
                .save(pRecipeOutput);

        // Rope crafting (from leather)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ROPE.get(), 2)
                .pattern("L")
                .pattern("S")
                .define('L', Items.LEATHER)
                .define('S', ModItems.SILICON_SHEARS.get())
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(pRecipeOutput, "rope_from_leather");

        // Lighter (2 sticks)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LIGHTER.get())
                .pattern("#")
                .pattern("#")
                .define('#', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(pRecipeOutput);

        // Silicon Shears (2 silicon shards crossed)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_SHEARS.get())
                .pattern("# ")
                .pattern(" #")
                .define('#', ModItems.SILICON_SHARD.get())
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput);

        // Silicon Axe (1 silicon, 2 sticks, 1 rope)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_AXE.get())
                .pattern("##")
                .pattern("X#")
                .pattern("X ")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput);

        // Silicon Pickaxe (3 silicon, 2 sticks, 1 rope)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_PICKAXE.get())
                .pattern("###")
                .pattern(" X ")
                .pattern(" X ")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput);

        // Silicon Spear (1 silicon, 1 stick, 1 rope)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILICON_SPEAR.get())
                .pattern("  #")
                .pattern(" X ")
                .pattern("X  ")
                .define('#', ModItems.SILICON_SHARD.get())
                .define('X', Items.STICK)
                .unlockedBy("has_silicon_shard", has(ModItems.SILICON_SHARD.get()))
                .save(pRecipeOutput);

        // Copper tools
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
                .pattern("###")
                .pattern(" X ")
                .pattern(" X ")
                .define('#', ModItems.COPPER_DUST.get())
                .define('X', Items.STICK)
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
                .pattern("##")
                .pattern("X#")
                .pattern("X ")
                .define('#', ModItems.COPPER_DUST.get())
                .define('X', Items.STICK)
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
                .pattern("#")
                .pattern("X")
                .pattern("X")
                .define('#', ModItems.COPPER_DUST.get())
                .define('X', Items.STICK)
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
                .pattern("##")
                .pattern(" X")
                .pattern(" X")
                .define('#', ModItems.COPPER_DUST.get())
                .define('X', Items.STICK)
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_SWORD.get())
                .pattern("#")
                .pattern("#")
                .pattern("X")
                .define('#', ModItems.COPPER_DUST.get())
                .define('X', Items.STICK)
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        // Copper armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
                .pattern("###")
                .pattern("# #")
                .define('#', ModItems.COPPER_DUST.get())
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.COPPER_DUST.get())
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.COPPER_DUST.get())
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.COPPER_DUST.get())
                .unlockedBy("has_copper_dust", has(ModItems.COPPER_DUST.get()))
                .save(pRecipeOutput);

        // Brick slab (already exists in vanilla, just ensuring recipe)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, io.marrybye.github.betterthangamers.block.ModBlocks.BRICK_SLAB.get(), 4)
                .pattern("###")
                .define('#', Blocks.BRICKS)
                .unlockedBy("has_bricks", has(Blocks.BRICKS))
                .save(pRecipeOutput);

        // Brick furnace (4 brick slabs)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, io.marrybye.github.betterthangamers.block.ModBlocks.BRICK_FURNACE.get())
                .pattern("##")
                .pattern("##")
                .define('#', io.marrybye.github.betterthangamers.block.ModBlocks.BRICK_SLAB.get())
                .unlockedBy("has_brick_slab", has(io.marrybye.github.betterthangamers.block.ModBlocks.BRICK_SLAB.get()))
                .save(pRecipeOutput);

        // Furnace smelting recipes
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

        // Iron nuggets to iron ingot (4 nuggets)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.IRON_INGOT)
                .pattern("##")
                .pattern("##")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(pRecipeOutput, "iron_ingot_from_nuggets");

        // Gold nuggets to gold ingot (4 nuggets)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GOLD_INGOT)
                .pattern("##")
                .pattern("##")
                .define('#', Items.GOLD_NUGGET)
                .unlockedBy("has_gold_nugget", has(Items.GOLD_NUGGET))
                .save(pRecipeOutput, "gold_ingot_from_nuggets");
    }
}

