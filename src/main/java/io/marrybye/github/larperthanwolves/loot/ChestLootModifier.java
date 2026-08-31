package io.marrybye.github.larperthanwolves.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import io.marrybye.github.larperthanwolves.item.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class ChestLootModifier extends LootModifier {
    public static final Supplier<MapCodec<ChestLootModifier>> CODEC =
            Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst ->
                    codecStart(inst).apply(inst, ChestLootModifier::new)
            ));

    public ChestLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (tableId == null) {
            return generatedLoot;
        }

        String path = tableId.getPath();
        // Do not modify standard block breaking loot tables (handled specifically per block)
        if (path.startsWith("blocks/")) {
            return generatedLoot;
        }

        // All chests across the world (villages, dungeons, temples, ancient cities, mineshafts, ruined portals, etc.)
        // produce ZERO loot, EXCEPT Bastion Remnant chests (where netherite upgrade templates generate)
        if (path.startsWith("chests/")) {
            if (!path.contains("bastion")) {
                return new ObjectArrayList<>();
            }
        }

        ObjectArrayList<ItemStack> modifiedLoot = new ObjectArrayList<>();

        for (ItemStack stack : generatedLoot) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }

            ItemStack rebalanced = rebalanceLootItem(stack);
            if (rebalanced != null && !rebalanced.isEmpty()) {
                if (!DisabledItemsHandler.isDisabled(rebalanced.getItem())) {
                    modifiedLoot.add(rebalanced);
                }
            }
        }

        return modifiedLoot;
    }

    private static ItemStack rebalanceLootItem(ItemStack stack) {
        Item item = stack.getItem();
        int count = stack.getCount();
        var random = ThreadLocalRandom.current();

        // 1. Golden & Iron Tools & Weapons -> Copper (75%) or Bronze (25% on high luck)
        if (item == Items.GOLDEN_SWORD || item == Items.IRON_SWORD || item == Items.DIAMOND_SWORD || item == Items.NETHERITE_SWORD) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_SWORD.get() : ModItems.COPPER_SWORD.get(), 1);
        }
        if (item == Items.GOLDEN_PICKAXE || item == Items.IRON_PICKAXE || item == Items.DIAMOND_PICKAXE || item == Items.NETHERITE_PICKAXE) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_PICKAXE.get() : ModItems.COPPER_PICKAXE.get(), 1);
        }
        if (item == Items.GOLDEN_AXE || item == Items.IRON_AXE || item == Items.DIAMOND_AXE || item == Items.NETHERITE_AXE) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_AXE.get() : ModItems.COPPER_AXE.get(), 1);
        }
        if (item == Items.GOLDEN_SHOVEL || item == Items.IRON_SHOVEL || item == Items.DIAMOND_SHOVEL || item == Items.NETHERITE_SHOVEL) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_SHOVEL.get() : ModItems.COPPER_SHOVEL.get(), 1);
        }
        if (item == Items.GOLDEN_HOE || item == Items.IRON_HOE || item == Items.DIAMOND_HOE || item == Items.NETHERITE_HOE) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_HOE.get() : ModItems.COPPER_HOE.get(), 1);
        }

        // Wooden & Stone Tools -> Silicon Tools
        if (item == Items.WOODEN_SWORD || item == Items.STONE_SWORD) {
            return copyEnchants(stack, ModItems.SILICON_SPEAR.get(), 1);
        }
        if (item == Items.WOODEN_PICKAXE || item == Items.STONE_PICKAXE) {
            return copyEnchants(stack, ModItems.SILICON_PICKAXE.get(), 1);
        }
        if (item == Items.WOODEN_AXE || item == Items.STONE_AXE) {
            return copyEnchants(stack, ModItems.SILICON_AXE.get(), 1);
        }
        if (item == Items.WOODEN_SHOVEL || item == Items.STONE_SHOVEL) {
            return copyEnchants(stack, ModItems.SILICON_SHOVEL.get(), 1);
        }
        if (item == Items.WOODEN_HOE || item == Items.STONE_HOE) {
            return copyEnchants(stack, ModItems.SILICON_HOE.get(), 1);
        }

        // 2. Armor -> Copper (75%) or Bronze (25%) / Leather
        if (item == Items.GOLDEN_HELMET || item == Items.IRON_HELMET || item == Items.CHAINMAIL_HELMET || item == Items.DIAMOND_HELMET || item == Items.NETHERITE_HELMET) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_HELMET.get() : ModItems.COPPER_HELMET.get(), 1);
        }
        if (item == Items.GOLDEN_CHESTPLATE || item == Items.IRON_CHESTPLATE || item == Items.CHAINMAIL_CHESTPLATE || item == Items.DIAMOND_CHESTPLATE || item == Items.NETHERITE_CHESTPLATE) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_CHESTPLATE.get() : ModItems.COPPER_CHESTPLATE.get(), 1);
        }
        if (item == Items.GOLDEN_LEGGINGS || item == Items.IRON_LEGGINGS || item == Items.CHAINMAIL_LEGGINGS || item == Items.DIAMOND_LEGGINGS || item == Items.NETHERITE_LEGGINGS) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_LEGGINGS.get() : ModItems.COPPER_LEGGINGS.get(), 1);
        }
        if (item == Items.GOLDEN_BOOTS || item == Items.IRON_BOOTS || item == Items.CHAINMAIL_BOOTS || item == Items.DIAMOND_BOOTS || item == Items.NETHERITE_BOOTS) {
            return copyEnchants(stack, random.nextFloat() < 0.25f ? ModItems.BRONZE_BOOTS.get() : ModItems.COPPER_BOOTS.get(), 1);
        }
        if (item == Items.GOLDEN_HORSE_ARMOR || item == Items.IRON_HORSE_ARMOR || item == Items.DIAMOND_HORSE_ARMOR) {
            return new ItemStack(Items.LEATHER_HORSE_ARMOR, 1);
        }

        // 3. Raw materials, Nuggets & Ingots
        if (item == Items.IRON_NUGGET) {
            float roll = random.nextFloat();
            if (roll < 0.65f) {
                return new ItemStack(ModItems.COPPER_NUGGET.get(), count);
            } else if (roll < 0.90f) {
                return new ItemStack(ModItems.TIN_NUGGET.get(), count);
            } else {
                return new ItemStack(ModItems.BRONZE_NUGGET.get(), Math.max(1, count / 2));
            }
        }
        if (item == Items.GOLD_NUGGET) {
            float roll = random.nextFloat();
            if (roll < 0.65f) {
                return new ItemStack(ModItems.COPPER_NUGGET.get(), Math.min(count, 4));
            } else if (roll < 0.90f) {
                return new ItemStack(ModItems.TIN_NUGGET.get(), Math.min(count, 4));
            } else {
                return new ItemStack(ModItems.GOLD_DUST.get(), 1);
            }
        }
        if (item == Items.IRON_INGOT) {
            float roll = random.nextFloat();
            if (roll < 0.50f) {
                return new ItemStack(Items.COPPER_INGOT, Math.min(count, 2));
            } else if (roll < 0.80f) {
                return new ItemStack(ModItems.TIN_INGOT.get(), Math.min(count, 2));
            } else if (roll < 0.95f) {
                return new ItemStack(ModItems.BRONZE_NUGGET.get(), Math.min(count * 2, 4));
            } else {
                return new ItemStack(ModItems.BRONZE_INGOT.get(), 1);
            }
        }
        if (item == Items.GOLD_INGOT) {
            float roll = random.nextFloat();
            if (roll < 0.50f) {
                return new ItemStack(Items.COPPER_INGOT, Math.min(count, 2));
            } else if (roll < 0.80f) {
                return new ItemStack(ModItems.TIN_INGOT.get(), Math.min(count, 2));
            } else if (roll < 0.95f) {
                return new ItemStack(ModItems.GOLD_DUST.get(), Math.min(count, 2));
            } else {
                return new ItemStack(ModItems.BRONZE_INGOT.get(), 1);
            }
        }
        if (item == Items.RAW_IRON) {
            float roll = random.nextFloat();
            if (roll < 0.50f) {
                return new ItemStack(Items.RAW_COPPER, Math.min(count, 2));
            } else if (roll < 0.85f) {
                return new ItemStack(ModItems.RAW_TIN.get(), Math.min(count, 2));
            } else {
                return new ItemStack(ModItems.IRON_DUST.get(), Math.min(count, 2));
            }
        }
        if (item == Items.RAW_GOLD) {
            float roll = random.nextFloat();
            if (roll < 0.50f) {
                return new ItemStack(Items.RAW_COPPER, Math.min(count, 2));
            } else if (roll < 0.85f) {
                return new ItemStack(ModItems.RAW_TIN.get(), Math.min(count, 2));
            } else {
                return new ItemStack(ModItems.GOLD_DUST.get(), Math.min(count, 2));
            }
        }
        if (item == Items.IRON_BLOCK || item == Items.RAW_IRON_BLOCK || item == Items.GOLD_BLOCK || item == Items.RAW_GOLD_BLOCK) {
            float roll = random.nextFloat();
            if (roll < 0.50f) {
                return new ItemStack(Blocks.COPPER_BLOCK, 1);
            } else if (roll < 0.85f) {
                return new ItemStack(ModItems.TIN_BLOCK.get(), 1);
            } else {
                return new ItemStack(ModItems.BRONZE_BLOCK.get(), 1);
            }
        }
        if (item == Items.DIAMOND) {
            return new ItemStack(random.nextFloat() < 0.75f ? ModItems.DIAMOND_DUST.get() : ModItems.DIAMOND_NUGGET.get(), 1);
        }
        if (item == Items.DIAMOND_BLOCK) {
            return new ItemStack(ModItems.DIAMOND_NUGGET.get(), 2);
        }

        // 4. Overpowered / Supernatural items
        if (item == Items.ENCHANTED_GOLDEN_APPLE) {
            return new ItemStack(Items.GOLDEN_APPLE, 1);
        }
        if (item == Items.OBSIDIAN || item == Items.CRYING_OBSIDIAN) {
            return new ItemStack(ModItems.DEEPSLATE_NUGGET.get(), Math.min(count, 4));
        }
        if (item == Items.ANVIL || item == Items.CHIPPED_ANVIL || item == Items.DAMAGED_ANVIL) {
            return new ItemStack(ModItems.STONE_NUGGET.get(), 8);
        }
        if (item == Items.NETHERITE_SCRAP || item == Items.NETHERITE_INGOT || item == Items.NETHERITE_BLOCK || item == Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
            return new ItemStack(ModItems.DIAMOND_DUST.get(), 1);
        }

        // 5. Early survival conversions & Crafting Station Replacements
        if (item == Items.FLINT) {
            return new ItemStack(ModItems.SILICON_SHARD.get(), count);
        }
        if (item == Items.BONE_MEAL) {
            return new ItemStack(Items.BONE, count);
        }
        if (item == Items.FURNACE || item == Items.BLAST_FURNACE) {
            return new ItemStack(ModItems.BRICK_FURNACE.get(), 1);
        }
        if (item == Items.SMOKER) {
            return new ItemStack(ModItems.OVEN.get(), 1);
        }

        return stack;
    }

    private static ItemStack copyEnchants(ItemStack source, Item newItem, int count) {
        ItemStack result = new ItemStack(newItem, count);
        if (source.has(DataComponents.ENCHANTMENTS)) {
            result.set(DataComponents.ENCHANTMENTS, source.get(DataComponents.ENCHANTMENTS));
        }
        return result;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
