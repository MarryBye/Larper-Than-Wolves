package io.marrybye.github.larperthanwolves.recipe;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class WoodToPlanksHelper {

    public static boolean isAxe(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof AxeItem ||
                stack.is(ItemTags.AXES);
    }

    public static ItemStack getPlanksForWood(ItemStack stack) {
        if (stack.isEmpty()) return null;
        Item item = stack.getItem();
        Block block = Block.byItem(item);

        // Oak
        if (block == Blocks.OAK_LOG || block == Blocks.STRIPPED_OAK_LOG ||
                block == Blocks.OAK_WOOD || block == Blocks.STRIPPED_OAK_WOOD ||
                block == ModBlocks.OAK_STUMP.get()) {
            return new ItemStack(Items.OAK_PLANKS);
        }
        // Spruce
        if (block == Blocks.SPRUCE_LOG || block == Blocks.STRIPPED_SPRUCE_LOG ||
                block == Blocks.SPRUCE_WOOD || block == Blocks.STRIPPED_SPRUCE_WOOD ||
                block == ModBlocks.SPRUCE_STUMP.get()) {
            return new ItemStack(Items.SPRUCE_PLANKS);
        }
        // Birch
        if (block == Blocks.BIRCH_LOG || block == Blocks.STRIPPED_BIRCH_LOG ||
                block == Blocks.BIRCH_WOOD || block == Blocks.STRIPPED_BIRCH_WOOD ||
                block == ModBlocks.BIRCH_STUMP.get()) {
            return new ItemStack(Items.BIRCH_PLANKS);
        }
        // Jungle
        if (block == Blocks.JUNGLE_LOG || block == Blocks.STRIPPED_JUNGLE_LOG ||
                block == Blocks.JUNGLE_WOOD || block == Blocks.STRIPPED_JUNGLE_WOOD ||
                block == ModBlocks.JUNGLE_STUMP.get()) {
            return new ItemStack(Items.JUNGLE_PLANKS);
        }
        // Acacia
        if (block == Blocks.ACACIA_LOG || block == Blocks.STRIPPED_ACACIA_LOG ||
                block == Blocks.ACACIA_WOOD || block == Blocks.STRIPPED_ACACIA_WOOD ||
                block == ModBlocks.ACACIA_STUMP.get()) {
            return new ItemStack(Items.ACACIA_PLANKS);
        }
        // Dark Oak
        if (block == Blocks.DARK_OAK_LOG || block == Blocks.STRIPPED_DARK_OAK_LOG ||
                block == Blocks.DARK_OAK_WOOD || block == Blocks.STRIPPED_DARK_OAK_WOOD ||
                block == ModBlocks.DARK_OAK_STUMP.get()) {
            return new ItemStack(Items.DARK_OAK_PLANKS);
        }
        // Mangrove
        if (block == Blocks.MANGROVE_LOG || block == Blocks.STRIPPED_MANGROVE_LOG ||
                block == Blocks.MANGROVE_WOOD || block == Blocks.STRIPPED_MANGROVE_WOOD ||
                block == ModBlocks.MANGROVE_STUMP.get()) {
            return new ItemStack(Items.MANGROVE_PLANKS);
        }
        // Cherry
        if (block == Blocks.CHERRY_LOG || block == Blocks.STRIPPED_CHERRY_LOG ||
                block == Blocks.CHERRY_WOOD || block == Blocks.STRIPPED_CHERRY_WOOD ||
                block == ModBlocks.CHERRY_STUMP.get()) {
            return new ItemStack(Items.CHERRY_PLANKS);
        }
        // Bamboo
        if (block == Blocks.BAMBOO_BLOCK || block == Blocks.STRIPPED_BAMBOO_BLOCK) {
            return new ItemStack(Items.BAMBOO_PLANKS);
        }
        // Crimson
        if (block == Blocks.CRIMSON_STEM || block == Blocks.STRIPPED_CRIMSON_STEM ||
                block == Blocks.CRIMSON_HYPHAE || block == Blocks.STRIPPED_CRIMSON_HYPHAE ||
                block == ModBlocks.CRIMSON_STUMP.get()) {
            return new ItemStack(Items.CRIMSON_PLANKS);
        }
        // Warped
        if (block == Blocks.WARPED_STEM || block == Blocks.STRIPPED_WARPED_STEM ||
                block == Blocks.WARPED_HYPHAE || block == Blocks.STRIPPED_WARPED_HYPHAE ||
                block == ModBlocks.WARPED_STUMP.get()) {
            return new ItemStack(Items.WARPED_PLANKS);
        }

        return null;
    }
}
