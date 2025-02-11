package dev.ftb.mods.pmapi.integrations;

import net.neoforged.fml.ModList;

public class Integrations {
    public static void init() {
        runIfLoaded("tipsmod", TipsIntegration::init);
        runIfLoaded("justzoom", JustZoomIntegration::init);
    }

    private static void runIfLoaded(String modid, Runnable runnable) {
        if (ModList.get().isLoaded(modid)) {
            runnable.run();
        }
    }
}
