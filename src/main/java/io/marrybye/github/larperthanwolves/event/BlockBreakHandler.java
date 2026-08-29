package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.UnfiredBrickBlock;
import io.marrybye.github.larperthanwolves.block.WorkStumpBlock;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = "larperthanwolves")
public class BlockBreakHandler {

    private static final Random RANDOM = new Random();

    private static boolean isSiliconPickaxe(ItemStack stack) {
        return stack.is(ModItems.SILICON_PICKAXE.get());
    }

    private static boolean isCopperPickaxe(ItemStack stack) {
        return stack.is(ModItems.COPPER_PICKAXE.get());
    }

    private static boolean isIronOrBetterPickaxe(ItemStack stack) {
        return stack.is(Items.IRON_PICKAXE) ||
                stack.is(ModItems.REINFORCED_IRON_PICKAXE.get()) ||
                stack.is(Items.NETHERITE_PICKAXE);
    }

    private static boolean isDeepslateOrTuffOrHighTier(Block block) {
        return block == Blocks.DEEPSLATE ||
                block == Blocks.COBBLED_DEEPSLATE ||
                block == Blocks.POLISHED_DEEPSLATE ||
                block == Blocks.DEEPSLATE_BRICKS ||
                block == Blocks.CRACKED_DEEPSLATE_BRICKS ||
                block == Blocks.DEEPSLATE_TILES ||
                block == Blocks.CRACKED_DEEPSLATE_TILES ||
                block == Blocks.CHISELED_DEEPSLATE ||
                block == Blocks.REINFORCED_DEEPSLATE ||
                block == Blocks.DEEPSLATE_COAL_ORE ||
                block == Blocks.DEEPSLATE_IRON_ORE ||
                block == Blocks.DEEPSLATE_COPPER_ORE ||
                block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.DEEPSLATE_REDSTONE_ORE ||
                block == Blocks.DEEPSLATE_LAPIS_ORE ||
                block == Blocks.DEEPSLATE_DIAMOND_ORE ||
                block == Blocks.DEEPSLATE_EMERALD_ORE ||
                block == Blocks.TUFF ||
                block == Blocks.TUFF_STAIRS ||
                block == Blocks.TUFF_SLAB ||
                block == Blocks.TUFF_WALL ||
                block == Blocks.CHISELED_TUFF ||
                block == Blocks.POLISHED_TUFF ||
                block == Blocks.TUFF_BRICKS ||
                block == Blocks.IRON_ORE ||
                block == Blocks.GOLD_ORE ||
                block == Blocks.REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE ||
                block == Blocks.DIAMOND_ORE ||
                block == Blocks.EMERALD_ORE ||
                block == Blocks.OBSIDIAN ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.ANCIENT_DEBRIS;
    }

    private static boolean isDiamondPlusTier(Block block) {
        return block == Blocks.DIAMOND_ORE ||
                block == Blocks.DEEPSLATE_DIAMOND_ORE ||
                block == Blocks.EMERALD_ORE ||
                block == Blocks.DEEPSLATE_EMERALD_ORE ||
                block == Blocks.GOLD_ORE ||
                block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.REDSTONE_ORE ||
                block == Blocks.DEEPSLATE_REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE ||
                block == Blocks.DEEPSLATE_LAPIS_ORE ||
                block == Blocks.OBSIDIAN ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.RESPAWN_ANCHOR ||
                block == Blocks.ANCIENT_DEBRIS;
    }

