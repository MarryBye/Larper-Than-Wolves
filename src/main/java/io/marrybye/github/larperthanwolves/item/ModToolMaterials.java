package io.marrybye.github.larperthanwolves.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolMaterials {
    public static final TagKey<Block> INCORRECT_FOR_SILICON = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("larperthanwolves", "incorrect_for_silicon_tool")
    );

    public static final TagKey<Block> INCORRECT_FOR_COPPER = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("larperthanwolves", "incorrect_for_copper_tool")
    );

    public static final TagKey<Block> INCORRECT_FOR_BRONZE = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("larperthanwolves", "incorrect_for_bronze_tool")
    );

    // Silicon tools: durability 30, speed 2.5f, bonus 0.0f
    public static final Tier SILICON = new SimpleTier(
            INCORRECT_FOR_SILICON,
            30,
            2.5f,
            0.0f,
            5,
            () -> Ingredient.of(ModItems.SILICON_SHARD.get())
    );

    // Copper tools: durability 100, speed 4.5f, bonus 1.5f
    public static final Tier COPPER = new SimpleTier(
            INCORRECT_FOR_COPPER,
            100,
            4.5f,
            1.5f,
            10,
            () -> Ingredient.of(net.minecraft.world.item.Items.COPPER_INGOT, ModItems.COPPER_DUST.get())
    );

    // Bronze tools: durability 150, speed 5.5f, bonus 2.0f
    public static final Tier BRONZE = new SimpleTier(
            INCORRECT_FOR_BRONZE,
            150,
            5.5f,
            2.0f,
            12,
            () -> Ingredient.of(ModItems.BRONZE_INGOT.get())
    );

    // Reinforced Iron tools (Diamond tier): durability 500, speed 8.0f, bonus 3.0f
    public static final Tier REINFORCED_IRON = new SimpleTier(
            net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            500,
            8.0f,
            3.0f,
            10,
            () -> Ingredient.of(ModItems.DIAMOND_INGOT.get())
    );
}

