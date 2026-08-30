package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.UnfiredBrickBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = "larperthanwolves")
public class BlockBreakHandler {

    private enum PickaxeTier {
        SILICON, COPPER, BRONZE, IRON_PLUS;
    }

    private static PickaxeTier getPickaxeTier(ItemStack tool) {
        if (isSiliconPickaxe(tool)) return PickaxeTier.SILICON;
        if (isCopperPickaxe(tool)) return PickaxeTier.COPPER;
        if (isBronzePickaxe(tool)) return PickaxeTier.BRONZE;
        return PickaxeTier.IRON_PLUS;
    }

    private static ItemStack getDropForBlock(Block block, PickaxeTier tier) {
        if (tier == PickaxeTier.IRON_PLUS) return null;

        if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE) {
            return new ItemStack(Items.COAL, 1);
        } else if (block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE) {
            if (tier == PickaxeTier.SILICON) {
                return new ItemStack(ModItems.COPPER_DUST.get(), 1);
            }
            return new ItemStack(Items.RAW_COPPER, 1);
        } else if (block == ModBlocks.TIN_ORE.get() || block == ModBlocks.DEEPSLATE_TIN_ORE.get()) {
            if (tier == PickaxeTier.SILICON) return null;
            if (tier == PickaxeTier.COPPER) return new ItemStack(ModItems.TIN_DUST.get(), 1);
            return new ItemStack(ModItems.RAW_TIN.get(), 1);
        } else if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE) {
            if (tier == PickaxeTier.SILICON || tier == PickaxeTier.COPPER) return null;
            return new ItemStack(ModItems.IRON_DUST.get(), 1);
        }

        int count = 2 + ThreadLocalRandom.current().nextInt(3);

        if (block == Blocks.STONE || block == Blocks.COBBLESTONE) {
            return new ItemStack(ModItems.STONE_NUGGET.get(), count);
        } else if (block == Blocks.GRANITE) {
            return new ItemStack(ModItems.GRANITE_NUGGET.get(), count);
        } else if (block == Blocks.DIORITE) {
            return new ItemStack(ModItems.DIORITE_NUGGET.get(), count);
        } else if (block == Blocks.ANDESITE) {
            return new ItemStack(ModItems.ANDESITE_NUGGET.get(), count);
        } else if (block == Blocks.CALCITE) {
            return new ItemStack(ModItems.CALCITE_NUGGET.get(), count);
        } else if (isSandstone(block)) {
            boolean isRed = block == Blocks.RED_SANDSTONE || block == Blocks.SMOOTH_RED_SANDSTONE ||
                    block == Blocks.CUT_RED_SANDSTONE || block == Blocks.CHISELED_RED_SANDSTONE;
            return new ItemStack(isRed ? Items.RED_SAND : Items.SAND, count);
        }

        // Bronze tier drops pebbles for deepslate, tuff, dripstone, and netherrack
        if (tier == PickaxeTier.BRONZE) {
            if (block == Blocks.DEEPSLATE || block == Blocks.COBBLED_DEEPSLATE) {
                return new ItemStack(ModItems.DEEPSLATE_NUGGET.get(), count);
            } else if (block == Blocks.TUFF) {
                return new ItemStack(ModItems.TUFF_NUGGET.get(), count);
            } else if (block == Blocks.DRIPSTONE_BLOCK) {
                return new ItemStack(ModItems.DRIPSTONE_NUGGET.get(), count);
            } else if (block == Blocks.POINTED_DRIPSTONE) {
                return new ItemStack(ModItems.DRIPSTONE_NUGGET.get(), 1);
            } else if (block == Blocks.NETHERRACK) {
                return new ItemStack(ModItems.NETHERRACK_NUGGET.get(), count);
            }
        }

        return null;
    }

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

    public static boolean isSiliconShovel(ItemStack stack) {
        return stack.is(ModItems.SILICON_SHOVEL.get());
    }

    public static boolean isCopperShovel(ItemStack stack) {
        return stack.is(ModItems.COPPER_SHOVEL.get());
    }

    public static boolean isBronzeShovel(ItemStack stack) {
        return stack.is(ModItems.BRONZE_SHOVEL.get());
    }

    public static boolean isIronShovel(ItemStack stack) {
        return stack.is(Items.IRON_SHOVEL);
    }

    public static boolean isReinforcedIronShovel(ItemStack stack) {
        return stack.is(ModItems.REINFORCED_IRON_SHOVEL.get());
    }

    public static boolean isNetheriteShovel(ItemStack stack) {
        return stack.is(Items.NETHERITE_SHOVEL);
    }

    public static boolean isCopperPlusShovel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        return isCopperShovel(stack) || isBronzeShovel(stack) || isIronShovel(stack) || isReinforcedIronShovel(stack) || isNetheriteShovel(stack);
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

    public static boolean isNetherOrEndRockOrOre(Block block) {
        return block == Blocks.NETHER_QUARTZ_ORE ||
                block == Blocks.NETHER_GOLD_ORE ||
                block == Blocks.BLACKSTONE ||
                block == Blocks.GILDED_BLACKSTONE ||
                block == Blocks.POLISHED_BLACKSTONE ||
                block == Blocks.POLISHED_BLACKSTONE_BRICKS ||
                block == Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS ||
                block == Blocks.CHISELED_POLISHED_BLACKSTONE ||
                block == Blocks.BASALT ||
                block == Blocks.SMOOTH_BASALT ||
                block == Blocks.POLISHED_BASALT ||
                block == Blocks.END_STONE ||
                block == Blocks.END_STONE_BRICKS ||
                block == Blocks.PRISMARINE ||
                block == Blocks.PRISMARINE_BRICKS ||
                block == Blocks.DARK_PRISMARINE ||
                block == Blocks.AMETHYST_BLOCK ||
                block == Blocks.BUDDING_AMETHYST;
    }

    public static boolean isSandstone(Block block) {
        return block == Blocks.SANDSTONE ||
                block == Blocks.SMOOTH_SANDSTONE ||
                block == Blocks.CUT_SANDSTONE ||
                block == Blocks.CHISELED_SANDSTONE ||
                block == Blocks.RED_SANDSTONE ||
                block == Blocks.SMOOTH_RED_SANDSTONE ||
                block == Blocks.CUT_RED_SANDSTONE ||
                block == Blocks.CHISELED_RED_SANDSTONE;
    }

    public static boolean isZincOre(Block block) {
        if (block == null) return false;
        net.minecraft.resources.ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block);
        if (key != null) {
            if ("create".equals(key.getNamespace()) && (key.getPath().equals("zinc_ore") || key.getPath().equals("deepslate_zinc_ore"))) {
                return true;
            }
            if (key.getPath().contains("zinc_ore")) {
                return true;
            }
        }
        return block.defaultBlockState().is(BlockTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "ores/zinc")));
    }

    public static boolean isStoneOrOre(Block block) {
        return isZincOre(block) ||
                block == Blocks.STONE ||
                block == Blocks.COBBLESTONE ||
                block == Blocks.GRANITE ||
                block == Blocks.DIORITE ||
                block == Blocks.ANDESITE ||
                block == Blocks.CALCITE ||
                block == Blocks.DRIPSTONE_BLOCK ||
                block == Blocks.POINTED_DRIPSTONE ||
                block == Blocks.NETHERRACK ||
                isSandstone(block) ||
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
                block == ModBlocks.BRONZE_BLOCK.get() ||
                isNetherOrEndRockOrOre(block);
    }

    public static boolean isObsidianOrNetheriteTier(Block block) {
        return block == Blocks.OBSIDIAN ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.RESPAWN_ANCHOR ||
                block == Blocks.ANCIENT_DEBRIS;
    }

    public static boolean isHighTierOre(Block block) {
        return isZincOre(block) ||
                block == Blocks.GOLD_ORE ||
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

        // Silicon and Copper tiers CANNOT mine deepslate layer or deepslate blocks
        if (isSiliconPickaxe(tool) || isCopperPickaxe(tool)) {
            if (isDeepslateLayerOrBlock(level, pos, block)) {
                return false;
            }
        }

        // Silicon, Copper, Bronze tiers CANNOT mine Obsidian / Ancient Debris, High Tier ores, or Nether/End rocks/ores
        if (isObsidianOrNetheriteTier(block) || isHighTierOre(block) || isNetherOrEndRockOrOre(block)) {
            return false;
        }

        if (isBronzePickaxe(tool)) {
            return block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE ||
                    block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE ||
                    block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE ||
                    block == ModBlocks.TIN_ORE.get() || block == ModBlocks.DEEPSLATE_TIN_ORE.get() ||
                    block == Blocks.STONE ||
                    block == Blocks.COBBLESTONE ||
                    block == Blocks.GRANITE ||
                    block == Blocks.DIORITE ||
                    block == Blocks.ANDESITE ||
                    block == Blocks.CALCITE ||
                    block == Blocks.DEEPSLATE ||
                    block == Blocks.COBBLED_DEEPSLATE ||
                    block == Blocks.TUFF ||
                    block == Blocks.DRIPSTONE_BLOCK ||
                    block == Blocks.POINTED_DRIPSTONE ||
                    block == Blocks.NETHERRACK ||
                    isSandstone(block) ||
                    block == ModBlocks.RAW_TIN_BLOCK.get() ||
                    block == ModBlocks.TIN_BLOCK.get() ||
                    block == ModBlocks.BRONZE_BLOCK.get();
        }

        if (isCopperPickaxe(tool)) {
            return block == Blocks.COAL_ORE ||
                    block == Blocks.COPPER_ORE ||
                    block == ModBlocks.TIN_ORE.get() ||
                    block == Blocks.STONE ||
                    block == Blocks.COBBLESTONE ||
                    block == Blocks.GRANITE ||
                    block == Blocks.DIORITE ||
                    block == Blocks.ANDESITE ||
                    block == Blocks.CALCITE ||
                    isSandstone(block) ||
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
                    block == Blocks.ANDESITE ||
                    block == Blocks.CALCITE ||
                    isSandstone(block);
        }

        // Hand or unhandled tool on stone/ore
        return false;
    }

    public static boolean isWoodOrPlank(BlockState state) {
        return state.is(BlockTags.LOGS) ||
                state.is(BlockTags.PLANKS) ||
                state.is(BlockTags.WOODEN_SLABS) ||
                state.is(BlockTags.WOODEN_STAIRS) ||
                state.is(BlockTags.WOODEN_FENCES) ||
                state.is(BlockTags.FENCE_GATES) ||
                state.is(BlockTags.WOODEN_DOORS) ||
                state.is(BlockTags.WOODEN_TRAPDOORS) ||
                ModBlocks.isStump(state) ||
                state.is(ModBlocks.WORK_STUMP.get());
    }

    public static boolean isAxe(ItemStack tool) {
        if (tool == null || tool.isEmpty()) return false;
        return tool.getItem() instanceof net.minecraft.world.item.AxeItem ||
                tool.is(net.minecraft.tags.ItemTags.AXES);
    }

    public static boolean isShears(ItemStack tool) {
        if (tool == null || tool.isEmpty()) return false;
        return tool.getItem() instanceof ShearsItem ||
                tool.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "tools/shears"))) ||
                tool.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("minecraft", "shears")));
    }

    public static boolean isShovel(ItemStack tool) {
        if (tool == null || tool.isEmpty()) return false;
        return tool.getItem() instanceof net.minecraft.world.item.ShovelItem ||
                tool.is(net.minecraft.tags.ItemTags.SHOVELS);
    }

    // 1. Mining speed checks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (player == null) return;
        ItemStack held = player.getMainHandItem();
        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPosition().orElse(player.blockPosition());
        Level level = player.level();

        // Chisel on logs / stumps: allow visual breaking animation (cracks) without stopping speed
        if (held.is(ModItems.CHISEL.get())) {
            if (state.is(BlockTags.LOGS) || state.is(ModBlocks.WORK_STUMP.get()) || ModBlocks.isStump(state)) {
                event.setNewSpeed(1.5f);
                return;
            }
        }

        // Clay breaks slightly slower
        if (state.is(Blocks.CLAY)) {
            if (isShovel(held)) {
                event.setNewSpeed(event.getOriginalSpeed() * 0.55f);
            } else {
                event.setNewSpeed(event.getOriginalSpeed() * 0.35f);
            }
        }

        // Cannot break logs/wood/planks without an axe!
        if (isWoodOrPlank(state)) {
            if (!isAxe(held)) {
                event.setNewSpeed(0.0f);
                event.setCanceled(true);
                return;
            }
        }

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
        if (player.isCreative() || player.getAbilities().instabuild) return;

        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPos();
        Level level = player.level();
        ItemStack held = player.getMainHandItem();

        // Chisel CANNOT break logs/stumps on Left Click (animation only)
        if (held.is(ModItems.CHISEL.get())) {
            if (state.is(BlockTags.LOGS) || state.is(ModBlocks.WORK_STUMP.get()) || ModBlocks.isStump(state)) {
                event.setCanceled(true);
                return;
            }
        }

        // Cannot break logs/wood/planks without an axe!
        if (isWoodOrPlank(state)) {
            if (!isAxe(held)) {
                event.setCanceled(true);
                return;
            }
        }

        if (isStoneOrOre(block) || isObsidianOrNetheriteTier(block) || isDeepslateLayerOrBlock(level, pos, block)) {
            if (!canToolMineBlock(held, level, pos, block)) {
                event.setCanceled(true);
                return;
            }
        }
    }

    // Prevent damage / camera recoil when hitting wood/blocks with Chisel
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getMainHandItem().is(ModItems.CHISEL.get())) {
                // If damage source has no attacker entity (e.g. tree punching penalty / environmental wood damage)
                if (event.getSource().getEntity() == null && event.getSource().getDirectEntity() == null) {
                    event.setCanceled(true);
                }
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
        if (tool.isEmpty() && event.getBreaker() instanceof Player p) {
            tool = p.getMainHandItem();
        }
        if (event.getBreaker() instanceof Player p && (p.isCreative() || p.getAbilities().instabuild)) {
            event.getDrops().clear();
            return;
        }

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

        // --- Clay Block (strictly requires shovel, drops 1 clay ball instead of 4) ---
        if (block == Blocks.CLAY) {
            drops.clear();
            if (isShovel(tool)) {
                ItemEntity clayDrop = new ItemEntity(level, x, y, z, new ItemStack(Items.CLAY_BALL, 1));
                clayDrop.setDefaultPickUpDelay();
                drops.add(clayDrop);
            }
            return;
        }

        // --- Wood / Logs / Planks require an axe ---
        if (isWoodOrPlank(state)) {
            if (!isAxe(tool)) {
                drops.clear();
                return;
            }
        }

        // --- Grass / Foliage ---
        if (block == Blocks.SHORT_GRASS || block == Blocks.TALL_GRASS || block == Blocks.FERN ||
                block == Blocks.LARGE_FERN || block == Blocks.SEAGRASS || block == Blocks.DEAD_BUSH) {
            if (!isShears(tool)) {
                drops.clear();
                return;
            } else {
                drops.clear();
                if (block == Blocks.SHORT_GRASS) {
                    ItemEntity item = new ItemEntity(level, x, y, z, new ItemStack(Items.SHORT_GRASS, 1));
                    item.setDefaultPickUpDelay();
                    drops.add(item);
                } else if (block == Blocks.TALL_GRASS) {
                    ItemEntity item = new ItemEntity(level, x, y, z, new ItemStack(Items.SHORT_GRASS, 2));
                    item.setDefaultPickUpDelay();
                    drops.add(item);
                } else if (block == Blocks.FERN) {
                    ItemEntity item = new ItemEntity(level, x, y, z, new ItemStack(Items.FERN, 1));
                    item.setDefaultPickUpDelay();
                    drops.add(item);
                } else if (block == Blocks.LARGE_FERN) {
                    ItemEntity item = new ItemEntity(level, x, y, z, new ItemStack(Items.FERN, 2));
                    item.setDefaultPickUpDelay();
                    drops.add(item);
                } else if (block == Blocks.SEAGRASS) {
                    ItemEntity item = new ItemEntity(level, x, y, z, new ItemStack(Items.SEAGRASS, 1));
                    item.setDefaultPickUpDelay();
                    drops.add(item);
                } else if (block == Blocks.DEAD_BUSH) {
                    ItemEntity item = new ItemEntity(level, x, y, z, new ItemStack(Items.DEAD_BUSH, 1));
                    item.setDefaultPickUpDelay();
                    drops.add(item);
                }
                return;
            }
        }

        // --- Leaves Drops (Twigs) ---
        if (block.defaultBlockState().is(net.minecraft.tags.BlockTags.LEAVES)) {
            if (!(tool.getItem() instanceof net.minecraft.world.item.ShearsItem)) {
                float twigRoll = ThreadLocalRandom.current().nextFloat();
                if (twigRoll < 0.35f) {
                    int count = ThreadLocalRandom.current().nextFloat() < 0.70f ? 1 : 2;
                    ItemEntity twigDrop = new ItemEntity(level, x, y, z, new ItemStack(ModItems.TWIG.get(), count));
                    twigDrop.setDefaultPickUpDelay();
                    drops.add(twigDrop);
                }
            }
        }

        // --- Rich Soils Drops (Only drops rich block if mined with copper+ shovel, otherwise regular soil) ---
        if (block == ModBlocks.RICH_GRASS_BLOCK.get() || block == ModBlocks.RICH_DIRT.get() ||
                block == ModBlocks.RICH_GRAVEL.get() || block == ModBlocks.RICH_SAND.get() ||
                block == ModBlocks.RICH_RED_SAND.get()) {
            drops.clear();
            if (isCopperPlusShovel(tool)) {
                if (block == ModBlocks.RICH_GRASS_BLOCK.get()) {
                    ItemEntity richDrop = new ItemEntity(level, x, y, z, new ItemStack(ModBlocks.RICH_DIRT.get().asItem(), 1));
                    richDrop.setDefaultPickUpDelay();
                    drops.add(richDrop);
                } else {
                    ItemEntity richDrop = new ItemEntity(level, x, y, z, new ItemStack(block.asItem(), 1));
                    richDrop.setDefaultPickUpDelay();
                    drops.add(richDrop);
                }
            } else {
                Block regularCounterpart = Blocks.DIRT;
                if (block == ModBlocks.RICH_GRAVEL.get()) regularCounterpart = Blocks.GRAVEL;
                else if (block == ModBlocks.RICH_SAND.get()) regularCounterpart = Blocks.SAND;
                else if (block == ModBlocks.RICH_RED_SAND.get()) regularCounterpart = Blocks.RED_SAND;

                ItemEntity regDrop = new ItemEntity(level, x, y, z, new ItemStack(regularCounterpart.asItem(), 1));
                regDrop.setDefaultPickUpDelay();
                drops.add(regDrop);
            }
            return;
        }

        // --- Regular Soils Drops (Gravel, Sand, Red Sand, Dirt, Suspicious Gravel, Suspicious Sand) ---
        if (block == Blocks.GRAVEL || block == Blocks.SUSPICIOUS_GRAVEL ||
                block == Blocks.SAND || block == Blocks.RED_SAND ||
                block == Blocks.SUSPICIOUS_SAND || block == Blocks.DIRT) {
            drops.clear();

            double copperChance = ModConfig.SERVER != null ? ModConfig.SERVER.copperDustGravelDropChance.get() : 0.02;
            double flintChance = ModConfig.SERVER != null ? ModConfig.SERVER.flintGravelDropChance.get() : 0.08;
            double siliconChance = ModConfig.SERVER != null ? ModConfig.SERVER.siliconShardGravelDropChance.get() : 0.20;

            float roll = ThreadLocalRandom.current().nextFloat();
            if (roll < copperChance) {
                ItemEntity dust = new ItemEntity(level, x, y, z, new ItemStack(ModItems.COPPER_DUST.get(), 1));
                dust.setDefaultPickUpDelay();
                drops.add(dust);
            } else if (roll < copperChance + flintChance) {
                ItemEntity flint = new ItemEntity(level, x, y, z, new ItemStack(Items.FLINT, 1));
                flint.setDefaultPickUpDelay();
                drops.add(flint);
            } else if (roll < copperChance + flintChance + siliconChance) {
                ItemEntity shard = new ItemEntity(level, x, y, z, new ItemStack(ModItems.SILICON_SHARD.get(), 1));
                shard.setDefaultPickUpDelay();
                drops.add(shard);
            } else {
                Block itemBlock = (block == Blocks.SUSPICIOUS_GRAVEL) ? Blocks.GRAVEL : (block == Blocks.SUSPICIOUS_SAND ? Blocks.SAND : block);
                ItemEntity regular = new ItemEntity(level, x, y, z, new ItemStack(itemBlock.asItem(), 1));
                regular.setDefaultPickUpDelay();
                drops.add(regular);
            }
            return;
        }

        if (isStoneOrOre(block) || isObsidianOrNetheriteTier(block) || isDeepslateLayerOrBlock(level, pos, block)) {
            if (!canToolMineBlock(tool, level, pos, block)) {
                drops.clear();
                return;
            }
        }

        PickaxeTier tier = getPickaxeTier(tool);
        if (tier != PickaxeTier.IRON_PLUS && isStoneOrOre(block)) {
            drops.clear();
            ItemStack customDrop = getDropForBlock(block, tier);
            if (customDrop != null && !customDrop.isEmpty()) {
                ItemEntity dropEntity = new ItemEntity(level, x, y, z, customDrop);
                dropEntity.setDefaultPickUpDelay();
                drops.add(dropEntity);
            }
            return;
        }

        // --- 4. IRON, REINFORCED IRON, NETHERITE PICKAXES ---
        // Default vanilla loot tables
    }

    // 4. Use item on block (2-stage hoe tilling & seed harvesting)
    @SubscribeEvent
    public static void onUseItemOnBlock(UseItemOnBlockEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack held = event.getItemStack();

        if ((state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.PODZOL) || state.is(Blocks.MYCELIUM) || state.is(ModBlocks.RICH_GRASS_BLOCK.get())) && held.getItem() instanceof HoeItem) {
            if (!level.isClientSide) {
                // Convert grassy block to plain dirt / rich dirt
                Block targetSoil = state.is(ModBlocks.RICH_GRASS_BLOCK.get()) ? ModBlocks.RICH_DIRT.get() : Blocks.DIRT;
                level.setBlock(pos, targetSoil.defaultBlockState(), 3);
                level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                double seedChance = ModConfig.SERVER != null ? ModConfig.SERVER.hoeGrassSeedDropChance.get() : 0.35;
                if (ThreadLocalRandom.current().nextFloat() < seedChance) {
                    ItemStack dropStack = getRandomWildCropSeed();
                    ItemEntity seed = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, dropStack);
                    seed.setDefaultPickUpDelay();
                    level.addFreshEntity(seed);
                }

                held.hurtAndBreak(1, player, event.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            event.cancelWithResult(net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide));
            return;
        }

        if (state.is(ModBlocks.RICH_DIRT.get()) && held.getItem() instanceof HoeItem) {
            if (!level.isClientSide) {
                level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 3);
                level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                held.hurtAndBreak(1, player, event.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            event.cancelWithResult(net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide));
            return;
        }
    }

    public static boolean isCrop(Block block) {
        return block instanceof net.minecraft.world.level.block.CropBlock
                || block instanceof net.minecraft.world.level.block.StemBlock
                || block instanceof net.minecraft.world.level.block.AttachedStemBlock
                || block instanceof net.minecraft.world.level.block.TorchflowerCropBlock
                || block instanceof net.minecraft.world.level.block.PitcherCropBlock
                || block instanceof net.minecraft.world.level.block.SweetBerryBushBlock;
    }

    // 5. Bone meal fertilization mechanic (soil fertilization instead of instant growth, 1 charge)
    @SubscribeEvent
    public static void onBonemeal(net.neoforged.neoforge.event.entity.player.BonemealEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        // 1. Direct use on Farmland
        if (state.is(Blocks.FARMLAND)) {
            if (!level.isClientSide) {
                int moisture = state.hasProperty(net.minecraft.world.level.block.FarmBlock.MOISTURE) ?
                        state.getValue(net.minecraft.world.level.block.FarmBlock.MOISTURE) : 0;
                level.setBlock(pos, ModBlocks.FERTILIZED_FARMLAND.get().defaultBlockState()
                        .setValue(net.minecraft.world.level.block.FarmBlock.MOISTURE, moisture)
                        .setValue(io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.CHARGES, 1), 3);
                level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 10, 0.3, 0.1, 0.3, 0.05);
                }
            }
            event.setSuccessful(true);
            return;
        }

        // If farmland is already fertilized, do not consume/waste bone meal
        if (state.is(ModBlocks.FERTILIZED_FARMLAND.get())) {
            event.setCanceled(true);
            return;
        }

        // 2. Use on a Crop planted on Farmland
        if (isCrop(state.getBlock())) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (belowState.is(Blocks.FARMLAND)) {
                if (!level.isClientSide) {
                    int moisture = belowState.hasProperty(net.minecraft.world.level.block.FarmBlock.MOISTURE) ?
                            belowState.getValue(net.minecraft.world.level.block.FarmBlock.MOISTURE) : 0;
                    level.setBlock(belowPos, ModBlocks.FERTILIZED_FARMLAND.get().defaultBlockState()
                            .setValue(net.minecraft.world.level.block.FarmBlock.MOISTURE, moisture)
                            .setValue(io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.CHARGES, 1), 3);
                    level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.3, 0.2, 0.3, 0.05);
                    }
                }
                // Successfully fertilized! Prevents vanilla instant growth and consumes bone meal
                event.setSuccessful(true);
                return;
            } else if (belowState.is(ModBlocks.FERTILIZED_FARMLAND.get())) {
                // Already fertilized! Prevent instant growth and do not consume extra bone meal
                event.setCanceled(true);
                return;
            }
        }
    }

    // 6. Crop growth prevention on unfertilized soil
    @SubscribeEvent
    public static void onCropGrowPre(net.neoforged.neoforge.event.level.block.CropGrowEvent.Pre event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (isCrop(state.getBlock())) {
            BlockPos belowPos = pos.below();
            BlockState belowState = event.getLevel().getBlockState(belowPos);

            // Soil MUST be Fertilized Farmland for crops to grow
            if (!belowState.is(ModBlocks.FERTILIZED_FARMLAND.get())) {
                event.setResult(net.neoforged.neoforge.event.level.block.CropGrowEvent.Pre.Result.DO_NOT_GROW);
            }
        }
    }

    // 7. Crop harvest/break decrements fertilized farmland charges (or resets when depleted)
    @SubscribeEvent
    public static void onCropBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (isCrop(state.getBlock())) {
            BlockPos belowPos = pos.below();
            BlockState belowState = event.getLevel().getBlockState(belowPos);
            if (belowState.is(ModBlocks.FERTILIZED_FARMLAND.get())) {
                BlockState nextState = io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.getAfterHarvestState(belowState);
                event.getLevel().setBlock(belowPos, nextState, 3);
            }
        }
    }

    // 8. Right click handlers for Dung fertilization and Sweet Berry harvesting
    @SubscribeEvent
    public static void onCropRightClick(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        // Dung fertilization (Provides 3 harvest charges)
        if (held.is(ModItems.DUNG.get())) {
            // Direct click on Farmland
            if (state.is(Blocks.FARMLAND) || (state.is(ModBlocks.FERTILIZED_FARMLAND.get()) && state.getValue(io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.CHARGES) < 3)) {
                if (!level.isClientSide) {
                    int moisture = state.hasProperty(net.minecraft.world.level.block.FarmBlock.MOISTURE) ?
                            state.getValue(net.minecraft.world.level.block.FarmBlock.MOISTURE) : 0;
                    level.setBlock(pos, ModBlocks.FERTILIZED_FARMLAND.get().defaultBlockState()
                            .setValue(net.minecraft.world.level.block.FarmBlock.MOISTURE, moisture)
                            .setValue(io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.CHARGES, 3), 3);
                    level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 0.8f);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 12, 0.3, 0.1, 0.3, 0.05);
                    }
                    if (!player.isCreative()) {
                        held.shrink(1);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide));
                return;
            }

            // Click on a crop planted on Farmland
            if (isCrop(state.getBlock())) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.is(Blocks.FARMLAND) || (belowState.is(ModBlocks.FERTILIZED_FARMLAND.get()) && belowState.getValue(io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.CHARGES) < 3)) {
                    if (!level.isClientSide) {
                        int moisture = belowState.hasProperty(net.minecraft.world.level.block.FarmBlock.MOISTURE) ?
                                belowState.getValue(net.minecraft.world.level.block.FarmBlock.MOISTURE) : 0;
                        level.setBlock(belowPos, ModBlocks.FERTILIZED_FARMLAND.get().defaultBlockState()
                                .setValue(net.minecraft.world.level.block.FarmBlock.MOISTURE, moisture)
                                .setValue(io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.CHARGES, 3), 3);
                        level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 0.8f);
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.3, 0.2, 0.3, 0.05);
                        }
                        if (!player.isCreative()) {
                            held.shrink(1);
                        }
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide));
                    return;
                }
            }
        }

        // Sweet berry bush harvesting decrements fertilizer charges
        if (state.getBlock() instanceof net.minecraft.world.level.block.SweetBerryBushBlock &&
                state.getValue(net.minecraft.world.level.block.SweetBerryBushBlock.AGE) >= 2) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            if (belowState.is(ModBlocks.FERTILIZED_FARMLAND.get())) {
                BlockState nextState = io.marrybye.github.larperthanwolves.block.FertilizedFarmlandBlock.getAfterHarvestState(belowState);
                level.setBlock(belowPos, nextState, 3);
            }
        }
    }

    public static ItemStack getRandomWildCropSeed() {
        float roll = ThreadLocalRandom.current().nextFloat();
        if (roll < 0.50f) {
            return new ItemStack(Items.WHEAT_SEEDS, 1);
        } else if (roll < 0.65f) {
            return new ItemStack(Items.CARROT, 1);
        } else if (roll < 0.80f) {
            return new ItemStack(Items.POTATO, 1);
        } else if (roll < 0.90f) {
            return new ItemStack(Items.BEETROOT_SEEDS, 1);
        } else if (roll < 0.95f) {
            return new ItemStack(Items.PUMPKIN_SEEDS, 1);
        } else {
            return new ItemStack(Items.MELON_SEEDS, 1);
        }
    }
}
