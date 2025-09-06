package net.artmaster.openpacbp.mixin;

import net.artmaster.openpacbp.gui.PartyGUIRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.client.ClientTickHandler;
import xaero.pac.client.event.ClientEvents;

@Mixin(ClientTickHandler.class) // <- реальный класс, куда делегирует ClientEvents
public class MixinOPACClientTickHandler {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
            ),
            cancellable = true
    )
    private void replaceMenu(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new PartyGUIRenderer()); // твой экран
        ci.cancel(); // отменить открытие оригинального меню OPaC
    }
}
