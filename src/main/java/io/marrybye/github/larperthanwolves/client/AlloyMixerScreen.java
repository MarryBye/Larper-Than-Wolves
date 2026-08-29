package io.marrybye.github.larperthanwolves.client;

import io.marrybye.github.larperthanwolves.menu.AlloyMixerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlloyMixerScreen extends AbstractContainerScreen<AlloyMixerMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("larperthanwolves", "textures/gui/container/alloy_mixer.png");

    public AlloyMixerScreen(AlloyMixerMenu menu, Inventory playerInventory, Component title) {
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

        // Animated burning flame at (80, 48)
        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            if (litProgress > 0) {
                guiGraphics.blit(TEXTURE, x + 80, y + 48 + 14 - litProgress, 176, 14 - litProgress, 14, litProgress);
            }
        }

        // Animated mixing progress arrow at (79, 24)
        int cookProgress = this.menu.getBurnProgress();
        if (cookProgress > 0) {
            guiGraphics.blit(TEXTURE, x + 79, y + 24, 176, 14, cookProgress, 17);
        }
    }
}
