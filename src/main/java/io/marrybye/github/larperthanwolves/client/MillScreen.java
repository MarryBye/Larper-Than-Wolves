package io.marrybye.github.larperthanwolves.client;

import io.marrybye.github.larperthanwolves.menu.MillMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MillScreen extends AbstractContainerScreen<MillMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("larperthanwolves", "textures/gui/container/mill.png");

    public MillScreen(MillMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Animated progress arrow at (74, 34) (24x17)
        int progress = this.menu.getGrindProgress();
        if (progress > 0) {
            guiGraphics.blit(TEXTURE, x + 74, y + 34, 176, 14, progress, 17);
        }
    }
}
