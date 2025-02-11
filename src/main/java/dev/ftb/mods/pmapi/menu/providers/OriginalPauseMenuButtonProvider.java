package dev.ftb.mods.pmapi.menu.providers;

import dev.ftb.mods.pmapi.api.menu.MenuLocation;
import dev.ftb.mods.pmapi.api.menu.PauseItemProvider;
import dev.ftb.mods.pmapi.api.menu.ScreenHolder;
import dev.ftb.mods.pmapi.menu.PauseMenuScreen;
import dev.ftb.mods.pmapi.api.menu.ScreenWidgetCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class OriginalPauseMenuButtonProvider implements PauseItemProvider {
    @Override
    public @Nullable ScreenWidgetCollection init(MenuLocation target, ScreenHolder screen, int x, int y) {
        ScreenWidgetCollection screenWidgetCollection = ScreenWidgetCollection.create();

        screenWidgetCollection.addRenderableWidget(Button.builder(Component.literal("Original"), btn -> {
            PauseMenuScreen.DISABLE_CUSTOM_PAUSE = true;
            Minecraft.getInstance().setScreen(new PauseScreen(true));
        }).width(70).pos(x, y).build());

        return screenWidgetCollection;
    }

    @Override
    public int width() {
        return 70;
    }

    @Override
    public int height() {
        return 20;
    }
}
