package dev.ftb.mods.pmapi;

import dev.ftb.mods.pmapi.api.menu.MenuLocation;
import dev.ftb.mods.pmapi.api.PauseMenuApi;
import dev.ftb.mods.pmapi.integrations.Integrations;
import dev.ftb.mods.pmapi.menu.providers.OriginalPauseMenuButtonProvider;
import dev.ftb.mods.pmapi.menu.providers.SupportPauseItemProvider;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(PauseMenuApi.MOD_ID)
public class PauseMenuMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static PauseMenuMod INSTANCE;
    public final Pair<ModConfig, ModConfigSpec> CONFIG_SPEC;

    public PauseMenuMod(IEventBus modEventBus, ModContainer modContainer) {
        INSTANCE = this;

        modEventBus.addListener(this::clientSetup);
        CONFIG_SPEC = new ModConfigSpec.Builder().configure(ModConfig::new);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.CLIENT, CONFIG_SPEC.getRight());
    }

    public void clientSetup(FMLClientSetupEvent event) {
        Integrations.init();

        if (config().enableSupportLinks.get()) {
            PauseMenuApi.get().registerPauseItem(MenuLocation.TOP_LEFT, new SupportPauseItemProvider());
        }

        if (config().enableGoToOriginal.get()) {
            PauseMenuApi.get().registerPauseItem(MenuLocation.TOP_RIGHT, new OriginalPauseMenuButtonProvider());
        }
    }

    public static PauseMenuMod get() {
        return INSTANCE;
    }

    public static ModConfig config() {
        return get().CONFIG_SPEC.getLeft();
    }
}
