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

            // Golden tools
            Items.GOLDEN_SWORD,
            Items.GOLDEN_PICKAXE,
            Items.GOLDEN_AXE,
            Items.GOLDEN_SHOVEL,
            Items.GOLDEN_HOE,

            // Vanilla Furnace, Blast Furnace, Smoker
            Items.FURNACE,
            Items.BLAST_FURNACE,
            Items.SMOKER
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
                if (path.startsWith("golden_") && (path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_axe") || path.endsWith("_shovel") || path.endsWith("_hoe"))) {
                    return true;
                }
                if (path.startsWith("chainmail_")) {
                    return true;
                }
                if (path.startsWith("diamond_") && (path.endsWith("_sword") || path.endsWith("_pickaxe") || path.endsWith("_axe") || path.endsWith("_shovel") || path.endsWith("_hoe")
                        || path.endsWith("_helmet") || path.endsWith("_chestplate") || path.endsWith("_leggings") || path.endsWith("_boots") || path.endsWith("_horse_armor"))) {
                    return true;
                }
                if ("furnace".equals(path) || "blast_furnace".equals(path) || "smoker".equals(path)) {
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

        // Realistic bone drops from peaceful animals, zombies and their subspecies
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide) {
            java.util.Random rand = new java.util.Random();

            if (entity instanceof net.minecraft.world.entity.animal.Animal) {
                boolean isLarge = (entity instanceof net.minecraft.world.entity.animal.Cow ||
                        entity instanceof net.minecraft.world.entity.animal.Pig ||
                        entity instanceof net.minecraft.world.entity.animal.Sheep ||
                        entity instanceof net.minecraft.world.entity.animal.horse.AbstractHorse ||
                        entity instanceof net.minecraft.world.entity.animal.goat.Goat ||
                        entity instanceof net.minecraft.world.entity.animal.camel.Camel ||
                        entity instanceof net.minecraft.world.entity.animal.sniffer.Sniffer ||
                        entity instanceof net.minecraft.world.entity.animal.PolarBear ||
                        entity instanceof net.minecraft.world.entity.animal.Panda ||
                        entity instanceof net.minecraft.world.entity.monster.hoglin.Hoglin);

                float chance = isLarge ? 0.30f : 0.15f;
                if (rand.nextFloat() < chance) {
                    int count = (isLarge && rand.nextFloat() < 0.25f) ? 2 : 1;
                    ItemEntity boneDrop = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.BONE, count));
                    boneDrop.setDefaultPickUpDelay();
                    event.getDrops().add(boneDrop);
                }
            } else if (entity instanceof net.minecraft.world.entity.monster.Zombie || entity instanceof net.minecraft.world.entity.monster.Zoglin) {
                float chance = 0.25f;
                if (rand.nextFloat() < chance) {
                    ItemEntity boneDrop = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.BONE, 1));
                    boneDrop.setDefaultPickUpDelay();
                    event.getDrops().add(boneDrop);
                }
            }
        }
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

    // Prevent placing or using disabled items on blocks, and replace vanilla blast furnace / smoker on click
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && isDisabled(stack.getItem())) {
            event.setCanceled(true);
            Player player = event.getEntity();
            if (player != null && !event.getLevel().isClientSide) {
                player.setItemInHand(event.getHand(), ItemStack.EMPTY);
            }
            return;
        }

        net.minecraft.world.level.Level level = event.getLevel();
        net.minecraft.core.BlockPos pos = event.getPos();
        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);

        if (!level.isClientSide) {
            if (state.is(net.minecraft.world.level.block.Blocks.BLAST_FURNACE)) {
                net.minecraft.core.Direction facing = state.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                        state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get().defaultBlockState()
                        .setValue(io.marrybye.github.larperthanwolves.block.BrickFurnaceBlock.FACING, facing), 3);
                event.setCanceled(true);
                return;
            } else if (state.is(net.minecraft.world.level.block.Blocks.SMOKER)) {
                net.minecraft.core.Direction facing = state.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                        state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.OVEN.get().defaultBlockState()
                        .setValue(io.marrybye.github.larperthanwolves.block.OvenBlock.FACING, facing), 3);
                event.setCanceled(true);
                return;
            } else if (state.is(net.minecraft.world.level.block.Blocks.FURNACE)) {
                net.minecraft.core.Direction facing = state.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                        state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get().defaultBlockState()
                        .setValue(io.marrybye.github.larperthanwolves.block.BrickFurnaceBlock.FACING, facing), 3);
                event.setCanceled(true);
                return;
            }
        }
    }

    // Replace blast furnaces, smokers, furnaces, and purge worldgen crafting tables & non-bastion chests
    @SubscribeEvent
    public static void onChunkLoad(net.neoforged.neoforge.event.level.ChunkEvent.Load event) {
        if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel level) {
            net.minecraft.world.level.chunk.ChunkAccess chunk = event.getChunk();

            // 1. Process block entities in chunk (Furnaces, Smokers, Blast Furnaces, Non-Bastion Chests)
            for (net.minecraft.core.BlockPos pos : chunk.getBlockEntitiesPos()) {
                net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
                if (state.is(net.minecraft.world.level.block.Blocks.BLAST_FURNACE)) {
                    net.minecraft.core.Direction facing = state.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                            state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                    level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get().defaultBlockState()
                            .setValue(io.marrybye.github.larperthanwolves.block.BrickFurnaceBlock.FACING, facing), 3);
                } else if (state.is(net.minecraft.world.level.block.Blocks.SMOKER)) {
                    net.minecraft.core.Direction facing = state.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                            state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                    level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.OVEN.get().defaultBlockState()
                            .setValue(io.marrybye.github.larperthanwolves.block.OvenBlock.FACING, facing), 3);
                } else if (state.is(net.minecraft.world.level.block.Blocks.FURNACE)) {
                    net.minecraft.core.Direction facing = state.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                            state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                    level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get().defaultBlockState()
                            .setValue(io.marrybye.github.larperthanwolves.block.BrickFurnaceBlock.FACING, facing), 3);
                } else if (state.getBlock() instanceof net.minecraft.world.level.block.ChestBlock
                        || state.getBlock() instanceof net.minecraft.world.level.block.TrappedChestBlock
                        || state.getBlock() instanceof net.minecraft.world.level.block.BarrelBlock) {
                    net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity container) {
                        net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTable = container.getLootTable();
                        if (lootTable != null) {
                            String path = lootTable.location().getPath();
                            if (!path.contains("bastion")) {
                                level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }

            // 2. Scan chunk sections to remove worldgen Crafting Tables & Furnaces
            net.minecraft.world.level.chunk.LevelChunkSection[] sections = chunk.getSections();
            for (int secIdx = 0; secIdx < sections.length; secIdx++) {
                net.minecraft.world.level.chunk.LevelChunkSection section = sections[secIdx];
                if (section == null || section.hasOnlyAir()) continue;

                if (section.maybeHas(s -> s.is(net.minecraft.world.level.block.Blocks.CRAFTING_TABLE)
                        || s.is(net.minecraft.world.level.block.Blocks.FURNACE)
                        || s.is(net.minecraft.world.level.block.Blocks.BLAST_FURNACE)
                        || s.is(net.minecraft.world.level.block.Blocks.SMOKER))) {
                    int secY = chunk.getSectionYFromSectionIndex(secIdx) << 4;
                    int startX = chunk.getPos().getMinBlockX();
                    int startZ = chunk.getPos().getMinBlockZ();

                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(startX + x, secY + y, startZ + z);
                                net.minecraft.world.level.block.state.BlockState bs = level.getBlockState(pos);
                                if (bs.is(net.minecraft.world.level.block.Blocks.CRAFTING_TABLE)) {
                                    level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.OAK_STUMP.get().defaultBlockState(), 3);
                                } else if (bs.is(net.minecraft.world.level.block.Blocks.BLAST_FURNACE) || bs.is(net.minecraft.world.level.block.Blocks.FURNACE)) {
                                    net.minecraft.core.Direction facing = bs.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                                            bs.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                                    level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get().defaultBlockState()
                                            .setValue(io.marrybye.github.larperthanwolves.block.BrickFurnaceBlock.FACING, facing), 3);
                                } else if (bs.is(net.minecraft.world.level.block.Blocks.SMOKER)) {
                                    net.minecraft.core.Direction facing = bs.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) ?
                                            bs.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING) : net.minecraft.core.Direction.NORTH;
                                    level.setBlock(pos, io.marrybye.github.larperthanwolves.block.ModBlocks.OVEN.get().defaultBlockState()
                                            .setValue(io.marrybye.github.larperthanwolves.block.OvenBlock.FACING, facing), 3);
                                }
                            }
                        }
                    }
                }
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
