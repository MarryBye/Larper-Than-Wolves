package io.marrybye.github.larperthanwolves.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FilterGrateMenu extends AbstractContainerMenu {
    public static final int FILTER_SLOTS = 9;
    private final Container container;

    public FilterGrateMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(FILTER_SLOTS));
    }

    public FilterGrateMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenuTypes.FILTER_GRATE.get(), containerId);
        checkContainerSize(container, FILTER_SLOTS);
        this.container = container;
        container.startOpen(playerInventory.player);

        // 3x3 Ghost/Phantom Filter Slots (centered at x=62, y=17)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(container, col + row * 3, 62 + col * 18, 17 + row * 18) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return true;
                    }
                });
            }
        }

        // 3x9 Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // 1x9 Player Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < FILTER_SLOTS) {
            ItemStack carried = getCarried();
            if (!carried.isEmpty()) {
                // Set ghost copy of carried item into filter slot
                this.container.setItem(slotId, carried.copyWithCount(1));
            } else {
                // Clear filter item when clicked with empty cursor
                this.container.setItem(slotId, ItemStack.EMPTY);
            }
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index >= FILTER_SLOTS) {
            Slot slot = this.slots.get(index);
            if (slot.hasItem()) {
                ItemStack stack = slot.getItem();
                // Copy item to first empty filter slot if possible
                for (int i = 0; i < FILTER_SLOTS; i++) {
                    if (this.container.getItem(i).isEmpty()) {
                        this.container.setItem(i, stack.copyWithCount(1));
                        break;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
