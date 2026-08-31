package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.compat.CreateCompatHelper;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.SieveMenu;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SieveBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    // 0..8: 9 Input slots for Gravel/Sand/Suspicious blocks, 9..17: 9 Output slots for sifted items
    public static final int TOTAL_SLOTS = 18;
    public static final int SHAKES_PER_BLOCK = 5;

    private final NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);

    private int shakeProgress = 0; // 0..5 shakes per block
    private int shakeCooldown = 0; // 0..10 ticks (0.5s) lock between manual shakes
    private float kineticBuffer = 0.0f;
    private boolean isKineticActive = false;
    private int kineticSoundTimer = 0;

    // Client-side animation
    public int clientTicks = 0;
    public int clientShakeTimer = 0;
    public int clientAnimationTicks = 0;

    private static final Random RANDOM = new Random();

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIEVE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.larperthanwolves.sieve");
    }

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> SieveBlockEntity.this.shakeProgress * 20; // 0..100% progress
                case 1 -> 100;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                SieveBlockEntity.this.shakeProgress = value / 20;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SieveMenu(containerId, playerInventory, this, this.dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("ShakeProgress", this.shakeProgress);
        tag.putInt("ShakeCooldown", this.shakeCooldown);
        tag.putBoolean("IsKineticActive", this.isKineticActive);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.shakeProgress = tag.getInt("ShakeProgress");
        this.shakeCooldown = tag.getInt("ShakeCooldown");
        this.isKineticActive = tag.getBoolean("IsKineticActive");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("ShakeProgress", this.shakeProgress);
        tag.putInt("ShakeCooldown", this.shakeCooldown);
        tag.putBoolean("IsKineticActive", this.isKineticActive);
        int slot = findFirstSiftableSlot();
        tag.putInt("InputSlot", slot);
        if (slot != -1) {
            tag.put("ActiveInput", this.items.get(slot).saveOptional(registries));
        }
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.shakeProgress = tag.getInt("ShakeProgress");
            this.isKineticActive = tag.getBoolean("IsKineticActive");
            int cooldown = tag.getInt("ShakeCooldown");
            if (cooldown > 0) {
                this.clientShakeTimer = cooldown;
            }
        }
    }

    public static boolean isSiftable(ItemStack stack) {
        return stack.is(Blocks.GRAVEL.asItem()) ||
                stack.is(Blocks.SUSPICIOUS_GRAVEL.asItem()) ||
                stack.is(Blocks.SAND.asItem()) ||
                stack.is(Blocks.RED_SAND.asItem()) ||
                stack.is(Blocks.SUSPICIOUS_SAND.asItem()) ||
                stack.is(Blocks.DIRT.asItem()) ||
                stack.is(Blocks.GRASS_BLOCK.asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_GRASS_BLOCK.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_DIRT.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_GRAVEL.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_SAND.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_RED_SAND.get().asItem());
    }

    public static boolean isRichSoil(ItemStack stack) {
        return stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_GRASS_BLOCK.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_DIRT.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_GRAVEL.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_SAND.get().asItem()) ||
                stack.is(io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_RED_SAND.get().asItem());
    }

    public int findFirstSiftableSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty() && isSiftable(stack)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isShaking() {
        return this.clientShakeTimer > 0 || this.isKineticActive;
    }

    public float getInterpolatedOffset(float partialTick) {
        if (this.isKineticActive) {
            return (float) Math.sin((this.clientTicks + partialTick) * 1.6f) * 0.05f;
        }
        if (this.clientShakeTimer > 0) {
            float elapsed = (10 - this.clientShakeTimer) + partialTick;
            float damp = Math.max(0.0f, (this.clientShakeTimer - partialTick) / 10.0f);
            return (float) Math.sin(elapsed * 1.6f) * 0.06f * damp;
        }
        return 0.0f;
    }

    /**
     * Manual Shift + Right-Click sifting action.
     * Takes 5 manual shakes with a 0.5s (10-tick) cooldown between shakes to sift 1 block.
     */
    public InteractionResult performManualShake(Player player, Level level, BlockPos pos) {
        int inputSlot = findFirstSiftableSlot();
        if (inputSlot == -1) {
            // No siftable items in sieve!
            return InteractionResult.PASS;
        }

        if (this.shakeCooldown > 0) {
            // Still on 0.5s cooldown from previous shake
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        this.shakeCooldown = 10;
        this.shakeProgress++;

        if (!level.isClientSide) {
            triggerShakeEffects(level, pos, inputSlot);
            if (this.shakeProgress >= SHAKES_PER_BLOCK) {
                this.shakeProgress = 0;
                processSifting(inputSlot);
                if (level instanceof ServerLevel sl) {
                    sl.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6f, 1.2f);
                }
            }
            setChanged(level, pos, getBlockState());
            level.sendBlockUpdated(pos, getBlockState(), getBlockState(), 3);
        } else {
            this.clientShakeTimer = 10;
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SieveBlockEntity sieve) {
        if (sieve.shakeCooldown > 0) {
            sieve.shakeCooldown--;
        }

        int inputSlot = sieve.findFirstSiftableSlot();
        if (inputSlot == -1) {
            if (sieve.isKineticActive || sieve.shakeProgress != 0) {
                sieve.isKineticActive = false;
                sieve.shakeProgress = 0;
                sieve.kineticBuffer = 0.0f;
                sieve.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return;
        }

        // Automated Rotational Sifting via Create integration (strictly through side axle drive sockets)
        if (ModList.get().isLoaded("create")) {
            Direction facing = state.hasProperty(io.marrybye.github.larperthanwolves.block.SieveBlock.FACING)
                    ? state.getValue(io.marrybye.github.larperthanwolves.block.SieveBlock.FACING) : Direction.NORTH;
            Direction[] sideSockets = new Direction[] { facing.getClockWise(), facing.getCounterClockWise() };
            float speed = CreateCompatHelper.getKineticSpeed(level, pos, sideSockets);
            if (speed > 0.0f) {
                sieve.isKineticActive = true;
                // 16 RPM = 0.5 progress per tick (1 shake every 10 ticks)
                // 64 RPM = 2.0 progress per tick
                // 256 RPM = 8.0 progress per tick
                float progressPerTick = speed / 32.0f;
                sieve.kineticBuffer += progressPerTick;

                sieve.kineticSoundTimer++;
                if (sieve.kineticSoundTimer >= 10) {
                    sieve.kineticSoundTimer = 0;
                    sieve.triggerShakeEffects(level, pos, inputSlot);
                }

                if (sieve.kineticBuffer >= 1.0f) {
                    int shakes = (int) sieve.kineticBuffer;
                    sieve.kineticBuffer -= shakes;
                    sieve.shakeProgress += shakes;

                    while (sieve.shakeProgress >= SHAKES_PER_BLOCK) {
                        sieve.shakeProgress -= SHAKES_PER_BLOCK;
                        inputSlot = sieve.findFirstSiftableSlot();
                        if (inputSlot != -1) {
                            sieve.processSifting(inputSlot);
                        } else {
                            sieve.shakeProgress = 0;
                            break;
                        }
                    }
                    sieve.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            } else {
                if (sieve.isKineticActive) {
                    sieve.isKineticActive = false;
                    sieve.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                sieve.kineticBuffer = 0.0f;
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SieveBlockEntity sieve) {
        sieve.clientTicks++;
        if (sieve.clientShakeTimer > 0) {
            sieve.clientShakeTimer--;
        }
    }

    private void triggerShakeEffects(Level level, BlockPos pos, int inputSlot) {
        ItemStack inputStack = this.items.get(inputSlot);
        Block inputBlock = Block.byItem(inputStack.getItem());
        BlockState particleState = inputBlock != Blocks.AIR ? inputBlock.defaultBlockState() : Blocks.GRAVEL.defaultBlockState();

        if (level instanceof ServerLevel sl) {
            sl.playSound(null, pos, SoundEvents.SAND_HIT, SoundSource.BLOCKS, 0.7f, 0.9f + sl.random.nextFloat() * 0.2f);
            sl.playSound(null, pos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 0.4f, 1.2f + sl.random.nextFloat() * 0.3f);
            sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, particleState),
                    pos.getX() + 0.5, pos.getY() + 0.85, pos.getZ() + 0.5,
                    8, 0.25, 0.05, 0.25, 0.05);
        }
    }

    private void processSifting(int inputSlot) {
        ItemStack input = items.get(inputSlot);
        if (input.isEmpty()) return;

        boolean isSuspicious = input.is(Blocks.SUSPICIOUS_GRAVEL.asItem()) || input.is(Blocks.SUSPICIOUS_SAND.asItem());
        boolean isSuspiciousSand = input.is(Blocks.SUSPICIOUS_SAND.asItem());
        boolean isSuspiciousGravel = input.is(Blocks.SUSPICIOUS_GRAVEL.asItem());
        boolean isRich = isRichSoil(input);

        CustomData blockEntityData = isSuspicious ? input.get(DataComponents.BLOCK_ENTITY_DATA) : null;

        input.shrink(1);

        ItemStack regularResult = ItemStack.EMPTY;
        float roll = ThreadLocalRandom.current().nextFloat();
        double cumulative = 0.0;

        if (isRich) {
            // Rich Soils: Pure natural metal dusts ONLY (Copper Dust -> Tin Dust -> Iron Dust -> Gold Dust -> Diamond Dust)
            double copperChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveRichCopperDustChance.get() : 0.55;
            double tinChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveRichTinDustChance.get() : 0.32;
            double ironChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveRichIronDustChance.get() : 0.15;
            double goldChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveRichGoldDustChance.get() : 0.08;
            double diamondChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveRichDiamondDustChance.get() : 0.03;

            if (roll < (cumulative += copperChance)) {
                regularResult = new ItemStack(ModItems.COPPER_DUST.get(), 1);
            } else if (roll < (cumulative += tinChance)) {
                regularResult = new ItemStack(ModItems.TIN_DUST.get(), 1);
            } else if (roll < (cumulative += ironChance)) {
                regularResult = new ItemStack(ModItems.IRON_DUST.get(), 1);
            } else if (roll < (cumulative += goldChance)) {
                regularResult = new ItemStack(ModItems.GOLD_DUST.get(), 1);
            } else if (roll < (cumulative += diamondChance)) {
                regularResult = new ItemStack(ModItems.DIAMOND_DUST.get(), 1);
            }
        } else {
            // Standard Soils (Gravel, Sand, Red Sand, Dirt, Suspicious): Silicon Shard -> Flint -> Copper Dust
            double siliconChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveSiliconShardChance.get() : 0.40;
            double flintChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveFlintChance.get() : 0.22;
            double copperChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveCopperDustChance.get() : 0.08;

            if (roll < (cumulative += siliconChance)) {
                regularResult = new ItemStack(ModItems.SILICON_SHARD.get(), 1);
            } else if (roll < (cumulative += flintChance)) {
                regularResult = new ItemStack(Items.FLINT, 1);
            } else if (roll < (cumulative += copperChance)) {
                regularResult = new ItemStack(ModItems.COPPER_DUST.get(), 1);
            }
        }

        if (!regularResult.isEmpty()) {
            insertOutput(regularResult);
        }

        // 2. Suspicious Block Archaeology drops
        if (isSuspicious && this.level instanceof ServerLevel serverLevel) {
            ResourceKey<LootTable> lootTableKey = null;

            if (blockEntityData != null && blockEntityData.contains("LootTable")) {
                String lootTableStr = blockEntityData.copyTag().getString("LootTable");
                if (!lootTableStr.isEmpty()) {
                    try {
                        lootTableKey = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(lootTableStr));
                    } catch (Exception ignored) {}
                }
            }

            if (lootTableKey == null) {
                if (isSuspiciousSand) {
                    ResourceKey<LootTable>[] sandTables = new ResourceKey[]{
                            BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
                            BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY,
                            BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY
                    };
                    lootTableKey = sandTables[RANDOM.nextInt(sandTables.length)];
                } else if (isSuspiciousGravel) {
                    ResourceKey<LootTable>[] gravelTables = new ResourceKey[]{
                            BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON,
                            BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE,
                            BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY
                    };
                    lootTableKey = gravelTables[RANDOM.nextInt(gravelTables.length)];
                }
            }

            if (lootTableKey != null) {
                try {
                    LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);
                    LootParams params = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
                            .create(LootContextParamSets.ARCHAEOLOGY);
                    ObjectArrayList<ItemStack> archDrops = lootTable.getRandomItems(params);
                    for (ItemStack drop : archDrops) {
                        if (!drop.isEmpty() && !DisabledItemsHandler.isDisabled(drop.getItem())) {
                            insertOutput(drop.copy());
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    private void insertOutput(ItemStack stack) {
        for (int i = 9; i < 18; i++) {
            ItemStack slotStack = items.get(i);
            if (ItemStack.isSameItemSameComponents(slotStack, stack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
                int add = Math.min(stack.getCount(), slotStack.getMaxStackSize() - slotStack.getCount());
                slotStack.grow(add);
                stack.shrink(add);
                if (stack.isEmpty()) return;
            }
        }

        for (int i = 9; i < 18; i++) {
            ItemStack slotStack = items.get(i);
            if (slotStack.isEmpty()) {
                items.set(i, stack.copy());
                return;
            }
        }

        // Drop in world if all output slots are completely full
        if (!stack.isEmpty() && this.level != null && !this.level.isClientSide) {
            Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, stack);
        }
    }

    private static final int[] SLOTS_INPUT = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] SLOTS_OUTPUT = new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17};

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_OUTPUT;
        }
        return SLOTS_INPUT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return index < 9 && isSiftable(itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= 9;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) if (!stack.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}
