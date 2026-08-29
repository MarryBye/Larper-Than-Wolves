package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = "larperthanwolves")
public class VillagerTradeHandler {

    public static VillagerTrades.ItemListing buy(ItemLike item, int cost, int maxUses, int xp) {
        return (entity, random) -> new MerchantOffer(new ItemCost(item.asItem(), cost), new ItemStack(Items.EMERALD, 1), maxUses, xp, 0.05f);
    }

    public static VillagerTrades.ItemListing sell(ItemLike item, int emeraldCost, int count, int maxUses, int xp) {
        return (entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), new ItemStack(item.asItem(), count), maxUses, xp, 0.05f);
    }

    public static VillagerTrades.ItemListing sell(ItemStack stack, int emeraldCost, int maxUses, int xp) {
        return (entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), stack.copy(), maxUses, xp, 0.05f);
    }

    public static VillagerTrades.ItemListing trade(ItemLike costItem, int count, ItemLike resultItem, int resultCount, int maxUses, int xp) {
        return (entity, random) -> new MerchantOffer(new ItemCost(costItem.asItem(), count), new ItemStack(resultItem.asItem(), resultCount), maxUses, xp, 0.05f);
    }

    public static VillagerTrades.ItemListing trade(ItemLike costA, int countA, ItemLike costB, int countB, ItemStack result, int maxUses, int xp) {
        return (entity, random) -> new MerchantOffer(new ItemCost(costA.asItem(), countA), Optional.of(new ItemCost(costB.asItem(), countB)), result.copy(), maxUses, xp, 0.05f);
    }

    public static boolean isOverpoweredOrDisabled(Item item) {
        if (item == null) return false;
        if (DisabledItemsHandler.isDisabled(item)) return true;

        // Strip all iron gear from trades
        if (item == Items.IRON_SWORD || item == Items.IRON_AXE || item == Items.IRON_PICKAXE ||
                item == Items.IRON_SHOVEL || item == Items.IRON_HOE ||
                item == Items.IRON_HELMET || item == Items.IRON_CHESTPLATE ||
                item == Items.IRON_LEGGINGS || item == Items.IRON_BOOTS ||
                item == Items.IRON_HORSE_ARMOR) {
            return true;
        }

        // Diamond gear already in DisabledItemsHandler, but double check
        if (item == Items.DIAMOND_SWORD || item == Items.DIAMOND_AXE || item == Items.DIAMOND_PICKAXE ||
                item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE ||
                item == Items.DIAMOND_HELMET || item == Items.DIAMOND_CHESTPLATE ||
                item == Items.DIAMOND_LEGGINGS || item == Items.DIAMOND_BOOTS) {
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        VillagerProfession profession = event.getType();

        // If Armorer, Weaponsmith, or Toolsmith: completely replace with balanced tiers up to Bronze!
        if (profession == VillagerProfession.ARMORER) {
            setupArmorerTrades(event);
            return;
        }
        if (profession == VillagerProfession.WEAPONSMITH) {
            setupWeaponsmithTrades(event);
            return;
        }
        if (profession == VillagerProfession.TOOLSMITH) {
            setupToolsmithTrades(event);
            return;
        }

        // For other professions: sanitize existing trades to ensure no disabled or overpowered items
        for (int tier : event.getTrades().keySet()) {
            List<VillagerTrades.ItemListing> listings = event.getTrades().get(tier);
            if (listings != null) {
                List<VillagerTrades.ItemListing> sanitized = new ArrayList<>(listings.size());
                for (VillagerTrades.ItemListing listing : listings) {
                    sanitized.add((entity, random) -> {
                        try {
                            MerchantOffer offer = listing.getOffer(entity, random);
                            if (offer == null) return null;
                            if (isOverpoweredOrDisabled(offer.getResult().getItem())) return null;
                            if (offer.getItemCostA() != null && isOverpoweredOrDisabled(offer.getItemCostA().item().value())) return null;
                            if (offer.getItemCostB().isPresent() && isOverpoweredOrDisabled(offer.getItemCostB().get().item().value())) return null;

                            // If an offer asks for Iron Ingot, replace with Copper Ingot
                            if (offer.getItemCostA() != null && offer.getItemCostA().item().value() == Items.IRON_INGOT) {
                                return new MerchantOffer(new ItemCost(Items.COPPER_INGOT, offer.getItemCostA().count()), offer.getItemCostB(), offer.getResult(), offer.getMaxUses(), offer.getXp(), offer.getPriceMultiplier());
                            }

                            return offer;
                        } catch (Exception e) {
                            return null;
                        }
                    });
                }
                event.getTrades().put(tier, sanitized);
            }
        }
    }

    private static void setupArmorerTrades(VillagerTradesEvent event) {
        event.getTrades().clear();

        // Level 1 (Novice)
        event.getTrades().put(1, List.of(
                buy(Items.COAL, 15, 16, 2),
                buy(Items.COPPER_INGOT, 5, 16, 2),
                sell(ModItems.COPPER_HELMET.get(), 4, 1, 12, 1),
                sell(ModItems.COPPER_BOOTS.get(), 3, 1, 12, 1)
        ));

        // Level 2 (Apprentice)
        event.getTrades().put(2, List.of(
                buy(ModItems.TIN_INGOT.get(), 4, 12, 10),
                sell(ModItems.COPPER_LEGGINGS.get(), 6, 1, 12, 5),
                sell(ModItems.COPPER_CHESTPLATE.get(), 8, 1, 12, 5),
                sell(Items.BELL, 36, 1, 12, 5)
        ));

        // Level 3 (Journeyman)
        event.getTrades().put(3, List.of(
                buy(ModItems.BRONZE_INGOT.get(), 3, 12, 20),
                sell(Items.SHIELD, 5, 1, 12, 10),
                sell(ModItems.BRONZE_BOOTS.get(), 5, 1, 12, 10),
                sell(ModItems.BRONZE_HELMET.get(), 6, 1, 12, 10)
        ));

        // Level 4 (Expert)
        event.getTrades().put(4, List.of(
                buy(Items.LAVA_BUCKET, 1, 12, 30),
                sell(ModItems.BRONZE_LEGGINGS.get(), 8, 1, 12, 15)
        ));

        // Level 5 (Master)
        event.getTrades().put(5, List.of(
                sell(ModItems.BRONZE_CHESTPLATE.get(), 11, 1, 12, 30)
        ));
    }

    private static void setupWeaponsmithTrades(VillagerTradesEvent event) {
        event.getTrades().clear();

        // Level 1 (Novice)
        event.getTrades().put(1, List.of(
                buy(Items.COAL, 15, 16, 2),
                buy(Items.COPPER_INGOT, 5, 16, 2),
                sell(ModItems.SILICON_SPEAR.get(), 1, 1, 12, 1),
                sell(ModItems.COPPER_SWORD.get(), 3, 1, 12, 1)
        ));

        // Level 2 (Apprentice)
        event.getTrades().put(2, List.of(
                buy(ModItems.TIN_INGOT.get(), 4, 12, 10),
                sell(ModItems.COPPER_AXE.get(), 4, 1, 12, 5),
                sell(Items.BELL, 36, 1, 12, 5)
        ));

        // Level 3 (Journeyman)
        event.getTrades().put(3, List.of(
                buy(ModItems.SILICON_SHARD.get(), 12, 16, 20),
                buy(Items.FLINT, 16, 16, 20),
                sell(ModItems.BRONZE_SWORD.get(), 6, 1, 12, 10)
        ));

        // Level 4 (Expert)
        event.getTrades().put(4, List.of(
                buy(ModItems.BRONZE_INGOT.get(), 3, 12, 30),
                sell(ModItems.BRONZE_AXE.get(), 8, 1, 12, 15)
        ));

        // Level 5 (Master)
        event.getTrades().put(5, List.of(
                sell(ModItems.BRONZE_SWORD.get(), 10, 1, 12, 30)
        ));
    }

    private static void setupToolsmithTrades(VillagerTradesEvent event) {
        event.getTrades().clear();

        // Level 1 (Novice)
        event.getTrades().put(1, List.of(
                buy(Items.COAL, 15, 16, 2),
                buy(ModItems.SILICON_SHARD.get(), 12, 16, 2),
                sell(ModItems.SILICON_SHOVEL.get(), 1, 1, 12, 1),
                sell(ModItems.SILICON_PICKAXE.get(), 1, 1, 12, 1)
        ));

        // Level 2 (Apprentice)
        event.getTrades().put(2, List.of(
                buy(Items.COPPER_INGOT, 5, 12, 10),
                sell(ModItems.COPPER_SHOVEL.get(), 2, 1, 12, 5),
                sell(ModItems.COPPER_HOE.get(), 2, 1, 12, 5),
                sell(ModItems.COPPER_PICKAXE.get(), 4, 1, 12, 5)
        ));

        // Level 3 (Journeyman)
        event.getTrades().put(3, List.of(
                buy(ModItems.TIN_INGOT.get(), 4, 12, 20),
                sell(ModItems.COPPER_AXE.get(), 4, 1, 12, 10),
                sell(ModItems.BRONZE_SHOVEL.get(), 3, 1, 12, 10)
        ));

        // Level 4 (Expert)
        event.getTrades().put(4, List.of(
                buy(ModItems.BRONZE_INGOT.get(), 3, 12, 30),
                sell(ModItems.BRONZE_PICKAXE.get(), 7, 1, 12, 15),
                sell(ModItems.BRONZE_AXE.get(), 7, 1, 12, 15)
        ));

        // Level 5 (Master)
        event.getTrades().put(5, List.of(
                sell(ModItems.BRONZE_HOE.get(), 5, 1, 12, 30)
        ));
    }

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        // Clear and add balanced, survival-friendly wandering trader trades
        List<VillagerTrades.ItemListing> generic = event.getGenericTrades();
        generic.clear();

        // Saplings for emeralds or sticks
        generic.add(sell(Items.OAK_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.SPRUCE_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.BIRCH_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.JUNGLE_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.ACACIA_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.DARK_OAK_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.CHERRY_SAPLING, 1, 1, 8, 1));
        generic.add(sell(Items.MANGROVE_PROPAGULE, 1, 1, 8, 1));

        // Crops & Seeds for emeralds or sticks
        generic.add(sell(Items.WHEAT_SEEDS, 1, 2, 8, 1));
        generic.add(sell(Items.PUMPKIN_SEEDS, 1, 1, 8, 1));
        generic.add(sell(Items.MELON_SEEDS, 1, 1, 8, 1));
        generic.add(sell(Items.BEETROOT_SEEDS, 1, 2, 8, 1));
        generic.add(sell(Items.CARROT, 1, 1, 8, 1));
        generic.add(sell(Items.POTATO, 1, 1, 8, 1));

        // Wild survival supplies (Dry Grass, Silicon Shard, Rope, Pebbles)
        generic.add(sell(ModItems.DRY_GRASS.get(), 1, 4, 12, 1));
        generic.add(sell(ModItems.ROPE.get(), 1, 2, 12, 1));
        generic.add(sell(ModItems.SILICON_SHARD.get(), 1, 3, 12, 1));
        generic.add(sell(ModItems.STONE_NUGGET.get(), 1, 8, 12, 1));

        // Flavor & Dyes
        generic.add(sell(Items.DANDELION, 1, 1, 12, 1));
        generic.add(sell(Items.POPPY, 1, 1, 12, 1));
        generic.add(sell(Items.FERN, 1, 1, 12, 1));
        generic.add(sell(Items.MOSS_BLOCK, 1, 2, 8, 1));
        generic.add(sell(Items.DRIPSTONE_BLOCK, 1, 2, 8, 1));
        generic.add(sell(Items.POINTED_DRIPSTONE, 1, 2, 8, 1));
        generic.add(sell(Items.CLAY_BALL, 1, 4, 12, 1));
        generic.add(sell(Items.GUNPOWDER, 1, 1, 8, 1));
        generic.add(sell(Items.SLIME_BALL, 1, 1, 5, 1));

        // Simple barter: trade sticks / pebbles for basic things
        generic.add(trade(Items.STICK, 16, Items.EMERALD, 1, 12, 1));
        generic.add(trade(ModItems.STONE_NUGGET.get(), 16, Items.EMERALD, 1, 12, 1));
        generic.add(trade(ModItems.SILICON_SHARD.get(), 8, Items.EMERALD, 1, 12, 1));

        // Rare trades
        List<VillagerTrades.ItemListing> rare = event.getRareTrades();
        rare.clear();
        rare.add(sell(Items.NAUTILUS_SHELL, 5, 1, 5, 1));
        rare.add(sell(Items.PUFFERFISH_BUCKET, 3, 1, 4, 1));
        rare.add(sell(Items.TROPICAL_FISH_BUCKET, 3, 1, 4, 1));
        rare.add(sell(Items.BLUE_ICE, 4, 1, 6, 1));
        rare.add(sell(Items.PACKED_ICE, 2, 1, 6, 1));
        rare.add(sell(Items.GLOWSTONE, 2, 1, 5, 1));
        rare.add(sell(ModItems.TANNED_LEATHER.get(), 2, 1, 6, 1));
    }
}
