package dev.ftb.mods.pmapi.mixin;

import dev.ftb.mods.pmapi.PauseMenuMod;
import dev.ftb.mods.pmapi.menu.PauseMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseMenuMixin extends Screen {
    protected PauseMenuMixin(Component component) {
        super(component);
    }

    @Inject(method = "createPauseMenu", at = @At("HEAD"), cancellable = true)
    private void ftbpmapi$createPauseMenu(CallbackInfo ci) {
        if (!PauseMenuMod.config().enabled.get()) {
            return;
        }

        if (!PauseMenuScreen.DISABLE_CUSTOM_PAUSE) {
            Minecraft.getInstance().setScreen(new PauseMenuScreen());
            ci.cancel();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        PauseMenuScreen.DISABLE_CUSTOM_PAUSE = false;
    }

    @Inject(method = "lambda$createPauseMenu$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void ftbpmapi$onCloseButtonPressed(CallbackInfo ci) {
        PauseMenuScreen.DISABLE_CUSTOM_PAUSE = false;
    }
}
