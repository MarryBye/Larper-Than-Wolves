package io.marrybye.github.larperthanwolves.menu;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class SieveMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess levelAccess;

    public SieveMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(18), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public SieveMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, ContainerLevelAccess levelAccess) {
        super(ModMenuTypes.SIEVE.get(), containerId);
        checkContainerSize(container, 18);
        checkContainerDataCount(data, 2);

        this.container = container;
        this.data = data;
        this.levelAccess = levelAccess;

        container.startOpen(playerInventory.player);

        // 9 Input slots for Gravel (3x3 grid on left)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new GravelInputSlot(container, col + row * 3, 18 + col * 18, 17 + row * 18));
            }
        }

        // 9 Output slots for Sifted items (3x3 grid on right)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new OutputSlot(container, 9 + col + row * 3, 106 + col * 18, 17 + row * 18));
            }
        }

        // Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.addDataSlots(data);
    }

    public int getBurnProgress() {
        int processTime = this.data.get(0);
        int processTimeTotal = this.data.get(1);
        return processTimeTotal != 0 && processTime != 0 ? Math.clamp((int) Math.ceil((float) processTime * 24.0f / (float) processTimeTotal), 0, 24) : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            // From output slots (9..17)
            if (index >= 9 && index < 18) {
                if (!this.moveItemStackTo(stackInSlot, 18, 54, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stackInSlot, itemstack);
            }
            // From input slots (0..8)
            else if (index < 9) {
                if (!this.moveItemStackTo(stackInSlot, 18, 54, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // From player inventory (18..53)
            else {
                if (stackInSlot.is(Blocks.GRAVEL.asItem()) || stackInSlot.is(Blocks.SUSPICIOUS_GRAVEL.asItem())) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, ModBlocks.SIEVE.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public static class GravelInputSlot extends Slot {
        public GravelInputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(Blocks.GRAVEL.asItem()) || stack.is(Blocks.SUSPICIOUS_GRAVEL.asItem());
        }
    }

    public static class OutputSlot extends Slot {
        public OutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