    // 1. Mining speed checks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack held = player.getMainHandItem();
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (isSiliconPickaxe(held) && isDeepslateOrTuffOrHighTier(block)) {
            event.setNewSpeed(0.0f);
            event.setCanceled(true);
        } else if (isCopperPickaxe(held) && isDiamondPlusTier(block)) {
            event.setNewSpeed(0.0f);
            event.setCanceled(true);
        }
    }

    // 2. Block break event: Hoeing grass & Work Stump carving when breaking
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide) return;

        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPos();
        Level level = player.level();
        ItemStack held = player.getMainHandItem();

        // Prevent silicon pickaxe breaking deepslate/tuff
        if (isSiliconPickaxe(held) && isDeepslateOrTuffOrHighTier(block)) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("§cКремниевая кирка слишком слаба для этого блока!"), true);
            return;
        }

        // Hoeing grass block with hoe
        if (block == Blocks.GRASS_BLOCK && held.getItem() instanceof HoeItem) {
            event.setCanceled(true);
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
            level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            held.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

            if (RANDOM.nextFloat() < 0.35f) {
                ItemEntity seedDrop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Items.WHEAT_SEEDS));
                seedDrop.setDefaultPickUpDelay();
                level.addFreshEntity(seedDrop);
            }
        }
    }

    // 3. Custom drops handling
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        ServerLevel level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Block block = state.getBlock();
        ItemStack tool = event.getTool();
        List<ItemEntity> drops = event.getDrops();

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        // --- Unfired Brick Block ---
        if (block instanceof UnfiredBrickBlock) {
            drops.clear();
            if (state.getValue(UnfiredBrickBlock.STAGE) == 3) {
                ItemEntity brick = new ItemEntity(level, x, y, z, new ItemStack(Items.BRICK, 1));
                brick.setDefaultPickUpDelay();
                drops.add(brick);
            } else {
                ItemEntity unfired = new ItemEntity(level, x, y, z, new ItemStack(ModItems.UNFIRED_BRICK.get(), 1));
                unfired.setDefaultPickUpDelay();
                drops.add(unfired);
            }
            return;
        }

        // --- Grass / Foliage ---
        if (block == Blocks.SHORT_GRASS || block == Blocks.TALL_GRASS || block == Blocks.FERN ||
                block == Blocks.LARGE_FERN || block == Blocks.SEAGRASS || block == Blocks.DEAD_BUSH) {
            if (!(tool.getItem() instanceof ShearsItem)) {
                drops.clear();
                return;
            }
        }

        // --- Gravel drops ---
        if (block == Blocks.GRAVEL) {
            drops.clear();
            if (RANDOM.nextFloat() < 0.08f) {
                ItemEntity shard = new ItemEntity(level, x, y, z, new ItemStack(ModItems.SILICON_SHARD.get(), 1));
                shard.setDefaultPickUpDelay();
                drops.add(shard);
            } else {
                ItemEntity gravel = new ItemEntity(level, x, y, z, new ItemStack(Blocks.GRAVEL.asItem(), 1));
                gravel.setDefaultPickUpDelay();
                drops.add(gravel);
            }
            return;
        }

        // --- A. Mining with Silicon Pickaxe ---
        if (isSiliconPickaxe(tool)) {
            if (isDeepslateOrTuffOrHighTier(block)) {
                drops.clear();
                return;
            }

            if (block == Blocks.STONE || block == Blocks.COBBLESTONE) {
                drops.clear();
                int count = 2 + RANDOM.nextInt(3); // 2..4 nuggets
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.STONE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
                return;
            } else if (block == Blocks.DIORITE) {
                drops.clear();
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.DIORITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
                return;
            } else if (block == Blocks.GRANITE) {
                drops.clear();
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.GRANITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
                return;
            } else if (block == Blocks.ANDESITE) {
                drops.clear();
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.ANDESITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
                return;
            } else if (block == Blocks.COPPER_ORE) {
                drops.clear();
                ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.COPPER_DUST.get(), 1));
                dust.setDefaultPickUpDelay();
                drops.add(dust);
                return;
            } else {
                // Silicon cannot mine other ores (Iron, Gold, etc.)
                if (block == Blocks.IRON_ORE || block == Blocks.GOLD_ORE || block == Blocks.COAL_ORE) {
                    drops.clear();
                    return;
                }
            }
        }

        // --- B. Mining with Copper Pickaxe ---
        if (isCopperPickaxe(tool)) {
            boolean isDeepslateLevel = pos.getY() <= 0 ||
                    block == Blocks.DEEPSLATE ||
                    block == Blocks.COBBLED_DEEPSLATE ||
                    block == Blocks.DEEPSLATE_IRON_ORE ||
                    block == Blocks.DEEPSLATE_COPPER_ORE ||
                    block == Blocks.TUFF;

            if (isDeepslateLevel) {
                if (block == Blocks.DEEPSLATE_IRON_ORE) {
                    drops.clear();
                    ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.IRON_DUST.get(), 1));
                    dust.setDefaultPickUpDelay();
                    drops.add(dust);
                    return;
                } else if (block == Blocks.DEEPSLATE_COPPER_ORE) {
                    drops.clear();
                    ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.COPPER_DUST.get(), 1));
                    dust.setDefaultPickUpDelay();
                    drops.add(dust);
                    return;
                } else if (block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE) {
                    drops.clear();
                    int count = 2 + RANDOM.nextInt(3);
                    ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.STONE_NUGGET.get(), count));
                    nug.setDefaultPickUpDelay();
                    drops.add(nug);
                    return;
                } else if (block == Blocks.TUFF) {
                    drops.clear();
                    int count = 2 + RANDOM.nextInt(3);
                    ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.TUFF_NUGGET.get(), count));
                    nug.setDefaultPickUpDelay();
                    drops.add(nug);
                    return;
                }
            } else {
                // Regular stone level: drops raw iron, raw copper, coal, cobblestone
                if (block == Blocks.STONE) {
                    drops.clear();
                    ItemEntity cobble = new ItemEntity(level, x, y, z, new ItemStack(Blocks.COBBLESTONE.asItem(), 1));
                    cobble.setDefaultPickUpDelay();
                    drops.add(cobble);
                    return;
                } else if (block == Blocks.IRON_ORE) {
                    drops.clear();
                    ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(Items.RAW_IRON, 1));
                    raw.setDefaultPickUpDelay();
                    drops.add(raw);
                    return;
                } else if (block == Blocks.COPPER_ORE) {
                    drops.clear();
                    ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(Items.RAW_COPPER, 1));
                    raw.setDefaultPickUpDelay();
                    drops.add(raw);
                    return;
                } else if (block == Blocks.COAL_ORE) {
                    drops.clear();
                    ItemEntity coal = new ItemEntity(level, x, y, z, new ItemStack(Items.COAL, 1));
                    coal.setDefaultPickUpDelay();
                    drops.add(coal);
                    return;
                }
            }

            if (isDiamondPlusTier(block)) {
                drops.clear();
                return;
            }
        }
    }

    // 4. Use item on block
    @SubscribeEvent
    public static void onUseItemOnBlock(UseItemOnBlockEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack held = event.getItemStack();

        if (state.is(Blocks.GRASS_BLOCK) && held.getItem() instanceof HoeItem && event.getUsePhase() == UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK) {
            if (!level.isClientSide && RANDOM.nextFloat() < 0.35f) {
                ItemEntity seed = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(Items.WHEAT_SEEDS));
                seed.setDefaultPickUpDelay();
                level.addFreshEntity(seed);
            }
        }
    }
}
