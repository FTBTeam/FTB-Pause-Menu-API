package dev.ftb.mods.pmapi.api.menu;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

/**
 * @implNote Please do NOT try and modify the screen. This api is provided to avoid nasty hacks
 *           and mutations of the original screen which has caused many issues in the past.
 */
public interface PauseItemProvider {
    /**
     * Initialize the additional pause screen
     *
     * @param target the target this provider is being initialized for
     * @param screen a read-only screen delegate
     */
    @Nullable
    default ScreenWidgetCollection init(MenuLocation target, ScreenHolder screen, int x, int y) {
        return null;
    }

    /**
     * Render the additional pause screen
     *
     * @param target the target this provider is being rendered for
     * @param screen the read-only screen
     * @param guiGraphics the gui graphics context
     * @param x the x position relative to the position this provider has been added to
     * @param y the y position relative to the position this provider has been added to
     * @param partialTicks the partial ticks
     */
    default void render(MenuLocation target, ScreenHolder screen, GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // NO-OP
    }

    /**
     * Helper method for optimizing render calls, if returned true, the render method will be called,
     * otherwise the render method will be skipped.
     */
    default boolean hasRender() {
        return false;
    }

    /**
     * Used to calculate the total amount of width that your elements require.
     *
     * @return Your items total width requirement. If you have one button, this might be 120 for example.
     */
    default int width() {
        return 0;
    }

    /**
     * Used to calculate spacing when item wrapping is needed.
     *
     * @return Your items total height requirement. If you have one button, this might be 20 for example.
     * @implNote Padding is handled for you, please do not include padding in this calculation.
     */
    default int height() {
        return 0;
    }

    /**
     * Checks if your item should render, this can be helpful if you have a config that
     * might conditionally disable your item.
     */
    default boolean disabled() {
        return false;
    }

    /**
     * Determines your items place in the rendering order. Higher numbers will render first,
     * lower numbers will render last.
     *
     * @return The sorting order of your item.
     * @implNote Default is 0, if you don't need to change the order, you can leave this as is.
     */
    default int sortingOrder() {
        return 0;
    }
}
