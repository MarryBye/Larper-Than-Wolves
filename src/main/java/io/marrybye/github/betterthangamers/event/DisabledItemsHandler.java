package io.marrybye.github.betterthangamers.event;

import io.marrybye.github.betterthangamers.block.ModBlocks;
import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = "betterthangamers")
public class DisabledItemsHandler {

    private static final Map<Item, Item> REPLACEMENTS = new HashMap<>();
    private static final Set<Item> DISABLED_ITEMS;

    static {
        REPLACEMENTS.put(Items.WOODEN_PICKAXE, ModItems.SILICON_PICKAXE.get());
        REPLACEMENTS.put(Items.WOODEN_AXE, ModItems.SILICON_AXE.get());
        REPLACEMENTS.put(Items.WOODEN_SHOVEL, ModItems.SILICON_SHOVEL.get());
        REPLACEMENTS.put(Items.WOODEN_SWORD, ModItems.SILICON_SPEAR.get());
        REPLACEMENTS.put(Items.WOODEN_HOE, ModItems.SILICON_SHARD.get());

        REPLACEMENTS.put(Items.STONE_PICKAXE, ModItems.SILICON_PICKAXE.get());
        REPLACEMENTS.put(Items.STONE_AXE, ModItems.SILICON_AXE.get());
        REPLACEMENTS.put(Items.STONE_SHOVEL, ModItems.SILICON_SHOVEL.get());
        REPLACEMENTS.put(Items.STONE_SWORD, ModItems.SILICON_SPEAR.get());
        REPLACEMENTS.put(Items.STONE_HOE, ModItems.SILICON_SHARD.get());

        REPLACEMENTS.put(Items.CHAINMAIL_HELMET, ModItems.COPPER_HELMET.get());
        REPLACEMENTS.put(Items.CHAINMAIL_CHESTPLATE, ModItems.COPPER_CHESTPLATE.get());
        REPLACEMENTS.put(Items.CHAINMAIL_LEGGINGS, ModItems.COPPER_LEGGINGS.get());
        REPLACEMENTS.put(Items.CHAINMAIL_BOOTS, ModItems.COPPER_BOOTS.get());

        REPLACEMENTS.put(Items.FURNACE, ModBlocks.BRICK_FURNACE.asItem());

        DISABLED_ITEMS = Set.copyOf(REPLACEMENTS.keySet());
    }

    public static boolean isDisabled(Item item) {
        return DISABLED_ITEMS.contains(item);
    }

    public static ItemStack getReplacement(ItemStack original) {
        Item replacementItem = REPLACEMENTS.get(original.getItem());
        if (replacementItem != null) {
            return new ItemStack(replacementItem, original.getCount());
        }
        return original;
    }

    // Replace dropped or spawned items in the world (e.g. from loot chests, mobs, breaks)
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            if (isDisabled(stack.getItem())) {
                ItemStack replacement = getReplacement(stack);
                itemEntity.setItem(replacement);
            }
        }
    }

    // Prevent using disabled items
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (isDisabled(stack.getItem())) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (!event.getLevel().isClientSide && player != null) {
                player.displayClientMessage(Component.literal("§cЭтот предмет вырезан модификацией BetterThanGamers!"), true);
            }
        }
    }

    // Prevent placing vanilla furnace
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (isDisabled(stack.getItem())) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (!event.getLevel().isClientSide && player != null) {
                player.displayClientMessage(Component.literal("§cЭтот предмет вырезан модификацией BetterThanGamers!"), true);
            }
        }
    }

    // Prevent equipping disabled armor
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            ItemStack to = event.getTo();
            if (isDisabled(to.getItem())) {
                EquipmentSlot slot = event.getSlot();
                ItemStack replacement = getReplacement(to);
                player.setItemSlot(slot, replacement);
                player.displayClientMessage(Component.literal("§eКольчужная броня заменена на медную!"), true);
            }
        }
    }
}

