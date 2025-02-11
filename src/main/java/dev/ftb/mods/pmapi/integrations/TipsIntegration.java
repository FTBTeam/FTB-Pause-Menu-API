package dev.ftb.mods.pmapi.integrations;

import dev.ftb.mods.pmapi.menu.PauseMenuScreen;
import net.darkhax.tipsmod.common.api.TipsAPI;

public class TipsIntegration {
    public static void init() {
        TipsAPI.registerDefaultScreen(PauseMenuScreen.class);
    }
}
