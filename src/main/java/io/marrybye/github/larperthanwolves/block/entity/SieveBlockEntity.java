package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.SieveMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SieveBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    // 0..8: 9 Input slots for Gravel, 9..17: 9 Output slots for sifted dusts/shards/flint
    private final NonNullList<ItemStack> items = NonNullList.withSize(18, ItemStack.EMPTY);

    private int processTime = 0;
    private int processTimeTotal = 100;
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
                case 0 -> SieveBlockEntity.this.processTime;
                case 1 -> SieveBlockEntity.this.processTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> SieveBlockEntity.this.processTime = value;
                case 1 -> SieveBlockEntity.this.processTimeTotal = value;
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
        tag.putInt("ProcessTime", this.processTime);
        tag.putInt("ProcessTimeTotal", this.processTimeTotal);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.processTime = tag.getInt("ProcessTime");
        this.processTimeTotal = tag.getInt("ProcessTimeTotal");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SieveBlockEntity entity) {
        if (level.isClientSide) return;

        int configuredTotal = ModConfig.SERVER != null ? ModConfig.SERVER.sieveProcessTimeTicks.get() : 100;
        entity.processTimeTotal = configuredTotal > 0 ? configuredTotal : 100;

        int gravelSlot = entity.findFirstGravelSlot();
        if (gravelSlot != -1) {
            entity.processTime++;
            if (entity.processTime >= entity.processTimeTotal) {
                entity.processSifting(gravelSlot);
                entity.processTime = 0;
                setChanged(level, pos, state);
            }
        } else {
            if (entity.processTime > 0) {
                entity.processTime = 0;
                setChanged(level, pos, state);
            }
        }
    }

    private int findFirstGravelSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty() && (stack.is(Blocks.GRAVEL.asItem()) || stack.is(Blocks.SUSPICIOUS_GRAVEL.asItem()))) {
                return i;
            }
        }
        return -1;
    }

    private void processSifting(int inputSlot) {
        ItemStack input = items.get(inputSlot);
        if (input.isEmpty()) return;

        input.shrink(1);

        double copperChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveCopperDustChance.get() : 0.15;
        double ironChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveIronDustChance.get() : 0.08;
        double goldChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveGoldDustChance.get() : 0.02;
        double siliconChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveSiliconShardChance.get() : 0.15;
        double flintChance = ModConfig.SERVER != null ? ModConfig.SERVER.sieveFlintChance.get() : 0.20;

        float roll = RANDOM.nextFloat();
        ItemStack result = ItemStack.EMPTY;

        if (roll < copperChance) {
            result = new ItemStack(ModItems.COPPER_DUST.get(), 1);
        } else if (roll < copperChance + ironChance) {
            result = new ItemStack(ModItems.IRON_DUST.get(), 1);
        } else if (roll < copperChance + ironChance + goldChance) {
            result = new ItemStack(ModItems.GOLD_DUST.get(), 1);
        } else if (roll < copperChance + ironChance + goldChance + siliconChance) {
            result = new ItemStack(ModItems.SILICON_SHARD.get(), 1);
        } else if (roll < copperChance + ironChance + goldChance + siliconChance + flintChance) {
            result = new ItemStack(Items.FLINT, 1);
        }

        if (!result.isEmpty()) {
            insertOutput(result);
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
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17};
        }
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return index < 9 && (itemStack.is(Blocks.GRAVEL.asItem()) || itemStack.is(Blocks.SUSPICIOUS_GRAVEL.asItem()));
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
