package io.marrybye.github.larperthanwolves.menu;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class AlloyMixerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess levelAccess;

    public AlloyMixerMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(4), new SimpleContainerData(4), ContainerLevelAccess.NULL);
    }

    public AlloyMixerMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, ContainerLevelAccess levelAccess) {
        super(ModMenuTypes.ALLOY_MIXER.get(), containerId);
        checkContainerSize(container, 4);
        checkContainerDataCount(data, 4);

        this.container = container;
        this.data = data;
        this.levelAccess = levelAccess;

        container.startOpen(playerInventory.player);

        // 3 Mixing inputs on left
        this.addSlot(new Slot(container, 0, 44, 17));
        this.addSlot(new Slot(container, 1, 44, 35));
        this.addSlot(new Slot(container, 2, 44, 53));

        // Output slot on right
        this.addSlot(new OutputSlot(container, 3, 116, 35));

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

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public int getLitProgress() {
        int burnTime = this.data.get(0);
        int maxBurnTime = this.data.get(1);
        if (maxBurnTime <= 0) maxBurnTime = 200;
        return Math.clamp((int) Math.ceil((float) burnTime * 14.0f / (float) maxBurnTime), 0, 14);
    }

    public int getBurnProgress() {
        int cookTime = this.data.get(2);
        int cookTimeTotal = this.data.get(3);
        return cookTimeTotal != 0 && cookTime != 0 ? Math.clamp((int) Math.ceil((float) cookTime * 24.0f / (float) cookTimeTotal), 0, 24) : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            // From output (3)
            if (index == 3) {
                if (!this.moveItemStackTo(stackInSlot, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stackInSlot, itemstack);
            }
            // From inputs (0..2)
            else if (index >= 0 && index <= 2) {
                if (!this.moveItemStackTo(stackInSlot, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // From player inventory (4..39)
            else {
                if (!this.moveItemStackTo(stackInSlot, 0, 3, false)) {
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
        return stillValid(this.levelAccess, player, ModBlocks.ALLOY_MIXER.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
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
