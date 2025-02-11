package dev.ftb.mods.pmapi.api.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemButton extends Button {
    private final ItemStack icon;

    @Nullable
    private final ItemStack hoverIcon;

    private int itemOffsetX = 2;
    private int itemOffsetY = 2;

    public ItemButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration, ItemStack icon) {
        this(x, y, width, height, message, onPress, createNarration, icon, null);
    }

    public ItemButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration, ItemStack icon, @Nullable ItemStack hoverIcon) {
        super(x, y, width, height, message, onPress, createNarration);
        this.icon = icon;
        this.hoverIcon = hoverIcon;
    }

    public ItemButton setItemOffset(int x, int y) {
        this.itemOffsetX = x;
        this.itemOffsetY = y;
        return this;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.renderItem(isHovered && hoverIcon != null ? hoverIcon : icon, this.getX() + itemOffsetX, this.getY() + itemOffsetY);
    }
}
