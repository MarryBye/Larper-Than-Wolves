package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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

            // Diamond armor & horse armor
            Items.DIAMOND_HELMET,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_HORSE_ARMOR,

            // Vanilla Furnace
            Items.FURNACE
    );

    public static boolean isDisabled(Item item) {
        if (item == null) return false;
        if (DISABLED_ITEMS.contains(item)) return true;

        net.minecraft.resources.ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
        if (id != null) {
            String path = id.getPath();
            String namespace = id.getNamespace();
            if ("minecraft".equals(namespace)) {
                if (path.startsWith("wooden_") && (path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_axe") || path.endsWith("_shovel") || path.endsWith("_hoe"))) {
                    return true;
                }
                if (path.startsWith("stone_") && (path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_axe") || path.endsWith("_shovel") || path.endsWith("_hoe"))) {
                    return true;
                }
                if (path.startsWith("chainmail_")) {
                    return true;
                }
                if (path.startsWith("diamond_") && (path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_axe") || path.endsWith("_shovel") || path.endsWith("_hoe")
                        || path.endsWith("_helmet") || path.endsWith("_chestplate") || path.endsWith("_leggings") || path.endsWith("_boots") || path.endsWith("_horse_armor"))) {
                    return true;
                }
                if ("furnace".equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDisabled(ItemStack stack) {
        return stack != null && !stack.isEmpty() && isDisabled(stack.getItem());
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

    // Remove dropped or spawned item entities in the world and purge mob equipment
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof ItemEntity itemEntity) {
            if (isDisabled(itemEntity.getItem().getItem())) {
                itemEntity.discard();
                event.setCanceled(true);
                return;
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

    // Strip mob equipment before death drops are created
    @SubscribeEvent
    public static void onLivingDeath(net.neoforged.neoforge.event.entity.living.LivingDeathEvent event) {
        if (event.getEntity() instanceof LivingEntity living && !(living instanceof Player)) {
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

    // Prevent mobs dropping disabled items upon death and strip iron from iron golems
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof net.minecraft.world.entity.animal.IronGolem) {
            event.getDrops().removeIf(itemEntity -> itemEntity != null && (
                    itemEntity.getItem().is(Items.IRON_INGOT) ||
                    itemEntity.getItem().is(Items.IRON_NUGGET) ||
                    itemEntity.getItem().is(ModItems.IRON_DUST.get())
            ));
        }
        event.getDrops().removeIf(itemEntity -> itemEntity == null || itemEntity.getItem().isEmpty() || isDisabled(itemEntity.getItem().getItem()));
    }

    // Prevent equipping disabled items
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        ItemStack to = event.getTo();
        if (!to.isEmpty() && isDisabled(to.getItem())) {
            event.getEntity().setItemSlot(event.getSlot(), ItemStack.EMPTY);
            if (event.getEntity() instanceof Mob mob) {
                mob.setDropChance(event.getSlot(), 0.0f);
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
}
