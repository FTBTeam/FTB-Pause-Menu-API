package dev.ftb.mods.pmapi.integrations;

import de.keksuccino.justzoom.OptionsScreen;
import dev.ftb.mods.pmapi.api.PauseMenuApi;
import dev.ftb.mods.pmapi.api.menu.MenuLocation;
import dev.ftb.mods.pmapi.api.menu.PauseItemProvider;
import dev.ftb.mods.pmapi.api.menu.ScreenHolder;
import dev.ftb.mods.pmapi.api.menu.ScreenWidgetCollection;
import dev.ftb.mods.pmapi.api.widgets.ItemButton;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class JustZoomIntegration {
    public static void init() {
        PauseMenuApi.get().registerPauseItem(MenuLocation.TOP_RIGHT, new JustZoomItemProvider());
    }

    private static class JustZoomItemProvider implements PauseItemProvider {
        @Override
        public @Nullable ScreenWidgetCollection init(MenuLocation target, ScreenHolder screen, int x, int y) {
            var collection = ScreenWidgetCollection.create();

            collection.addRenderableWidget(new ItemButton(x, y, 20, 20, Component.empty(), btn -> {
                Minecraft.getInstance().setScreen(new OptionsScreen(screen.unsafeScreenAccess()));
            }, Supplier::get, new ItemStack(Items.SPYGLASS)));

            return collection;
        }

        @Override
        public int width() {
            return 20;
        }

        @Override
        public int height() {
            return 20;
        }
    }
}
