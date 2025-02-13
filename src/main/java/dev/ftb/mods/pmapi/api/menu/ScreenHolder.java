package dev.ftb.mods.pmapi.api.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;

import java.util.Collections;
import java.util.List;

/**
 * Wraps a screen to provide read-only access to its internals.
 */
public class ScreenHolder {
    private final Screen screen;

    private ScreenHolder(Screen screen) {
        this.screen = screen;
    }

    public static ScreenHolder of(Screen screen) {
        return new ScreenHolder(screen);
    }

    /**
     * Please do not use this method unless you absolutely have to. This is only provided for screens that require it
     * to return back to the previous screen.
     *
     * @return the wrapped screen
     */
    public final Screen unsafeScreenAccess() {
        return this.screen;
    }

    public List<Renderable> renderables() {
        return Collections.unmodifiableList(this.screen.renderables);
    }

    public List<NarratableEntry> narratables() {
        return Collections.unmodifiableList(this.screen.narratables);
    }

    public List<GuiEventListener> children() {
        return Collections.unmodifiableList(this.screen.children);
    }

    public int getWidth() {
        return screen.width;
    }

    public int getHeight() {
        return screen.height;
    }

    public Font font() {
        return Minecraft.getInstance().font;
    }

    public ItemRenderer itemRenderer() {
        return Minecraft.getInstance().getItemRenderer();
    }
}
