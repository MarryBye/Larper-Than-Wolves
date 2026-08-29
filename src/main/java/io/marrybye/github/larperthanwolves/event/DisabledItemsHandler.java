package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = LarperThanWolves.MODID)
public class DisabledItemsHandler {

    public static final Set<Item> DISABLED_ITEMS = Set.of(
            // Wooden tools
            Items.WOODEN_SWORD,
            Items.WOODEN_PICKAXE,
            Items.WOODEN_AXE,
            Items.WOODEN_SHOVEL,
            Items.WOODEN_HOE,

            // Stone tools
            Items.STONE_SWORD,
            Items.STONE_PICKAXE,
            Items.STONE_AXE,
            Items.STONE_SHOVEL,
            Items.STONE_HOE,

            // Chainmail armor
            Items.CHAINMAIL_HELMET,
            Items.CHAINMAIL_CHESTPLATE,
            Items.CHAINMAIL_LEGGINGS,
            Items.CHAINMAIL_BOOTS,

            // Diamond tools
            Items.DIAMOND_SWORD,
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_AXE,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_HOE,

            // Diamond armor
            Items.DIAMOND_HELMET,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_BOOTS,

            // Vanilla Furnace
            Items.FURNACE
    );

    public static boolean isDisabled(Item item) {
        return DISABLED_ITEMS.contains(item);
    }

    // Remove disabled items from all Creative Tabs (called via mod event bus listener)
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        try {
            event.getParentEntries().removeIf(stack -> !stack.isEmpty() && isDisabled(stack.getItem()));
        } catch (Throwable ignored) {}
        try {
            event.getSearchEntries().removeIf(stack -> !stack.isEmpty() && isDisabled(stack.getItem()));
        } catch (Throwable ignored) {}
    }

    // Remove dropped or spawned item entities in the world
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof ItemEntity itemEntity) {
            if (isDisabled(itemEntity.getItem().getItem())) {
                itemEntity.discard();
            }
        } else if (event.getEntity() instanceof LivingEntity living && !(living instanceof Player)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = living.getItemBySlot(slot);
                if (!stack.isEmpty() && isDisabled(stack.getItem())) {
                    living.setItemSlot(slot, ItemStack.EMPTY);
                    if (living instanceof Mob mob) {
                        mob.setDropChance(slot, 0.0f);
                    }
                }
            }
        }
    }

    // Prevent mobs from spawning with disabled equipment
    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = mob.getItemBySlot(slot);
            if (!stack.isEmpty() && isDisabled(stack.getItem())) {
                mob.setItemSlot(slot, ItemStack.EMPTY);
                mob.setDropChance(slot, 0.0f);
            }
        }
    }

    // Prevent mobs dropping disabled items upon death
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        event.getDrops().removeIf(itemEntity -> !itemEntity.getItem().isEmpty() && isDisabled(itemEntity.getItem().getItem()));
    }

    // Prevent equipping disabled items
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        ItemStack to = event.getTo();
        if (!to.isEmpty() && isDisabled(to.getItem())) {
            event.getEntity().setItemSlot(event.getSlot(), ItemStack.EMPTY);
            if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
                player.displayClientMessage(Component.literal("§cЭтот предмет вырезан модификацией Larper Than Wolves!"), true);
            }
        }
    }

    // Prevent using disabled items
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && isDisabled(stack.getItem())) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (player != null && !event.getLevel().isClientSide) {
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                player.displayClientMessage(Component.literal("§cЭтот предмет вырезан модификацией Larper Than Wolves!"), true);
            }
        }
    }

    // Prevent placing or using disabled items on blocks
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && isDisabled(stack.getItem())) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (player != null && !event.getLevel().isClientSide) {
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                player.displayClientMessage(Component.literal("§cЭтот предмет вырезан модификацией Larper Than Wolves!"), true);
            }
        }
    }

    // Prevent attacking / breaking blocks with disabled items
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && isDisabled(stack.getItem())) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (player != null && !event.getLevel().isClientSide) {
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
                player.displayClientMessage(Component.literal("§cЭтот предмет вырезан модификацией Larper Than Wolves!"), true);
            }
        }
    }

    // Scan inventory periodically to purge any disabled items from players
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && isDisabled(stack.getItem())) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

    // Filter villager trades
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        for (int tier : event.getTrades().keySet()) {
            List<VillagerTrades.ItemListing> listings = event.getTrades().get(tier);
            if (listings != null) {
                List<VillagerTrades.ItemListing> wrapped = new ArrayList<>(listings.size());
                for (VillagerTrades.ItemListing listing : listings) {
                    wrapped.add(filterListing(listing));
                }
                event.getTrades().put(tier, wrapped);
            }
        }
    }

    // Filter wandering trader trades
    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> generic = event.getGenericTrades();
        for (int i = 0; i < generic.size(); i++) {
            generic.set(i, filterListing(generic.get(i)));
        }
        List<VillagerTrades.ItemListing> rare = event.getRareTrades();
        for (int i = 0; i < rare.size(); i++) {
            rare.set(i, filterListing(rare.get(i)));
        }
    }

    private static VillagerTrades.ItemListing filterListing(VillagerTrades.ItemListing original) {
        return (entity, random) -> {
            try {
                MerchantOffer offer = original.getOffer(entity, random);
                if (offer == null) return null;
                if (isDisabled(offer.getResult().getItem())) return null;
                if (offer.getItemCostA() != null && isDisabled(offer.getItemCostA().item().value())) return null;
                if (offer.getItemCostB().isPresent() && isDisabled(offer.getItemCostB().get().item().value())) return null;
                return offer;
            } catch (Exception e) {
                return null;
            }
        };
    }
}
