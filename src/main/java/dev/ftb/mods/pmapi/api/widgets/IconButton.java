package dev.ftb.mods.pmapi.api.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Similar to {@link net.minecraft.client.gui.components.ImageButton} but a bit lazier
 * as it only takes a {@link ResourceLocation} for the icon
 */
public class IconButton extends Button {
    private final ResourceLocation icon;

    public IconButton(int x, int y, int width, int height, ResourceLocation icon, Component tooltip, OnPress onPress, CreateNarration narration) {
        super(x, y, width, height, Component.empty(), onPress, narration);
        this.icon = icon;
        setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(this.getX() + 3, this.getY() + 3, 0);
        pose.scale(.85f, .85f, 0);
        guiGraphics.blit(icon, 0, 0, 0, 0, 16, 16, 16, 16);
        pose.popPose();
    }
}
