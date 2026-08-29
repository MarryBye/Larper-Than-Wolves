package io.marrybye.github.larperthanwolves.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import io.marrybye.github.larperthanwolves.item.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
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
        boolean isChest = path.startsWith("chests/");

        if (!isChest) {
            return generatedLoot;
        }

        ObjectArrayList<ItemStack> modifiedLoot = new ObjectArrayList<>();

        for (ItemStack stack : generatedLoot) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }

            if (DisabledItemsHandler.isDisabled(stack.getItem())) {
                continue;
            }

            ItemStack rebalanced = rebalanceChestItem(stack);
            if (rebalanced != null && !rebalanced.isEmpty()) {
                modifiedLoot.add(rebalanced);
            }
        }

        return modifiedLoot;
    }

    private static ItemStack rebalanceChestItem(ItemStack stack) {
        var item = stack.getItem();
        int count = stack.getCount();
        var random = ThreadLocalRandom.current();

        // 1. Tools & Weapons -> Copper (70%) or Bronze (30%)
        if (item == Items.IRON_SWORD) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_SWORD.get() : ModItems.COPPER_SWORD.get(), 1);
        }
        if (item == Items.IRON_PICKAXE) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_PICKAXE.get() : ModItems.COPPER_PICKAXE.get(), 1);
        }
        if (item == Items.IRON_AXE) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_AXE.get() : ModItems.COPPER_AXE.get(), 1);
        }
        if (item == Items.IRON_SHOVEL) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_SHOVEL.get() : ModItems.COPPER_SHOVEL.get(), 1);
        }
        if (item == Items.IRON_HOE) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_HOE.get() : ModItems.COPPER_HOE.get(), 1);
        }

        // 2. Armor -> Copper (70%) or Bronze (30%)
        if (item == Items.IRON_HELMET) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_HELMET.get() : ModItems.COPPER_HELMET.get(), 1);
        }
        if (item == Items.IRON_CHESTPLATE) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_CHESTPLATE.get() : ModItems.COPPER_CHESTPLATE.get(), 1);
        }
        if (item == Items.IRON_LEGGINGS) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_LEGGINGS.get() : ModItems.COPPER_LEGGINGS.get(), 1);
        }
        if (item == Items.IRON_BOOTS) {
            return new ItemStack(random.nextFloat() < 0.3f ? ModItems.BRONZE_BOOTS.get() : ModItems.COPPER_BOOTS.get(), 1);
        }
        if (item == Items.IRON_HORSE_ARMOR) {
            return new ItemStack(Items.LEATHER_HORSE_ARMOR, 1);
        }

        // 3. Raw materials & Ingots
        if (item == Items.IRON_INGOT) {
            float roll = random.nextFloat();
            if (roll < 0.35f) {
                return new ItemStack(ModItems.COPPER_NUGGET.get(), Math.min(count * 2, 8));
            } else if (roll < 0.65f) {
                return new ItemStack(ModItems.BRONZE_NUGGET.get(), Math.min(count, 4));
            } else if (roll < 0.85f) {
                return new ItemStack(ModItems.TIN_INGOT.get(), Math.min(count, 2));
            } else {
                return new ItemStack(ModItems.IRON_DUST.get(), Math.min(count, 2));
            }
        }
        if (item == Items.RAW_IRON) {
            return new ItemStack(ModItems.IRON_DUST.get(), Math.min(count, 2));
        }
        if (item == Items.IRON_BLOCK || item == Items.RAW_IRON_BLOCK) {
            return new ItemStack(ModItems.BRONZE_BLOCK.get(), 1);
        }
        if (item == Items.GOLD_INGOT) {
            return new ItemStack(Items.GOLD_NUGGET, Math.min(count * 2, 8));
        }
        if (item == Items.RAW_GOLD) {
            return new ItemStack(ModItems.GOLD_DUST.get(), Math.min(count, 2));
        }
        if (item == Items.DIAMOND) {
            return new ItemStack(random.nextBoolean() ? ModItems.DIAMOND_DUST.get() : ModItems.DIAMOND_NUGGET.get(), 1);
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
        if (item == Items.NETHERITE_SCRAP || item == Items.NETHERITE_INGOT) {
            return new ItemStack(ModItems.DIAMOND_DUST.get(), 1);
        }

        // 5. Early survival conversions
        if (item == Items.FLINT) {
            return new ItemStack(ModItems.SILICON_SHARD.get(), count);
        }
        if (item == Items.FURNACE) {
            return new ItemStack(ModItems.BRICK_FURNACE.get(), 1);
        }

        return stack;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
