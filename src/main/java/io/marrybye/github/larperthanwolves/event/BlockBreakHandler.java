package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.UnfiredBrickBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

    public static boolean isSiliconPickaxe(ItemStack stack) {
        return stack.is(ModItems.SILICON_PICKAXE.get());
    }

    public static boolean isCopperPickaxe(ItemStack stack) {
        return stack.is(ModItems.COPPER_PICKAXE.get());
    }

    public static boolean isBronzePickaxe(ItemStack stack) {
        return stack.is(ModItems.BRONZE_PICKAXE.get());
    }

    public static boolean isIronPickaxe(ItemStack stack) {
        return stack.is(Items.IRON_PICKAXE);
    }

    public static boolean isReinforcedIronPickaxe(ItemStack stack) {
        return stack.is(ModItems.REINFORCED_IRON_PICKAXE.get());
    }

    public static boolean isNetheritePickaxe(ItemStack stack) {
        return stack.is(Items.NETHERITE_PICKAXE);
    }

    public static boolean isAnyPickaxe(ItemStack stack) {
        return isSiliconPickaxe(stack) ||
                isCopperPickaxe(stack) ||
                isBronzePickaxe(stack) ||
                isIronPickaxe(stack) ||
                isReinforcedIronPickaxe(stack) ||
                isNetheritePickaxe(stack);
    }

    public static boolean isDeepslateLayerOrBlock(Level level, BlockPos pos, Block block) {
        if (block == Blocks.DEEPSLATE ||
                block == Blocks.COBBLED_DEEPSLATE ||
                block == Blocks.POLISHED_DEEPSLATE ||
                block == Blocks.DEEPSLATE_BRICKS ||
                block == Blocks.CRACKED_DEEPSLATE_BRICKS ||
                block == Blocks.DEEPSLATE_TILES ||
                block == Blocks.CRACKED_DEEPSLATE_TILES ||
                block == Blocks.CHISELED_DEEPSLATE ||
                block == Blocks.REINFORCED_DEEPSLATE ||
                block == Blocks.TUFF ||
                block == Blocks.TUFF_STAIRS ||
                block == Blocks.TUFF_SLAB ||
                block == Blocks.TUFF_WALL ||
                block == Blocks.CHISELED_TUFF ||
                block == Blocks.POLISHED_TUFF ||
                block == Blocks.TUFF_BRICKS ||
                block == Blocks.DEEPSLATE_COAL_ORE ||
                block == Blocks.DEEPSLATE_IRON_ORE ||
                block == Blocks.DEEPSLATE_COPPER_ORE ||
                block == ModBlocks.DEEPSLATE_TIN_ORE.get() ||
                block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.DEEPSLATE_REDSTONE_ORE ||
                block == Blocks.DEEPSLATE_LAPIS_ORE ||
                block == Blocks.DEEPSLATE_DIAMOND_ORE ||
                block == Blocks.DEEPSLATE_EMERALD_ORE) {
            return true;
        }
        if (pos.getY() <= 0 && isStoneOrOre(block)) {
            return true;
        }
        return false;
    }

    public static boolean isStoneOrOre(Block block) {
        return block == Blocks.STONE ||
                block == Blocks.COBBLESTONE ||
                block == Blocks.GRANITE ||
                block == Blocks.DIORITE ||
                block == Blocks.ANDESITE ||
                block == Blocks.COAL_ORE ||
                block == Blocks.COPPER_ORE ||
                block == Blocks.IRON_ORE ||
                block == ModBlocks.TIN_ORE.get() ||
                block == Blocks.GOLD_ORE ||
                block == Blocks.REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE ||
                block == Blocks.DIAMOND_ORE ||
                block == Blocks.EMERALD_ORE ||
                block == Blocks.DEEPSLATE ||
                block == Blocks.COBBLED_DEEPSLATE ||
                block == Blocks.TUFF ||
                block == ModBlocks.RAW_TIN_BLOCK.get() ||
                block == ModBlocks.TIN_BLOCK.get() ||
                block == ModBlocks.BRONZE_BLOCK.get();
    }

    public static boolean isObsidianOrNetheriteTier(Block block) {
        return block == Blocks.OBSIDIAN ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.RESPAWN_ANCHOR ||
                block == Blocks.ANCIENT_DEBRIS;
    }

    public static boolean isHighTierOre(Block block) {
        return block == Blocks.GOLD_ORE ||
                block == Blocks.REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE ||
                block == Blocks.DIAMOND_ORE ||
                block == Blocks.EMERALD_ORE ||
                block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.DEEPSLATE_REDSTONE_ORE ||
                block == Blocks.DEEPSLATE_LAPIS_ORE ||
                block == Blocks.DEEPSLATE_DIAMOND_ORE ||
                block == Blocks.DEEPSLATE_EMERALD_ORE;
    }

    public static boolean canToolMineBlock(ItemStack tool, Level level, BlockPos pos, Block block) {
        if (!isStoneOrOre(block) && !isObsidianOrNetheriteTier(block) && !isDeepslateLayerOrBlock(level, pos, block)) {
            return true;
        }

        if (isNetheritePickaxe(tool) || isReinforcedIronPickaxe(tool)) {
            return true;
        }

        if (isIronPickaxe(tool)) {
            return !isObsidianOrNetheriteTier(block);
        }

        // Silicon, Copper, Bronze tiers CANNOT mine deepslate layer or deepslate blocks
        if (isDeepslateLayerOrBlock(level, pos, block)) {
            return false;
        }

        // Silicon, Copper, Bronze tiers CANNOT mine Obsidian / Ancient Debris or High Tier ores
        if (isObsidianOrNetheriteTier(block) || isHighTierOre(block)) {
            return false;
        }

        if (isBronzePickaxe(tool)) {
            return block == Blocks.COAL_ORE ||
                    block == Blocks.COPPER_ORE ||
                    block == Blocks.IRON_ORE ||
                    block == ModBlocks.TIN_ORE.get() ||
                    block == Blocks.STONE ||
                    block == Blocks.COBBLESTONE ||
                    block == Blocks.GRANITE ||
                    block == Blocks.DIORITE ||
                    block == Blocks.ANDESITE ||
                    block == ModBlocks.RAW_TIN_BLOCK.get() ||
                    block == ModBlocks.TIN_BLOCK.get() ||
                    block == ModBlocks.BRONZE_BLOCK.get();
        }

        if (isCopperPickaxe(tool)) {
            return block == Blocks.COAL_ORE ||
                    block == Blocks.COPPER_ORE ||
                    block == Blocks.IRON_ORE ||
                    block == ModBlocks.TIN_ORE.get() ||
                    block == Blocks.STONE ||
                    block == Blocks.COBBLESTONE ||
                    block == Blocks.GRANITE ||
                    block == Blocks.DIORITE ||
                    block == Blocks.ANDESITE ||
                    block == ModBlocks.RAW_TIN_BLOCK.get() ||
                    block == ModBlocks.TIN_BLOCK.get() ||
                    block == ModBlocks.BRONZE_BLOCK.get();
        }

        if (isSiliconPickaxe(tool)) {
            return block == Blocks.COAL_ORE ||
                    block == Blocks.COPPER_ORE ||
                    block == Blocks.STONE ||
                    block == Blocks.COBBLESTONE ||
                    block == Blocks.GRANITE ||
                    block == Blocks.DIORITE ||
                    block == Blocks.ANDESITE;
        }

        // Hand or unhandled tool on stone/ore
        return false;
    }

    // 1. Mining speed checks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack held = player.getMainHandItem();
        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPosition().orElse(player.blockPosition());
        Level level = player.level();

        if (isStoneOrOre(block) || isObsidianOrNetheriteTier(block) || isDeepslateLayerOrBlock(level, pos, block)) {
            if (!canToolMineBlock(held, level, pos, block)) {
                event.setNewSpeed(0.0f);
                event.setCanceled(true);
            }
        }
    }

    // 2. Block break event
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide) return;

        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPos();
        Level level = player.level();
        ItemStack held = player.getMainHandItem();

        if (isStoneOrOre(block) || isObsidianOrNetheriteTier(block) || isDeepslateLayerOrBlock(level, pos, block)) {
            if (!canToolMineBlock(held, level, pos, block)) {
                event.setCanceled(true);
                if (isDeepslateLayerOrBlock(level, pos, block)) {
                    player.displayClientMessage(Component.literal("§cЭтот инструмент не может добывать породу глубинного сланца!"), true);
                } else if (isObsidianOrNetheriteTier(block)) {
                    player.displayClientMessage(Component.literal("§cДля добычи этого блока нужна алмазная (укрепленная) или незеритовая кирка!"), true);
                } else if (isHighTierOre(block)) {
                    player.displayClientMessage(Component.literal("§cДля этой руды требуется железная кирка или выше!"), true);
                } else if (isSiliconPickaxe(held) && (block == Blocks.IRON_ORE || block == ModBlocks.TIN_ORE.get())) {
                    player.displayClientMessage(Component.literal("§cКремниевая кирка не может добывать железную или оловянную руду!"), true);
                }
                return;
            }
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
        if (block == Blocks.GRAVEL || block == Blocks.SUSPICIOUS_GRAVEL) {
            drops.clear();

            double copperChance = ModConfig.SERVER != null ? ModConfig.SERVER.copperDustGravelDropChance.get() : 0.05;
            double siliconChance = ModConfig.SERVER != null ? ModConfig.SERVER.siliconShardGravelDropChance.get() : 0.08;

            float roll = RANDOM.nextFloat();
            if (roll < copperChance) {
                ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.COPPER_DUST.get(), 1));
                dust.setDefaultPickUpDelay();
                drops.add(dust);
            } else if (roll < copperChance + siliconChance) {
                ItemEntity shard = new ItemEntity(level, x, y, z, new ItemStack(ModItems.SILICON_SHARD.get(), 1));
                shard.setDefaultPickUpDelay();
                drops.add(shard);
            } else if (roll < copperChance + siliconChance + 0.10f) {
                ItemEntity flint = new ItemEntity(level, x, y, z, new ItemStack(Items.FLINT, 1));
                flint.setDefaultPickUpDelay();
                drops.add(flint);
            } else {
                ItemEntity gravel = new ItemEntity(level, x, y, z, new ItemStack(Blocks.GRAVEL.asItem(), 1));
                gravel.setDefaultPickUpDelay();
                drops.add(gravel);
            }
            return;
        }

        if (isStoneOrOre(block) || isObsidianOrNetheriteTier(block) || isDeepslateLayerOrBlock(level, pos, block)) {
            if (!canToolMineBlock(tool, level, pos, block)) {
                drops.clear();
                return;
            }
        }

        // --- 1. SILICON PICKAXE DROPS ---
        if (isSiliconPickaxe(tool)) {
            drops.clear();
            if (block == Blocks.COPPER_ORE) {
                ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.COPPER_DUST.get(), 1));
                dust.setDefaultPickUpDelay();
                drops.add(dust);
            } else if (block == Blocks.COAL_ORE) {
                ItemEntity coal = new ItemEntity(level, x, y, z, new ItemStack(Items.COAL, 1));
                coal.setDefaultPickUpDelay();
                drops.add(coal);
            } else if (block == Blocks.STONE || block == Blocks.COBBLESTONE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.STONE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            } else if (block == Blocks.GRANITE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.GRANITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            } else if (block == Blocks.DIORITE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.DIORITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            } else if (block == Blocks.ANDESITE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.ANDESITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            }
            return;
        }

        // --- 2. COPPER PICKAXE DROPS ---
        if (isCopperPickaxe(tool)) {
            drops.clear();
            if (block == Blocks.COAL_ORE) {
                ItemEntity coal = new ItemEntity(level, x, y, z, new ItemStack(Items.COAL, 1));
                coal.setDefaultPickUpDelay();
                drops.add(coal);
            } else if (block == Blocks.COPPER_ORE) {
                ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(Items.RAW_COPPER, 1));
                raw.setDefaultPickUpDelay();
                drops.add(raw);
            } else if (block == Blocks.IRON_ORE) {
                ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.IRON_DUST.get(), 1));
                dust.setDefaultPickUpDelay();
                drops.add(dust);
            } else if (block == ModBlocks.TIN_ORE.get()) {
                ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(ModItems.RAW_TIN.get(), 1));
                raw.setDefaultPickUpDelay();
                drops.add(raw);
            } else if (block == Blocks.STONE || block == Blocks.COBBLESTONE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.STONE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            } else if (block == Blocks.GRANITE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.GRANITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            } else if (block == Blocks.DIORITE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.DIORITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            } else if (block == Blocks.ANDESITE) {
                int count = 2 + RANDOM.nextInt(3);
                ItemEntity nug = new ItemEntity(level, x, y, z, new ItemStack(ModItems.ANDESITE_NUGGET.get(), count));
                nug.setDefaultPickUpDelay();
                drops.add(nug);
            }
            return;
        }

        // --- 3. BRONZE PICKAXE DROPS ---
        if (isBronzePickaxe(tool)) {
            drops.clear();
            if (block == Blocks.COAL_ORE) {
                ItemEntity coal = new ItemEntity(level, x, y, z, new ItemStack(Items.COAL, 1));
                coal.setDefaultPickUpDelay();
                drops.add(coal);
            } else if (block == Blocks.COPPER_ORE) {
                ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(Items.RAW_COPPER, 1));
                raw.setDefaultPickUpDelay();
                drops.add(raw);
            } else if (block == Blocks.IRON_ORE) {
                ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(Items.RAW_IRON, 1));
                raw.setDefaultPickUpDelay();
                drops.add(raw);
            } else if (block == ModBlocks.TIN_ORE.get()) {
                ItemEntity raw = new ItemEntity(level, x, y, z, new ItemStack(ModItems.RAW_TIN.get(), 1));
                raw.setDefaultPickUpDelay();
                drops.add(raw);
            } else if (block == Blocks.STONE || block == Blocks.COBBLESTONE) {
                ItemEntity cobble = new ItemEntity(level, x, y, z, new ItemStack(Blocks.COBBLESTONE.asItem(), 1));
                cobble.setDefaultPickUpDelay();
                drops.add(cobble);
            } else if (block == Blocks.GRANITE) {
                ItemEntity g = new ItemEntity(level, x, y, z, new ItemStack(Blocks.GRANITE.asItem(), 1));
                g.setDefaultPickUpDelay();
                drops.add(g);
            } else if (block == Blocks.DIORITE) {
                ItemEntity d = new ItemEntity(level, x, y, z, new ItemStack(Blocks.DIORITE.asItem(), 1));
                d.setDefaultPickUpDelay();
                drops.add(d);
            } else if (block == Blocks.ANDESITE) {
                ItemEntity a = new ItemEntity(level, x, y, z, new ItemStack(Blocks.ANDESITE.asItem(), 1));
                a.setDefaultPickUpDelay();
                drops.add(a);
            }
            return;
        }

        // --- 4. IRON, REINFORCED IRON, NETHERITE PICKAXES ---
        // Default vanilla loot tables
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
