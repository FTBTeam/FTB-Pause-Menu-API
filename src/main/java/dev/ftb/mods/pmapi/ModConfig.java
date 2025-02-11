package dev.ftb.mods.pmapi;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public final ModConfigSpec.BooleanValue enabled;
    public final ModConfigSpec.BooleanValue enableGoToOriginal;
    public final ModConfigSpec.BooleanValue enableSupportLinks;

    public final ModConfigSpec.ConfigValue<String> discordLink;
    public final ModConfigSpec.ConfigValue<String> githubLink;

    public final ModConfigSpec.BooleanValue removeAdvancementsButton;
    public final ModConfigSpec.BooleanValue removeStatsButton;

    public ModConfig(ModConfigSpec.Builder builder) {
        this.enabled = builder.comment("Enable the Pause Menu API").define("enabled", true);

        builder.push("builtInFeatures");
        builder.comment("Built in features that can be enabled or disabled");

        this.enableGoToOriginal = builder.comment("Enable the Go To Original button on the main menu").define("enableGoToOriginal", false);
        this.enableSupportLinks = builder.comment("Enable the built in support links to the main menu").define("enableSupportLinks", true);

        builder.pop();

        builder.push("supportLinks");
        builder.comment("Support links that can be configured for the built in support links");
        this.discordLink = builder.comment("The Discord link to open when the Discord button is clicked").define("discordLink", "https://go.ftb.team/discord");
        this.githubLink = builder.comment("The GitHub link to open when the GitHub button is clicked").define("githubLink", "https://go.ftb.team/support-modpack");
        builder.pop();

        builder.push("removeButtons");
        builder.comment("Remove buttons from the stock pause menu");
        this.removeAdvancementsButton = builder.comment("Remove the Advancements button from the pause menu").define("removeAdvancementsButton", false);
        this.removeStatsButton = builder.comment("Remove the Stats button from the pause menu").define("removeStatsButton", false);
        builder.pop();
    }
}
