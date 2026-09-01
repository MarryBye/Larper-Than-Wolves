package io.marrybye.github.larperthanwolves.client;

import io.marrybye.github.larperthanwolves.menu.MithrilFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MithrilFurnaceScreen extends AbstractContainerScreen<MithrilFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("larperthanwolves", "textures/gui/container/advanced_smelter.png");

    public MithrilFurnaceScreen(MithrilFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        double modifier = this.menu.getEfficiencyModifier();
        int percent = this.menu.getEfficiencyPercent();
        String text = String.format(java.util.Locale.ROOT, "%.2fx", modifier);
        int color = percent > 100 ? 0x55FFFF : (percent == 100 ? 0x55FF55 : 0xFFAA00);
        int textWidth = this.font.width(text);
        guiGraphics.drawString(this.font, text, this.imageWidth - 8 - textWidth, this.titleLabelY, color, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = this.leftPos;
        int y = this.topPos;
        double modifier = this.menu.getEfficiencyModifier();
        int percent = this.menu.getEfficiencyPercent();
        String effText = String.format(java.util.Locale.ROOT, "%.2fx", modifier);
        int textWidth = this.font.width(effText);

        boolean hoverBadge = mouseX >= x + this.imageWidth - 12 - textWidth && mouseX <= x + this.imageWidth - 4 &&
                mouseY >= y + 4 && mouseY <= y + 16;
        boolean hoverFlame = mouseX >= x + 84 && mouseX <= x + 98 && mouseY >= y + 54 && mouseY <= y + 68;
        boolean hoverArrow = mouseX >= x + 79 && mouseX <= x + 103 && mouseY >= y + 34 && mouseY <= y + 50;

        if (hoverBadge || hoverFlame || hoverArrow) {
            java.util.List<Component> tooltip = new java.util.ArrayList<>();
            tooltip.add(Component.translatable("gui.larperthanwolves.efficiency.title", effText).withStyle(net.minecraft.ChatFormatting.GOLD, net.minecraft.ChatFormatting.BOLD));
            tooltip.add(Component.translatable("gui.larperthanwolves.efficiency.speed_desc", percent).withStyle(net.minecraft.ChatFormatting.GRAY));

            int cookTimeTotal = this.menu.getCookTimeTotal();
            if (cookTimeTotal > 0) {
                float seconds = cookTimeTotal / 20.0f;
                tooltip.add(Component.translatable("gui.larperthanwolves.efficiency.cook_time", String.format(java.util.Locale.ROOT, "%.1fs", seconds), cookTimeTotal).withStyle(net.minecraft.ChatFormatting.YELLOW));
            }
            tooltip.add(Component.translatable("gui.larperthanwolves.efficiency.burn_unaffected").withStyle(net.minecraft.ChatFormatting.DARK_GREEN));

            if (this.menu.isLit()) {
                int burnTime = this.menu.getBurnTime();
                float burnSeconds = burnTime / 20.0f;
                tooltip.add(Component.translatable("gui.larperthanwolves.efficiency.remaining_burn", String.format(java.util.Locale.ROOT, "%.1fs", burnSeconds)).withStyle(net.minecraft.ChatFormatting.AQUA));
            }

            guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Animated burning flame at (84, 54)
        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            if (litProgress > 0) {
                guiGraphics.blit(TEXTURE, x + 84, y + 54 + 14 - litProgress, 176, 14 - litProgress, 14, litProgress);
            }
        }

        // Animated cooking arrow at (79, 34)
        int cookProgress = this.menu.getBurnProgress();
        if (cookProgress > 0) {
            guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, cookProgress, 16);
        }
    }
}
