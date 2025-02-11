package dev.ftb.mods.pmapi.api.menu;

import java.util.UUID;

public record PauseItemHolder(
        UUID uuid,
        MenuLocation location,
        PauseItemProvider provider
) {
    public static PauseItemHolder create(MenuLocation location, PauseItemProvider provider) {
        return new PauseItemHolder(UUID.randomUUID(), location, provider);
    }
}
