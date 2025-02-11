package dev.ftb.mods.pmapi.menu;

import com.mojang.realmsclient.RealmsMainScreen;
import dev.ftb.mods.pmapi.PauseMenuMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerLinksScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerLinks;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.ModListScreen;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A clean implementation of the original pause screen
 * <p>
 * We then extend this in {@link PauseMenuScreen} to add our own data
 */
public class OriginalPauseScreenImpl extends Screen {
    private static final ResourceLocation DRAFT_REPORT_SPRITE = ResourceLocation.withDefaultNamespace("icon/draft_report");

    public static final Component RETURN_TO_GAME = Component.translatable("menu.returnToGame");
    public static final Component ADVANCEMENTS = Component.translatable("gui.advancements");
    public static final Component STATS = Component.translatable("gui.stats");

    public static final Component SERVER_LINKS = Component.translatable("menu.server_links");
    public static final Component OPTIONS = Component.translatable("menu.options");
    public static final Component SHARE_TO_LAN = Component.translatable("menu.shareToLan");
    public static final Component PLAYER_REPORTING = Component.translatable("menu.playerReporting");
    public static final Component RETURN_TO_MENU = Component.translatable("menu.returnToMenu");

    private static final int BUTTON_WIDTH_HALF = 98;
    private static final int BUTTON_WIDTH_FULL = 204;

    @Nullable
    private Button disconnectButton;

    public OriginalPauseScreenImpl() {
        super(Component.translatable("menu.game"));
    }

    /**
     * Basically minecrafts layout system but modified a touch
     */
    @Override
    protected void init() {
        var mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return; // How?
        }

        this.addRenderableWidget(new StringWidget(0, 40, this.width, 9, this.title, this.font));

        GridLayout layout = new GridLayout();
        layout.defaultCellSetting().padding(4, 4, 4, 0);

        GridLayout.RowHelper rowHelper = layout.createRowHelper(2);
        rowHelper.addChild(Button.builder(RETURN_TO_GAME, p_280814_ -> {
            mc.setScreen(null);
            mc.mouseHandler.grabMouse();
        }).width(BUTTON_WIDTH_FULL).build(), 2, layout.newCellSettings().paddingTop(50));

        Boolean advancementsDisabled = PauseMenuMod.config().removeAdvancementsButton.get();
        if (!advancementsDisabled) {
            rowHelper.addChild(
                    this.openScreenButton(ADVANCEMENTS, () -> new AdvancementsScreen(player.connection.getAdvancements(), this))
            );
        }

        rowHelper.addChild(this.openScreenButton(STATS, () -> new StatsScreen(this, player.getStats())));

        ServerLinks serverlinks = player.connection.serverLinks();
        if (!serverlinks.isEmpty()) {
            rowHelper.addChild(this.openScreenButton(SERVER_LINKS, () -> new ServerLinksScreen(this, serverlinks)));
        }

        rowHelper.addChild(this.openScreenButton(OPTIONS, () -> new OptionsScreen(this, mc.options)));
        if (mc.hasSingleplayerServer() && !mc.getSingleplayerServer().isPublished()) {
            rowHelper.addChild(this.openScreenButton(SHARE_TO_LAN, () -> new ShareToLanScreen(this)));
        } else {
            rowHelper.addChild(this.openScreenButton(PLAYER_REPORTING, () -> new SocialInteractionsScreen(this)));
        }

        Button.Builder modsButtonBuilder = Button.builder(Component.translatable("ftbpmapi.generic.mods", ModList.get().size()), button -> mc.setScreen(new ModListScreen(this)))
                .width(advancementsDisabled ? BUTTON_WIDTH_HALF : BUTTON_WIDTH_FULL);

        rowHelper.addChild(modsButtonBuilder.build(), advancementsDisabled ? 1 : 2);

        Component component = mc.isLocalServer() ? RETURN_TO_MENU : CommonComponents.GUI_DISCONNECT;
        this.disconnectButton = rowHelper.addChild(Button.builder(component, button -> {
            button.active = false;
            mc.getReportingContext().draftReportHandled(mc, this, this::onDisconnect, true);
        }).width(204).build(), 2);

        layout.arrangeElements();
        FrameLayout.alignInRectangle(layout, 0, 0, this.width, this.height, 0.5F, 0.25F);
        layout.visitWidgets(this::addRenderableWidget);
    }

    public void onDisconnect() {
        var mc = Minecraft.getInstance();
        boolean flag = mc.isLocalServer();
        ServerData serverdata = mc.getCurrentServer();
        mc.level.disconnect();
        if (flag) {
            mc.disconnect(new GenericMessageScreen(ConfigurationScreen.SAVING_LEVEL));
        } else {
            mc.disconnect();
        }

        TitleScreen titlescreen = new TitleScreen();
        if (flag) {
            mc.setScreen(titlescreen);
        } else if (serverdata != null && serverdata.isRealm()) {
            mc.setScreen(new RealmsMainScreen(titlescreen));
        } else {
            mc.setScreen(new JoinMultiplayerScreen(titlescreen));
        }
    }

    public Button openScreenButton(Component message, Supplier<Screen> screenSupplier) {
        return Button.builder(message, p_280817_ -> Minecraft.getInstance().setScreen(screenSupplier.get())).width(98).build();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var mc = Minecraft.getInstance();
        if (mc == null) {
            return;
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (mc.getReportingContext().hasDraftReport() && this.disconnectButton != null) {
            guiGraphics.blitSprite(
                    DRAFT_REPORT_SPRITE, this.disconnectButton.getX() + this.disconnectButton.getWidth() - 17, this.disconnectButton.getY() + 3, 15, 15
            );
        }
    }
}
