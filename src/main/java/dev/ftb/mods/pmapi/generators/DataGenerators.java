package dev.ftb.mods.pmapi.generators;

import dev.ftb.mods.pmapi.api.PauseMenuApi;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = PauseMenuApi.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(event.includeClient(), new LanguageGenerator(packOutput));
    }

    private static final class LanguageGenerator extends LanguageProvider {
        public LanguageGenerator(PackOutput output) {
            super(output,  PauseMenuApi.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            add("ftbpmapi.generic.mods", "Mods (%s)");
            add("ftbpmapi.tooltip.support_discord", "Need help? Join our Discord server!");
            add("ftbpmapi.tooltip.support_github", "Found a bug? Report it on GitHub!");
        }
    }
}
