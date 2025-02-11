package dev.ftb.mods.pmapi.api;

import dev.ftb.mods.pmapi.api.menu.MenuLocation;
import dev.ftb.mods.pmapi.api.menu.PauseItemHolder;
import dev.ftb.mods.pmapi.api.menu.PauseItemProvider;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public enum PauseMenuApi {
    INSTANCE;

    public static final String MOD_ID = "ftbpmapi";

    private final EnumMap<MenuLocation, HashSet<PauseItemHolder>> pauseItems = new EnumMap<>(MenuLocation.class);

    public static PauseMenuApi get() {
        return INSTANCE;
    }

    public void registerPauseItem(MenuLocation target, PauseItemProvider provider) {
        var providers = pauseItems.computeIfAbsent(target, k -> new HashSet<>());

        var holder = PauseItemHolder.create(target, provider);
        providers.add(holder);

        pauseItems.put(target, providers);
    }

    public EnumMap<MenuLocation, HashSet<PauseItemHolder>> getPauseItems() {
        return pauseItems;
    }
}
