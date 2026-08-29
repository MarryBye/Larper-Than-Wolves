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

    // Silicon tools: 3x less durability than wooden tools (wood = 59, 59/3 = 20 uses), wood speed (2.0f)
    public static final Tier SILICON = new SimpleTier(
            INCORRECT_FOR_SILICON,
            20,
            2.0f,
            0.0f,
            5,
            () -> Ingredient.of(ModItems.SILICON_SHARD.get())
    );

    // Copper tools: same durability as stone (131 uses), speed 4.5f, attack bonus 1.5f
    public static final Tier COPPER = new SimpleTier(
            INCORRECT_FOR_COPPER,
            131,
            4.5f,
            1.5f,
            12,
            () -> Ingredient.of(ModItems.COPPER_DUST.get())
    );

    // Reinforced Iron tools: same durability and stats as Diamond (1561 uses, 8.0f speed, 3.0f bonus)
    public static final Tier REINFORCED_IRON = new SimpleTier(
            net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1561,
            8.0f,
            3.0f,
            10,
            () -> Ingredient.of(ModItems.DIAMOND_INGOT.get())
    );
}

