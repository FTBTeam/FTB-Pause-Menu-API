package dev.ftb.mods.pmapi.menu.providers;

import dev.ftb.mods.pmapi.PauseMenuMod;
import dev.ftb.mods.pmapi.api.PauseMenuApi;
import dev.ftb.mods.pmapi.api.menu.PauseItemProvider;
import dev.ftb.mods.pmapi.api.menu.MenuLocation;
import dev.ftb.mods.pmapi.api.menu.ScreenHolder;
import dev.ftb.mods.pmapi.api.menu.ScreenWidgetCollection;
import dev.ftb.mods.pmapi.api.widgets.IconButton;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SupportPauseItemProvider implements PauseItemProvider {
    private static final ResourceLocation DISCORD_ICON = ResourceLocation.fromNamespaceAndPath(PauseMenuApi.MOD_ID, "textures/gui/discord.png");
    private static final ResourceLocation GITHUB_ICON = ResourceLocation.fromNamespaceAndPath(PauseMenuApi.MOD_ID, "textures/gui/github.png");

    @Override
    public @Nullable ScreenWidgetCollection init(MenuLocation target, ScreenHolder screen, int x, int y) {
        ScreenWidgetCollection screenWidgetCollection = ScreenWidgetCollection.create();

        String githubUrl = PauseMenuMod.config().githubLink.get();
        String discordUrl = PauseMenuMod.config().discordLink.get();

        int xOffset = x;
        if (!discordUrl.isEmpty()) {
            screenWidgetCollection.addRenderableWidget(new IconButton(xOffset, y, 20, 20, DISCORD_ICON, Component.translatable("ftbpmapi.tooltip.support_discord"), button -> {
                Util.getPlatform().openUri(discordUrl);
            }, Supplier::get));

            xOffset += 24;
        }

        if (!githubUrl.isEmpty()) {
            screenWidgetCollection.addRenderableWidget(new IconButton(xOffset, y, 20, 20, GITHUB_ICON, Component.translatable("ftbpmapi.tooltip.support_github"), button -> {
                Util.getPlatform().openUri(githubUrl);
            }, Supplier::get));
        }

        return screenWidgetCollection;
    }

    @Override
    public int width() {
        if (PauseMenuMod.config().githubLink.get().isEmpty() || PauseMenuMod.config().discordLink.get().isEmpty()) {
            return 20; // One button width because only one button is present
        }

        return 20 + 20 + 4; // Button width + padding
    }

    @Override
    public int height() {
        return 20; // Button height
    }

    @Override
    public boolean disabled() {
        return PauseMenuMod.config().githubLink.get().isEmpty() && PauseMenuMod.config().discordLink.get().isEmpty();
    }
}
