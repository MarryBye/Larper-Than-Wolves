package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.menu.MillMenu;
import io.marrybye.github.larperthanwolves.recipe.MillRecipe;
import io.marrybye.github.larperthanwolves.recipe.MillRegistry;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import io.marrybye.github.larperthanwolves.api.IKineticReceiver;
import java.util.List;
import java.util.Optional;

public class MillBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider, IKineticReceiver {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT_1 = 1;
    public static final int SLOT_OUTPUT_2 = 2;
    public static final int SLOT_OUTPUT_3 = 3;
    public static final int TOTAL_SLOTS = 4;
    public static final int MAX_PROGRESS = 100;

    private NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    private int progress = 0;
    private float kineticBuffer = 0.0f;
    private int particleTimer = 0;

    public static void serverTick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, MillBlockEntity mill) {
        if (!mill.canGrind()) {
            if (mill.progress != 0) {
                mill.progress = 0;
                mill.setChanged();
            }
            mill.kineticBuffer = 0.0f;
            return;
        }

        // Support kinetic rotational force automation if Create is installed (exclusively through top face)
        if (net.neoforged.fml.ModList.get().isLoaded("create")) {
            float speed = io.marrybye.github.larperthanwolves.compat.CreateCompatHelper.getKineticSpeedForReceiver(level, pos, mill);
            if (speed > 0.0f) {
                mill.tickKineticRotation(speed, Direction.UP);
            } else {
                mill.kineticBuffer = 0.0f;
            }
        }
    }

    @Override
    public boolean acceptsKineticRotationFrom(Direction face) {
        return face == Direction.UP;
    }

    @Override
    public boolean hasWorkAvailable() {
        return canGrind();
    }

    @Override
    public boolean onManualCrank(Direction fromFace, Player player) {
        if (!canGrind()) return false;

        ItemStack inputBefore = getItem(SLOT_INPUT).copy();
        addGrindProgress(5);

        if (this.progress == 0 && (getItem(SLOT_INPUT).getCount() < inputBefore.getCount() || getItem(SLOT_INPUT).isEmpty())) {
            if (this.level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.playSound(null, this.worldPosition, net.minecraft.sounds.SoundEvents.ITEM_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 0.8f, 1.3f);
                serverLevel.playSound(null, this.worldPosition, net.minecraft.sounds.SoundEvents.GRINDSTONE_USE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 0.85f);
                serverLevel.sendParticles(new net.minecraft.core.particles.ItemParticleOption(net.minecraft.core.particles.ParticleTypes.ITEM, inputBefore),
                        this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.9D, this.worldPosition.getZ() + 0.5D,
                        12, 0.2D, 0.1D, 0.2D, 0.05D);
            }
        }
        return true;
    }

    @Override
    public void tickKineticRotation(float speed, Direction fromFace) {
        if (!canGrind() || speed <= 0.0f) {
            this.kineticBuffer = 0.0f;
            return;
        }

        // 16 RPM (Hand Crank) = 0.5 progress/tick (10 ticks per 5%)
        // 64 RPM = 2.0 progress/tick
        // 256 RPM = 8.0 progress/tick
        float progressPerTick = speed / 32.0f;
        this.kineticBuffer += progressPerTick;

        if (this.kineticBuffer >= 1.0f) {
            int toAdd = (int) this.kineticBuffer;
            this.kineticBuffer -= toAdd;
            addGrindProgress(toAdd);
        }

        this.particleTimer++;
        if (this.particleTimer >= 10) {
            this.particleTimer = 0;
            if (this.level != null) {
                this.level.playSound(null, this.worldPosition, net.minecraft.sounds.SoundEvents.GRINDSTONE_USE, net.minecraft.sounds.SoundSource.BLOCKS, 0.4f, 1.1f + this.level.random.nextFloat() * 0.2f);
            }
        }
    }

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> MillBlockEntity.this.progress;
                case 1 -> MAX_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                MillBlockEntity.this.progress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public MillBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MILL.get(), pos, state);
    }

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    public int getProgress() {
        return progress;
    }

    public boolean canGrind() {
        ItemStack input = items.get(SLOT_INPUT);
        if (input.isEmpty()) return false;

        Optional<MillRecipe> recipeOpt = MillRegistry.findMatchingRecipe(input);
        if (recipeOpt.isEmpty()) return false;

        MillRecipe recipe = recipeOpt.get();
        List<ItemStack> results = recipe.getResults();
        if (results.isEmpty()) return false;

        return canFitOutputs(results);
    }

    private boolean canFitOutputs(List<ItemStack> results) {
        // Create a simulation copy of output slots
        NonNullList<ItemStack> sim = NonNullList.withSize(3, ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) {
            sim.set(i, items.get(SLOT_OUTPUT_1 + i).copy());
        }

        for (ItemStack result : results) {
            ItemStack toInsert = result.copy();
            for (int i = 0; i < 3; i++) {
                ItemStack slotStack = sim.get(i);
                if (slotStack.isEmpty()) {
                    sim.set(i, toInsert);
                    toInsert = ItemStack.EMPTY;
                    break;
                } else if (ItemStack.isSameItemSameComponents(slotStack, toInsert)) {
                    int space = slotStack.getMaxStackSize() - slotStack.getCount();
                    int move = Math.min(space, toInsert.getCount());
                    slotStack.grow(move);
                    toInsert.shrink(move);
                    if (toInsert.isEmpty()) break;
                }
            }
            if (!toInsert.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean addGrindProgress(int amount) {
        if (!canGrind()) {
            if (progress != 0) {
                progress = 0;
                setChanged();
            }
            return false;
        }

        progress += amount;
        if (progress >= MAX_PROGRESS) {
            finishGrinding();
        } else {
            setChanged();
        }
        return true;
    }

    public void finishGrinding() {
        ItemStack input = items.get(SLOT_INPUT);
        Optional<MillRecipe> recipeOpt = MillRegistry.findMatchingRecipe(input);
        if (recipeOpt.isPresent()) {
            MillRecipe recipe = recipeOpt.get();
            List<ItemStack> results = recipe.getResults();

            input.shrink(recipe.getInputCount());

            for (ItemStack res : results) {
                insertIntoOutputSlots(res);
            }
        }
        progress = 0;
        setChanged();
    }

    private void insertIntoOutputSlots(ItemStack stack) {
        for (int i = SLOT_OUTPUT_1; i <= SLOT_OUTPUT_3; i++) {
            ItemStack existing = items.get(i);
            if (existing.isEmpty()) {
                items.set(i, stack.copy());
                return;
            } else if (ItemStack.isSameItemSameComponents(existing, stack)) {
                int space = existing.getMaxStackSize() - existing.getCount();
                int move = Math.min(space, stack.getCount());
                existing.grow(move);
                stack.shrink(move);
                if (stack.isEmpty()) return;
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return new int[]{SLOT_OUTPUT_1, SLOT_OUTPUT_2, SLOT_OUTPUT_3};
        }
        return new int[]{SLOT_INPUT};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return slot == SLOT_INPUT && MillRegistry.isValidInput(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot >= SLOT_OUTPUT_1 && slot <= SLOT_OUTPUT_3;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.larperthanwolves.mill");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MillMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public int getContainerSize() {
        return TOTAL_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
            if (slot == SLOT_INPUT && !canGrind()) {
                progress = 0;
            }
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if (slot == SLOT_INPUT && !canGrind()) {
            progress = 0;
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
        progress = 0;
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.progress = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", this.progress);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }
}
